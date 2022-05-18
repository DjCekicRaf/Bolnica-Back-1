import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.VakcinaController;
import raf.si.bolnica.management.entities.Vakcina;
import raf.si.bolnica.management.entities.Vakcinacija;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.exceptionHandler.medicalRecord.AllergenRecordExceptionHandler;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.exceptions.VaccineNotExistException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.AddVaccineToPatientRequestDTO;
import raf.si.bolnica.management.services.AlergenZdravstveniKartonService;
import raf.si.bolnica.management.services.VakcinacijaService;
import raf.si.bolnica.management.services.vakcina.VakcinaService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VaccineTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    VakcinaService vakcinaService;

    @Mock
    AlergenZdravstveniKartonService alergenZdravstveniKartonService;

    @Mock
    AllergenRecordExceptionHandler allergenRecordExceptionHandler;

    @Mock
    VakcinacijaService vakcinacijaService;

    @Mock
    EntityManager entityManager;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

    @Mock
    ResponseEntity response;

    @InjectMocks
    VakcinaController vakcinaController;

    AddVaccineToPatientRequestDTO getRequest() {
        AddVaccineToPatientRequestDTO request = new AddVaccineToPatientRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setNaziv("SYNFLORIX");
        request.setDatumVakcinacije(new Date(Calendar.getInstance().getTime().getTime()));

        return request;
    }

    AddVaccineToPatientRequestDTO getRequestWithMissingField() {
        AddVaccineToPatientRequestDTO request = new AddVaccineToPatientRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setNaziv("SYNFLORIX");

        return request;
    }


    @Test
    public void whenUnauthorizedRole_ExpectStatusCode403() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddVaccineToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenMissingFieldInRequest_ExpectMissingRequestFieldsException() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddVaccineToPatientRequestDTO request1 = getRequest();
        request1.setNaziv(null);

        Throwable thrown = catchThrowable(() -> {
            ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request1);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);

        AddVaccineToPatientRequestDTO request2 = getRequest();
        request2.setLbp(null);

        thrown = catchThrowable(() -> {
            ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request2);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);

        AddVaccineToPatientRequestDTO request3 = getRequest();
        request3.setDatumVakcinacije(null);

        thrown = catchThrowable(() -> {
            ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request3);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);
    }

    @Test
    public void whenVaccineNotExistInDB_ExpectAllergenNotExistException() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddVaccineToPatientRequestDTO request = getRequest();
        request.setNaziv("Vakcina Test");

        Throwable thrown = catchThrowable(() -> {
            ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);
        });

        assertThat(thrown).isInstanceOf(VaccineNotExistException.class)
                .hasMessageContaining("Vaccine not exist.");
    }

    @Test
    public void whenInvalidPatientLBP_ExpectStatusCode400() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(vakcinaService.findVakcinaByNaziv(any(String.class))).thenReturn(new Vakcina());
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(null);

        AddVaccineToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void whenValidRequest_ExpectStatusCode200() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        Vakcina v = new Vakcina();
        long vakcinaId = 12;

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(vakcinaService.findVakcinaByNaziv(any(String.class))).thenAnswer(i -> {
            v.setNaziv((String)i.getArguments()[0]);
            v.setOpis("opis");
            v.setProizvodjac("proizvodjac");
            v.setTip("tip");
            v.setVakcinaId(vakcinaId);
            return v;
        });
        ZdravstveniKarton zk = new ZdravstveniKarton();
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(zk);
        when(vakcinacijaService.save(any(Vakcinacija.class))).thenAnswer( i -> {
            return i.getArguments()[0];
                }
        );

        AddVaccineToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(Vakcinacija.class);

        Vakcinacija vk = (Vakcinacija) response.getBody();

        assertThat(vk.getVakcina().getVakcinaId()).isEqualTo(v.getVakcinaId());
        assertThat(vk.getVakcina().getNaziv()).isEqualTo(v.getNaziv());
        assertThat(vk.getDatumVakcinacije()).isEqualTo(request.getDatumVakcinacije());
        assertThat(vk.getVakcina().getOpis()).isEqualTo(v.getOpis());
        assertThat(vk.getVakcina().getProizvodjac()).isEqualTo(v.getProizvodjac());
        assertThat(vk.getVakcina().getTip()).isEqualTo(v.getTip());
        assertThat(vk.getZdravstveniKarton()).isEqualTo(zk);
    }

}
