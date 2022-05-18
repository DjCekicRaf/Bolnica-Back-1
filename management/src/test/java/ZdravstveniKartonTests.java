import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ZdravstveniKartonController;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.KrvnaGrupa;
import raf.si.bolnica.management.entities.enums.RhFaktor;
import raf.si.bolnica.management.exceptionHandler.medicalRecord.AllergenRecordExceptionHandler;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.UpdateMedicalRecordBloodTypeRhFactorRequestDTO;
import raf.si.bolnica.management.services.AlergenZdravstveniKartonService;
import raf.si.bolnica.management.services.vakcina.VakcinaService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ZdravstveniKartonTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    VakcinaService vakcinaService;

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
    ZdravstveniKartonController zdravstveniKartonController;

    UpdateMedicalRecordBloodTypeRhFactorRequestDTO getRequest() {
        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = new UpdateMedicalRecordBloodTypeRhFactorRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setKrvnaGrupa(KrvnaGrupa.A);
        request.setRhFaktor(RhFaktor.MINUS);

        return request;
    }

    UpdateMedicalRecordBloodTypeRhFactorRequestDTO getRequestWithMissingField() {
        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = new UpdateMedicalRecordBloodTypeRhFactorRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setKrvnaGrupa(KrvnaGrupa.A);

        return request;
    }


    @Test
    public void whenUnauthorizedRole_ExpectStatusCode403() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = getRequest();

        ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenMissingFieldInRequest_ExpectMissingRequestFieldsException() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request1 = getRequest();
        request1.setLbp(null);

        Throwable thrown = catchThrowable(() -> {
            ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request1);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request2 = getRequest();
        request2.setKrvnaGrupa(null);

        thrown = catchThrowable(() -> {
            ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request2);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request3 = getRequest();
        request3.setRhFaktor(null);

        thrown = catchThrowable(() -> {
            ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request3);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);
    }

    @Test
    public void whenInvalidPatientLBP_ExpectStatusCode400() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(null);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = getRequest();

        ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void whenValidRequest_ExpectStatusCode200() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(new ZdravstveniKarton());
        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenReturn(new ZdravstveniKarton());

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = getRequest();

        ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

}
