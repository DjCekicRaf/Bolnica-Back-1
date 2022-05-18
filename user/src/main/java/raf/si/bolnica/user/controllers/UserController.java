package raf.si.bolnica.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.repositories.RoleRepository;
import raf.si.bolnica.user.requests.CreateEmployeeRequestDTO;
import raf.si.bolnica.user.requests.ListEmployeesRequestDTO;
import raf.si.bolnica.user.requests.UpdateEmployeeRequestDTO;
import raf.si.bolnica.user.responses.UserDataResponseDTO;
import raf.si.bolnica.user.responses.UserResponseDTO;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;
import raf.si.bolnica.user.service.EmailService;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = Constants.BASE_API)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserExceptionHandler userExceptionHandler;

    @Autowired
    private OdeljenjeService odeljenjeService;

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EntityManager entityManager;

    @GetMapping(value = "/fetch-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> fetchUserByUsername(@RequestParam String username) {
        User user = userService.fetchUserByEmail(username);
        if (user != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUserId(), user.getName(), user.getSurname(), user.getPassword(), user.getEmail(), user.getRoles());
            return ok(userResponseDTO);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword() {

        User user = userService.fetchUserByEmail(loggedInUser.getUsername());

        if (user == null) {
            // Username doesn't exist, return 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            // Username exists, proceeds to generate and save new password, also send email
            String generatedPassword = userService.generateNewPassword(user);

            userService.savePassword(user, generatedPassword);
            emailService.sendEmail(loggedInUser.getUsername(), generatedPassword);

            return ok().build();
        }

    }

    @PostMapping(value = Constants.CREATE_EMPLOYEE)
    public ResponseEntity<?> createEmployee(@RequestBody CreateEmployeeRequestDTO requestDTO) {
        if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {
            Odeljenje odeljenje = odeljenjeService.fetchOdeljenjeById(requestDTO.getDepartment());
            String username = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));
            String password = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));

            userExceptionHandler.validateUsername.accept(username);
            userExceptionHandler.validateUserTitle.accept(requestDTO.getTitle());
            userExceptionHandler.validateUserProfession.accept(requestDTO.getProfession());
            userExceptionHandler.validateUserGender.accept(requestDTO.getGender());

            User user = new User();

            user.setLbz(UUID.randomUUID());
            user.setOdeljenje(odeljenje);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setKorisnickoIme(username);
            user.setEmail(requestDTO.getEmail());
            user.setName(requestDTO.getName());
            user.setSurname(requestDTO.getSurname());
            user.setAdresaStanovanja(requestDTO.getAddress());
            user.setDatumRodjenja(requestDTO.getDob());
            user.setMestoStanovanja(requestDTO.getCity());
            user.setJmbg(requestDTO.getJmbg());
            user.setKontaktTelefon(requestDTO.getContact());
            user.setPol(requestDTO.getGender());
            user.setTitula(requestDTO.getTitle());
            user.setZanimanje(requestDTO.getProfession());

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_ADMIN"));
            user.setRoles(roles);

            User userToReturn = userService.saveEmployee(user);

            return ok(new UserDataResponseDTO(userToReturn));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @DeleteMapping(value = Constants.REMOVE_EMPLOYEE)
    public ResponseEntity<?> removeEmployee(@RequestParam String lbz) {
        if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {

            User user = userService.fetchUserByLBZ(UUID.fromString(lbz));

            if (user == null) {
                return ResponseEntity.status(403).build();
            }

            if (user.getEmail().equals(loggedInUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            user.setObrisan(true);
            userService.saveEmployee(user);

            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = Constants.GET_EMPLOYEE)
    public ResponseEntity<UserDataResponseDTO> getEmployee(@PathVariable String lbz) {

        if (loggedInUser.getRoles().contains("ROLE_ADMIN") || loggedInUser.getLBZ().equals(UUID.fromString(lbz))) {
            User user = userService.fetchUserByLBZ(UUID.fromString(lbz));
            UserDataResponseDTO userDataResponseDTO = new UserDataResponseDTO(user);
            return ok(userDataResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = Constants.LIST_EMPLOYEES_BY_PBO)
    public ResponseEntity<List<UserDataResponseDTO>> listEmployeesByPbo(@PathVariable Long pbo) {
        // Načelnik odeljenja, Doktor specijalista, Viša medicinska sestra i Medicinska sestra.
        String[] rolesPermited = {"ROLE_DR_SPEC_ODELJENJA", "ROLE_DR_SPEC", "ROLE_VISA_MED_SESTRA", "ROLE_MED_SESTRA"};

        for (int i = 0; i < 4; i++) {
            if (loggedInUser.getRoles().contains(rolesPermited[i])) {
                List<User> users = userService.fetchUsersByPBO(pbo);
                List<UserDataResponseDTO> userDataResponseDTOS = new ArrayList<>();
                for (User user : users) {
                    if (!user.isObrisan())
                        userDataResponseDTOS.add(new UserDataResponseDTO(user));
                }
                return ok(userDataResponseDTOS);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(value = Constants.LIST_EMPLOYEES)
    public ResponseEntity<List<UserDataResponseDTO>> listEmployees(@RequestBody ListEmployeesRequestDTO requestDTO,
                                                                   @RequestParam int page,
                                                                   @RequestParam int size) {
        if (!loggedInUser.getRoles().contains("ROLE_ADMIN"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        String s = "SELECT u FROM User u";

        HashMap<String, Object> param = new HashMap<>();

        if (requestDTO.getDepartment() != null || requestDTO.getHospital() != null) {
            s = s + " INNER JOIN u.odeljenje o";
        }

        if (requestDTO.getHospital() != null) {
            s = s + " INNER JOIN o.bolnica z";
        }

        String nextOper = " WHERE ";

        if (requestDTO.getName() != null) {
            s = s + nextOper;
            nextOper = " AND ";
            s = s + " u.name like :name ";
            param.put("name", requestDTO.getName());
        }
        if (requestDTO.getSurname() != null) {
            s = s + nextOper;
            nextOper = " AND ";
            s = s + " u.surname like :surname ";
            param.put("surname", requestDTO.getSurname());
        }

        s = s + nextOper;
        nextOper = " AND ";
        s = s + " u.obrisan = :obrisan ";
        param.put("obrisan", false);

        if (requestDTO.getDepartment() != null) {
            s = s + nextOper;
            nextOper = " AND ";
            s = s + " o.odeljenjeId = :odeljenje";
            param.put("odeljenje", requestDTO.getDepartment());
        }

        if (requestDTO.getHospital() != null) {
            s = s + nextOper;
            s = s + " z.zdravstvenaUstanovaId = :bolnica";
            param.put("bolnica", requestDTO.getHospital());
        }

        TypedQuery<User> query
                = entityManager.createQuery(
                s, User.class);
        for (String t : param.keySet()) {
            query.setParameter(t, param.get(t));
        }

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        List<User> users = query.getResultList();

        List<UserDataResponseDTO> userDataResponseDTOList = new ArrayList<>();
        for (User user : users) {
            UserDataResponseDTO userDataResponseDTO = new UserDataResponseDTO(user);
            userDataResponseDTOList.add(userDataResponseDTO);
        }
        return ok(userDataResponseDTOList);
    }

    @PutMapping(value = Constants.UPDATE_EMPLOYEE)
    public ResponseEntity<?> updateEmployee(@RequestBody UpdateEmployeeRequestDTO requestDTO) {
        if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {
            Odeljenje odeljenje = odeljenjeService.fetchOdeljenjeById(requestDTO.getDepartment());
            String password = requestDTO.getNewPassword();

            userExceptionHandler.validateUserTitle.accept(requestDTO.getTitle());
            userExceptionHandler.validateUserProfession.accept(requestDTO.getProfession());
            userExceptionHandler.validateUserGender.accept(requestDTO.getGender());

            User user = userService.fetchUserByLBZ(requestDTO.getLbz());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            user.setOdeljenje(odeljenje);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setKorisnickoIme(user.getKorisnickoIme());
            user.setEmail(user.getEmail());
            user.setName(requestDTO.getName());
            user.setSurname(requestDTO.getSurname());
            user.setAdresaStanovanja(requestDTO.getAddress());
            user.setDatumRodjenja(requestDTO.getDob());
            user.setMestoStanovanja(requestDTO.getCity());
            user.setJmbg(requestDTO.getJmbg());
            user.setKontaktTelefon(requestDTO.getContact());
            user.setPol(requestDTO.getGender());
            user.setTitula(requestDTO.getTitle());
            user.setZanimanje(requestDTO.getProfession());

            User userToReturn = userService.saveEmployee(user);
            return ok(userToReturn);
        } else {
            User requestUser = userService.fetchUserByLBZ(requestDTO.getLbz());
            if (loggedInUser.getLBZ().equals(requestUser.getLbz())) {
                User user = userService.fetchUserByLBZ(requestDTO.getLbz());
                if (requestDTO.getContact() != null) {
                    user.setKontaktTelefon(requestDTO.getContact());
                }
                if (requestDTO.getNewPassword() != null && BCrypt.checkpw(requestDTO.getOldPassword(),user.getPassword())) {
                    user.setPassword(BCrypt.hashpw(requestDTO.getNewPassword(), BCrypt.gensalt()));
                }
                user = userService.saveEmployee(user);
                return ok(user);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
