package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.Vakcina;
import raf.si.bolnica.management.entities.Vakcinacija;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.exceptions.VaccineNotExistException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.AddVaccineToPatientRequestDTO;
import raf.si.bolnica.management.services.VakcinacijaService;
import raf.si.bolnica.management.services.vakcina.VakcinaService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = Constants.BASE_API)
public class VakcinaController {

    @Autowired
    private VakcinacijaService vakcinacijaService;

    @Autowired
    private VakcinaService vakcinaService;

    @Autowired
    private ZdravstveniKartonService zdravstveniKartonService;

    @Autowired
    private LoggedInUser loggedInUser;

    @PostMapping(value = Constants.ADD_VACCINE_TO_PATIENT)
    public ResponseEntity<?> addVaccineToPatient(@RequestBody AddVaccineToPatientRequestDTO requestDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.NACELNIK);
        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {

            if (requestDTO.getLbp() == null || requestDTO.getNaziv() == null || requestDTO.getDatumVakcinacije() == null)
                throw new MissingRequestFieldsException(Constants.MissingRequestFields.MESSAGE, Constants.MissingRequestFields.DEVELOPER_MESSAGE);

            Vakcina vakcina = vakcinaService.findVakcinaByNaziv(requestDTO.getNaziv());

            if (vakcina == null) {
                throw new VaccineNotExistException(
                        Constants.VaccineNotExist.MESSAGE,
                        Constants.VaccineNotExist.DEVELOPER_MESSAGE
                );
            } else {
                ZdravstveniKarton zdravstveniKarton = zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(UUID.fromString(requestDTO.getLbp()));
                if (zdravstveniKarton == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                Vakcinacija vakcinacija = new Vakcinacija();
                vakcinacija.setVakcina(vakcina);
                vakcinacija.setDatumVakcinacije(requestDTO.getDatumVakcinacije());
                vakcinacija.setZdravstveniKarton(zdravstveniKarton);


                Vakcinacija vakcinacijaToReturn = vakcinacijaService.save(vakcinacija);

                return ResponseEntity.ok(vakcinacijaToReturn);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
