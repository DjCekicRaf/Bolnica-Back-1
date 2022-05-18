import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.CountryCode;
import raf.si.bolnica.management.entities.enums.Pol;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreatePregledReportRequestDTO;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.response.PacijentResponseDTO;
import raf.si.bolnica.management.services.IstorijaBolestiService;
import raf.si.bolnica.management.services.PacijentService;
import raf.si.bolnica.management.services.PregledService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PregledReportTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    PregledService pregledService;

    @Mock
    PacijentService pacijentService;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

    @Mock
    IstorijaBolestiService istorijaBolestiService;

    @InjectMocks
    ManagementController managementController;


    CreatePregledReportRequestDTO getRequest() {
        CreatePregledReportRequestDTO request = new CreatePregledReportRequestDTO();

        request.setLbz(UUID.randomUUID().toString());
        request.setLbp(UUID.randomUUID().toString());
        request.setDijagnoza("dijagnoza");
        request.setGlavneTegobe("glavnetegobe");
        request.setIndikatorPoverljivosti(false);
        request.setLicnaAnamneza("licnaanamneza");
        request.setObjektivniNalaz("objektivninalaz");
        request.setMisljenjePacijenta("misljenjepacijenta");
        request.setRezultatLecenja(RezultatLecenja.OPORAVLJEN);
        request.setOpisTekucegStanja("opistekucegstanja");
        request.setPorodicnaAnamneza("porodicnaanamneza");
        request.setSadasnjaBolest("sadasnjabolest");
        request.setSavet("savet");
        request.setPredlozenaTerapija("predlozenaterapija");

        return request;
    }

    @Test
    public void testCreatePregledReportInvalidRequest() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();
        request.setLbp(null);

        ResponseEntity<?> response = managementController.createPregledReport(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(406);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("LBP je obavezno polje!");
    }

    @Test
    public void testCreatePregledReportSuccess() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();
        request.setDijagnoza(null);

        ResponseEntity<?> responseCreate = managementController.createPregledReport(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void testCreatePregledReportTreatmentOngoing() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();
        request.setRezultatLecenja(RezultatLecenja.U_TOKU);

        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(new ZdravstveniKarton());

        when(istorijaBolestiService.fetchByZdravstveniKartonPodaciValidni(any(ZdravstveniKarton.class),eq(true))).thenReturn(new IstorijaBolesti());

        ResponseEntity<?> responseCreate = managementController.createPregledReport(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void testCreatePregledReportTreatmentNoResult() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();
        request.setRezultatLecenja(null);

        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(new ZdravstveniKarton());

        when(istorijaBolestiService.fetchByZdravstveniKartonPodaciValidni(any(ZdravstveniKarton.class),eq(true))).thenReturn(new IstorijaBolesti());

        ResponseEntity<?> responseCreate = managementController.createPregledReport(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void testCreatePregledReportUnauthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();

        ResponseEntity<?> response = managementController.createPregledReport(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testCreatePregledReportAuthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();

        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(new ZdravstveniKarton());

        when(istorijaBolestiService.fetchByZdravstveniKartonPodaciValidni(any(ZdravstveniKarton.class), eq(true))).thenReturn(new IstorijaBolesti());

        ResponseEntity<?> response = managementController.createPregledReport(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testCreatePregledReportNonConfidentialUnauthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJALISTA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();

        request.setIndikatorPoverljivosti(true);

        ResponseEntity<?> response = managementController.createPregledReport(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testCreatePregledReportNonConfidentialAuthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJALISTA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();

        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(new ZdravstveniKarton());

        when(istorijaBolestiService.fetchByZdravstveniKartonPodaciValidni(any(ZdravstveniKarton.class),eq(true))).thenReturn(new IstorijaBolesti());

        ResponseEntity<?> response = managementController.createPregledReport(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testCreatePregledReportNoDiseaseAuthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();

        request.setSadasnjaBolest(null);

        ResponseEntity<?> response = managementController.createPregledReport(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
