import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.util.ReflectionTestUtils;
import raf.si.bolnica.user.controllers.UserController;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;
import raf.si.bolnica.user.repositories.RoleRepository;
import raf.si.bolnica.user.requests.CreateEmployeeRequestDTO;
import raf.si.bolnica.user.requests.ListEmployeesRequestDTO;
import raf.si.bolnica.user.requests.UpdateEmployeeRequestDTO;
import raf.si.bolnica.user.responses.UserDataResponseDTO;
import raf.si.bolnica.user.responses.UserResponseDTO;
import raf.si.bolnica.user.service.EmailService;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    UserService userService;

    @Mock
    OdeljenjeService odeljenjeService;

    @Mock
    private EmailService emailService;

    @Mock
    UserExceptionHandler userExceptionHandler;

    @InjectMocks
    UserController userController;

    @Mock
    EntityManager entityManager;

    @Mock
    RoleRepository roleRepository;

    public User getUser(){
        User user1 = new User();
        user1.setEmail("test1@gmail.com");
        user1.setPassword(BCrypt.hashpw("superadmin", BCrypt.gensalt()));
        user1.setName("Super");
        user1.setSurname("Admin");
        Set<Role> roles = new HashSet<>();
        Role drSpecRole = new Role();
        drSpecRole.setName("ROLE_DR_SPEC");
        roles.add(drSpecRole);
        user1.setRoles(roles);

        ZdravstvenaUstanova zdravstvenaUstanova = new ZdravstvenaUstanova();
        zdravstvenaUstanova.setAdresa("Heroja Milana Tepića 1, Beograd");
        zdravstvenaUstanova.setDatumOsnivanja(new Date(Calendar.getInstance().getTime().getTime()));
        zdravstvenaUstanova.setDelatnost("Ginekologija i akušerstvo");
        zdravstvenaUstanova.setMesto("Beograd");
        zdravstvenaUstanova.setNaziv("Kliničko-bolnički centar \"Dragiša Mišović\"");
        zdravstvenaUstanova.setPoslovniBrojBolnice(1234);
        zdravstvenaUstanova.setSkracenNaziv("KBC Dragiša Mišović");

        Odeljenje odeljenje = new Odeljenje();
        odeljenje.setNaziv("Hirurgija");
        odeljenje.setBolnica(zdravstvenaUstanova);
        odeljenje.setPoslovniBrojOdeljenja(12345);
        user1.setOdeljenje(odeljenje);

        user1.setOdeljenje(odeljenje);

        //admin user fields
        user1.setLbz(UUID.randomUUID());
        user1.setName("admin");
        user1.setSurname("adminic");
        user1.setDatumRodjenja(new Date(System.currentTimeMillis()));
        user1.setPol("Muski");
        user1.setJmbg("123456789");
        user1.setAdresaStanovanja("adresa 1");
        user1.setMestoStanovanja("SRBIJA");
        user1.setTitula("titula");
        user1.setKorisnickoIme("superadminn");
        user1.setZanimanje("zanimanje");

        return user1;
    }

    public CreateEmployeeRequestDTO createEmployeeRequestDTO() {
        CreateEmployeeRequestDTO requestDTO = new CreateEmployeeRequestDTO();
        requestDTO.setEmail("email@email.com");
        requestDTO.setName("name");
        requestDTO.setSurname("surname");
        requestDTO.setJmbg("jmbg");
        requestDTO.setContact("kontakt");
        requestDTO.setAddress("adresa");
        requestDTO.setCity("grad");
        requestDTO.setDepartment(1);
        requestDTO.setDob(Date.valueOf(LocalDate.now().minusYears(40)));
        requestDTO.setGender("male");
        requestDTO.setTitle("Dipl. farm.");
        requestDTO.setProfession("Spec. hematolog");
        return requestDTO;
    }

    public UpdateEmployeeRequestDTO updateEmployeeRequestDTO() {
        UpdateEmployeeRequestDTO requestDTO = new UpdateEmployeeRequestDTO();
        requestDTO.setEmail("email@email.com");
        requestDTO.setName("name");
        requestDTO.setSurname("surname");
        requestDTO.setJmbg("jmbg");
        requestDTO.setContact("kontakt");
        requestDTO.setAddress("adresa");
        requestDTO.setCity("grad");
        requestDTO.setDepartment(1);
        requestDTO.setDob(Date.valueOf(LocalDate.now().minusYears(40)));
        requestDTO.setGender("male");
        requestDTO.setTitle("Dipl. farm.");
        requestDTO.setProfession("Spec. hematolog");
        requestDTO.setOldPassword("stari");
        requestDTO.setNewPassword("novi");
        requestDTO.setLbz(UUID.randomUUID());
        return requestDTO;
    }

    @Test
    public void fetchUserUsername() {
        User u = getUser();
        when(userService.fetchUserByEmail(u.getEmail())).thenReturn(u);

        ResponseEntity<?> response = userController.fetchUserByUsername(u.getEmail());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(UserResponseDTO.class);

        UserResponseDTO responseDTO = (UserResponseDTO) response.getBody();

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getEmail()).isEqualTo(u.getEmail());
        assertThat(responseDTO.getName()).isEqualTo(u.getName());
        assertThat(responseDTO.getPassword()).isEqualTo(u.getPassword());
        assertThat(responseDTO.getSurname()).isEqualTo(u.getSurname());
        assertThat(responseDTO.getUserId()).isEqualTo(u.getUserId());
        assertThat(responseDTO.getRoles()).isEqualTo(u.getRoles());
    }

    @Test
    public void fetchUserUsernameNoSuchUser() {
        User u = getUser();
        when(userService.fetchUserByEmail(any(String.class))).thenAnswer(i -> {
            if(i.getArguments()[0].equals(u.getEmail())) return u;
            else return null;
        });

        ResponseEntity<?> response = userController.fetchUserByUsername("nepostojeci");

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }


    @Test
    void forgotPassword(){
        User u = getUser();

        when(loggedInUser.getUsername()).thenReturn(u.getEmail());

        when(userService.fetchUserByEmail(u.getEmail())).thenReturn(u);

        String pas = "123";
        when(userService.generateNewPassword(u)).thenReturn(pas);

        when(emailService.sendEmail(loggedInUser.getUsername(), pas)).thenReturn(true);

        ResponseEntity<?> responseForgot = userController.forgotPassword();

        assertThat(responseForgot.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void forgotPasswordNoSuchUser(){
        User u = getUser();

        when(loggedInUser.getUsername()).thenReturn(u.getEmail());

        when(userService.fetchUserByEmail(u.getEmail())).thenReturn(null);

        ResponseEntity<?> responseForgot = userController.forgotPassword();

        assertThat(responseForgot.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void removeEmployee(){
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        User u = getUser();

        when(userService.fetchUserByLBZ(u.getLbz())).thenReturn(u);

        ResponseEntity<?> response = userController.removeEmployee(u.getLbz().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void removeEmployeeUnauthorized(){
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        User u = getUser();

        when(userService.fetchUserByLBZ(u.getLbz())).thenReturn(null);

        ResponseEntity<?> response = userController.removeEmployee(u.getLbz().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void removeEmployeeSelfErase(){
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        String email = "email@email.com";

        User u = getUser();

        u.setEmail(email);

        when(loggedInUser.getUsername()).thenReturn(email);

        when(userService.fetchUserByLBZ(u.getLbz())).thenReturn(u);

        ResponseEntity<?> response = userController.removeEmployee(u.getLbz().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void removeEmployeeNoSuchUser(){
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_MED_SESTRA");

        when(loggedInUser.getRoles()).thenReturn(roles);

        User u = getUser();

        ResponseEntity<?> response = userController.removeEmployee(u.getLbz().toString());



        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void createEmployeeSuccess() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(userService.saveEmployee(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        CreateEmployeeRequestDTO requestDTO = createEmployeeRequestDTO();

        when(odeljenjeService.fetchOdeljenjeById(any(Long.class))).thenAnswer(i -> {
            Odeljenje odeljenje = new Odeljenje();
            odeljenje.setOdeljenjeId((Long)i.getArguments()[0]);
            return odeljenje;
        });

        when(roleRepository.findByName(any(String.class))).thenReturn(new Role());

        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler,"validateUsername",handler.validateUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserGender",handler.validateUserGender);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserTitle", handler.validateUserTitle);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserProfession",handler.validateUserProfession);

        ResponseEntity<?> response = userController.createEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(UserDataResponseDTO.class);

        UserDataResponseDTO data = (UserDataResponseDTO) response.getBody();

        assertThat(data).isNotNull();

        assertThat(data.getAddress()).isEqualTo(requestDTO.getAddress());
        assertThat(data.getCity()).isEqualTo(requestDTO.getCity());
        assertThat(data.getContact()).isEqualTo(requestDTO.getContact());
        assertThat(data.getCity()).isEqualTo(requestDTO.getCity());
        assertThat(data.getEmail()).isEqualTo(requestDTO.getEmail());
        assertThat(data.getLbz()).isNotNull();
        assertThat(data.getDob()).isEqualTo(requestDTO.getDob());
        assertThat(data.getDepartment()).isEqualTo(requestDTO.getDepartment());
        assertThat(data.getGender()).isEqualTo(requestDTO.getGender());
        assertThat(data.getName()).isEqualTo(requestDTO.getName());
        assertThat(data.getJmbg()).isEqualTo(requestDTO.getJmbg());
        assertThat(data.getSurname()).isEqualTo(requestDTO.getSurname());
        assertThat(data.getProfession()).isEqualTo(requestDTO.getProfession());
        assertThat(data.getTitle()).isEqualTo(requestDTO.getTitle());
    }

    @Test
    void createEmployeeUnauthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_MED_SESTRA");

        when(loggedInUser.getRoles()).thenReturn(roles);

        ResponseEntity<?> response = userController.createEmployee(createEmployeeRequestDTO());

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void adminUpdateEmployeeNoSuchUser() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(userService.fetchUserByLBZ(any(UUID.class))).thenReturn(null);

        UpdateEmployeeRequestDTO requestDTO = updateEmployeeRequestDTO();

        when(odeljenjeService.fetchOdeljenjeById(any(Long.class))).thenAnswer(i -> {
            Odeljenje odeljenje = new Odeljenje();
            odeljenje.setOdeljenjeId((Long)i.getArguments()[0]);
            return odeljenje;
        });

        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler,"validateUsername",handler.validateUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserGender",handler.validateUserGender);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserTitle", handler.validateUserTitle);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserProfession",handler.validateUserProfession);

        ResponseEntity<?> response = userController.updateEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void adminUpdateEmployeeSuccess() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(userService.fetchUserByLBZ(any(UUID.class))).thenReturn(new User());

        when(userService.saveEmployee(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UpdateEmployeeRequestDTO requestDTO = updateEmployeeRequestDTO();

        when(odeljenjeService.fetchOdeljenjeById(any(Long.class))).thenAnswer(i -> {
            Odeljenje odeljenje = new Odeljenje();
            odeljenje.setOdeljenjeId((Long)i.getArguments()[0]);
            return odeljenje;
        });

        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler,"validateUsername",handler.validateUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserGender",handler.validateUserGender);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserTitle", handler.validateUserTitle);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserProfession",handler.validateUserProfession);

        ResponseEntity<?> response = userController.updateEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(User.class);

        User data = (User) response.getBody();

        assertThat(data).isNotNull();

        assertThat(data.getAdresaStanovanja()).isEqualTo(requestDTO.getAddress());
        assertThat(data.getMestoStanovanja()).isEqualTo(requestDTO.getCity());
        assertThat(data.getKontaktTelefon()).isEqualTo(requestDTO.getContact());
        assertThat(data.getDatumRodjenja()).isEqualTo(requestDTO.getDob());
        assertThat(data.getOdeljenje().getOdeljenjeId()).isEqualTo(requestDTO.getDepartment());
        assertThat(data.getPol()).isEqualTo(requestDTO.getGender());
        assertThat(data.getName()).isEqualTo(requestDTO.getName());
        assertThat(data.getJmbg()).isEqualTo(requestDTO.getJmbg());
        assertThat(data.getSurname()).isEqualTo(requestDTO.getSurname());
        assertThat(data.getZanimanje()).isEqualTo(requestDTO.getProfession());
        assertThat(data.getTitula()).isEqualTo(requestDTO.getTitle());
    }

    @Test
    void userUpdateEmployeeUnauthorized() {
        Set<String> roles = new TreeSet<>();

        when(loggedInUser.getLBZ()).thenReturn(UUID.randomUUID());

        when(loggedInUser.getRoles()).thenReturn(roles);

        UpdateEmployeeRequestDTO requestDTO = updateEmployeeRequestDTO();

        when(userService.fetchUserByLBZ(any(UUID.class))).thenAnswer(i -> {
            User u = new User();
            u.setLbz(requestDTO.getLbz());
            return u;
        });

        ResponseEntity<?> response = userController.updateEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void userUpdateEmployeeWithData() {
        Set<String> roles = new TreeSet<>();

        UpdateEmployeeRequestDTO requestDTO = updateEmployeeRequestDTO();

        when(loggedInUser.getLBZ()).thenReturn(requestDTO.getLbz());

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(userService.fetchUserByLBZ(any(UUID.class))).thenAnswer(i -> {
            User u = new User();
            u.setLbz(requestDTO.getLbz());
            u.setPassword(BCrypt.hashpw(requestDTO.getOldPassword(), BCrypt.gensalt()));
            return u;
        });

        ResponseEntity<?> response = userController.updateEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void userUpdateEmployeeNoData() {
        Set<String> roles = new TreeSet<>();

        UpdateEmployeeRequestDTO requestDTO = updateEmployeeRequestDTO();

        requestDTO.setContact(null);

        requestDTO.setNewPassword(null);

        when(loggedInUser.getLBZ()).thenReturn(requestDTO.getLbz());

        when(loggedInUser.getRoles()).thenReturn(roles);

        String pass = BCrypt.hashpw(requestDTO.getOldPassword(), BCrypt.gensalt());


        when(userService.fetchUserByLBZ(any(UUID.class))).thenAnswer(i -> {
            User u = new User();
            u.setLbz(requestDTO.getLbz());
            u.setPassword(pass);
            u.setKontaktTelefon("telefon");
            return u;
        });

        when(userService.saveEmployee(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<?> response = userController.updateEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(User.class);

        User u = (User)response.getBody();

        assertThat(u).isNotNull();

        assertThat(u.getKontaktTelefon()).isEqualTo("telefon");

        assertThat(u.getPassword()).isEqualTo(pass);
    }

    @Test
    void userUpdateEmployeeInvalidPass() {
        Set<String> roles = new TreeSet<>();

        UpdateEmployeeRequestDTO requestDTO = updateEmployeeRequestDTO();

        requestDTO.setContact(null);

        requestDTO.setNewPassword(null);

        when(loggedInUser.getLBZ()).thenReturn(requestDTO.getLbz());

        when(loggedInUser.getRoles()).thenReturn(roles);

        String pass = BCrypt.hashpw(requestDTO.getOldPassword(), BCrypt.gensalt());

        String realPass = BCrypt.hashpw("sifra", BCrypt.gensalt());


        when(userService.fetchUserByLBZ(any(UUID.class))).thenAnswer(i -> {
            User u = new User();
            u.setLbz(requestDTO.getLbz());
            u.setPassword(realPass);
            u.setKontaktTelefon("telefon");
            return u;
        });

        when(userService.saveEmployee(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<?> response = userController.updateEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(User.class);

        User u = (User)response.getBody();

        assertThat(u).isNotNull();

        assertThat(u.getKontaktTelefon()).isEqualTo("telefon");

        assertThat(u.getPassword()).isEqualTo(realPass);
    }

    @Test
    public void getEmployeeAdmin() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(userService.fetchUserByLBZ(any(UUID.class))).thenAnswer(i -> {
            User u = new User();
            u.setLbz((UUID)i.getArguments()[0]);
            u.setOdeljenje(new Odeljenje());
            return u;
        });

        ResponseEntity<?> response = userController.getEmployee(UUID.randomUUID().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getEmployeeMe() {
        Set<String> roles = new TreeSet<>();

        UUID uuid = UUID.randomUUID();

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(loggedInUser.getLBZ()).thenReturn(uuid);

        when(userService.fetchUserByLBZ(any(UUID.class))).thenAnswer(i -> {
                User u = new User();
                u.setLbz((UUID)i.getArguments()[0]);
                u.setOdeljenje(new Odeljenje());
                return u;
        });

        ResponseEntity<?> response = userController.getEmployee(uuid.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getEmployeeUnauthorized() {
        Set<String> roles = new TreeSet<>();

        UUID uuid = UUID.randomUUID();

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(loggedInUser.getLBZ()).thenReturn(uuid);

        ResponseEntity<?> response = userController.getEmployee(UUID.randomUUID().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listEmployeesUnauthorized() {
        Set<String> roles = new TreeSet<>();

        UUID uuid = UUID.randomUUID();

        when(loggedInUser.getRoles()).thenReturn(roles);

        ListEmployeesRequestDTO requestDTO = new ListEmployeesRequestDTO();

        ResponseEntity<?> response = userController.listEmployees(requestDTO,2,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listEmployeesEmptyRequest() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        ListEmployeesRequestDTO requestDTO = new ListEmployeesRequestDTO();

        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(eq("SELECT u FROM User u WHERE  u.obrisan = :obrisan "),any(Class.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());

        ResponseEntity<?> response = userController.listEmployees(requestDTO,2,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }


    @Test
    public void listEmployeesFullRequest() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        ListEmployeesRequestDTO requestDTO = new ListEmployeesRequestDTO();
        requestDTO.setDepartment(1L);
        requestDTO.setName("name");
        requestDTO.setSurname("surname");
        requestDTO.setHospital(1L);

        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(eq("SELECT u FROM User u INNER JOIN u.odeljenje o INNER JOIN o.bolnica z WHERE  u.name like :name  AND  u.surname like :surname  AND  u.obrisan = :obrisan  AND  o.odeljenjeId = :odeljenje AND  z.zdravstvenaUstanovaId = :bolnica"),any(Class.class))).thenReturn(query);
        ArrayList<User> a = new ArrayList();
        a.add(getUser());
        when(query.getResultList()).thenReturn(a);

        ResponseEntity<?> response = userController.listEmployees(requestDTO,2,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void listEmployeesPBOUnauthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        ResponseEntity<?> response = userController.listEmployeesByPbo(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listEmployeesPBO() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_VISA_MED_SESTRA");

        when(loggedInUser.getRoles()).thenReturn(roles);

        ArrayList<User> a = new ArrayList();
        User u1 = new User();
        u1.setObrisan(true);
        u1.setOdeljenje(new Odeljenje());
        User u2 = new User();
        u2.setObrisan(false);
        u2.setOdeljenje(u1.getOdeljenje());
        a.add(u1);
        a.add(u2);
        when(userService.fetchUsersByPBO(any(Long.class))).thenReturn(a);


        ResponseEntity<?> response = userController.listEmployeesByPbo(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(List.class);

        List<UserDataResponseDTO> lista = (List)(response.getBody());
        assertThat(lista).isNotNull();
        assertThat(lista.size()).isEqualTo(1);
    }

}