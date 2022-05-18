import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.AlergentController;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.exceptionHandler.medicalRecord.AllergenRecordExceptionHandler;
import raf.si.bolnica.management.exceptions.AllergenNotExistException;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.AddAllergentToPatientRequestDTO;
import raf.si.bolnica.management.services.AlergenZdravstveniKartonService;
import raf.si.bolnica.management.services.alergen.AlergenService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AllergenTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    AlergenService alergenService;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

    @Mock
    AlergenZdravstveniKartonService alergenZdravstveniKartonService;

    @Mock
    AllergenRecordExceptionHandler allergenRecordExceptionHandler;

    @Mock
    EntityManager entityManager;

    @Mock
    ResponseEntity response;

    @InjectMocks
    AlergentController alergentController;

    AddAllergentToPatientRequestDTO getRequest() {
        AddAllergentToPatientRequestDTO request = new AddAllergentToPatientRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setNaziv("Mleko");

        return request;
    }

    AddAllergentToPatientRequestDTO getRequestWithMissingField() {
        AddAllergentToPatientRequestDTO request = new AddAllergentToPatientRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");

        return request;
    }


    @Test
    public void whenUnauthorizedRole_ExpectStatusCode403() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddAllergentToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = alergentController.addAllergenToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenMissingFieldInRequest_ExpectMissingRequestFieldsException() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddAllergentToPatientRequestDTO request1 = getRequest();
        request1.setLbp(null);

        Throwable thrown = catchThrowable(() -> {
            ResponseEntity<?> response = alergentController.addAllergenToPatient(request1);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);

        AddAllergentToPatientRequestDTO request2 = getRequest();
        request2.setNaziv(null);

        thrown = catchThrowable(() -> {
            ResponseEntity<?> response = alergentController.addAllergenToPatient(request2);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);
    }

    @Test
    public void whenAllergenNotExistInDB_ExpectAllergenNotExistException() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddAllergentToPatientRequestDTO request = getRequestWithMissingField();
        request.setNaziv("Kikiriki");

        Throwable thrown = catchThrowable(() -> {
            ResponseEntity<?> response = alergentController.addAllergenToPatient(request);
        });

        assertThat(thrown).isInstanceOf(AllergenNotExistException.class)
                .hasMessageContaining("Allergen not exist");
    }

    @Test
    public void whenInvalidPatientLBP_ExpectStatusCode400() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(alergenService.findAlergenByNaziv(any(String.class))).thenReturn(new Alergen());
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(null);

        AddAllergentToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = alergentController.addAllergenToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void whenValidRequest_ExpectStatusCode200() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        int alergenId = 12;
        int alergenZdravstveniKartonId = 15;

        when(alergenService.findAlergenByNaziv(any(String.class))).thenAnswer(i -> {
            Alergen a = new Alergen((String)i.getArguments()[0]);
            a.setAlergenId(alergenId);
            return a;
        });

        ZdravstveniKarton zk = new ZdravstveniKarton();

        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(zk);
        when(alergenZdravstveniKartonService.save(any(AlergenZdravstveniKarton.class))).thenAnswer(i -> {
            AlergenZdravstveniKarton azk = (AlergenZdravstveniKarton) i.getArguments()[0];
            azk.setId(Long.valueOf(alergenZdravstveniKartonId));
            return azk;
        });

        AddAllergentToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = alergentController.addAllergenToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(AlergenZdravstveniKarton.class);

        AlergenZdravstveniKarton alergenZdravstveniKarton = (AlergenZdravstveniKarton)response.getBody();

        assertThat(alergenZdravstveniKarton.getAlergen().getNaziv()).isEqualTo(request.getNaziv());
        assertThat(alergenZdravstveniKarton.getAlergen().getAlergenId()).isEqualTo(alergenId);
        assertThat(alergenZdravstveniKarton.getZdravstveniKarton()).isEqualTo(zk);
        assertThat(alergenZdravstveniKarton.getId()).isEqualTo(alergenZdravstveniKartonId);
    }


}
