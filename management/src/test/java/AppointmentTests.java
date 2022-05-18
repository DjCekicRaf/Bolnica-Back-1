import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.PrispecePacijenta;
import raf.si.bolnica.management.entities.enums.StatusPregleda;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.SearchForAppointmentDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;
import raf.si.bolnica.management.services.ScheduledAppointmentService;
import raf.si.bolnica.management.services.PacijentService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentTests {
    @Mock
    LoggedInUser loggedInUser;

    @Mock
    ScheduledAppointmentService appointmentService;

    @Mock
    PacijentService pacijentService;

    @InjectMocks
    ManagementController managementController;

    @Test
    public void testSetAppointmentTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UUID uuid = UUID.randomUUID();
        when(loggedInUser.getLBZ()).thenReturn(uuid);
        when(appointmentService.saveAppointment(any(ZakazaniPregled.class))).thenAnswer(i -> i.getArguments()[0]);
        CreateScheduledAppointmentRequestDTO requestDTO = new CreateScheduledAppointmentRequestDTO();
        requestDTO.setDateAndTimeOfAppointment(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setLbz(UUID.randomUUID().toString());
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setNote("Napomena");
        ResponseEntity<?> response = managementController.setAppointment(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(ZakazaniPregled.class);

        ZakazaniPregled pregled = (ZakazaniPregled) response.getBody();
        assertThat(pregled).isNotNull();
        assertThat(pregled.getDatumIVremePregleda()).isEqualTo(requestDTO.getDateAndTimeOfAppointment());
        assertThat(pregled.getLbzLekara().toString()).isEqualTo(requestDTO.getLbz());
        assertThat(pregled.getLbzSestre()).isEqualTo(loggedInUser.getLBZ());
        assertThat(pregled.getNapomena()).isEqualTo(requestDTO.getNote());
    }

    @Test
    public void testSetAppointmentUnauthorizedTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        CreateScheduledAppointmentRequestDTO requestDTO = new CreateScheduledAppointmentRequestDTO();
        requestDTO.setDateAndTimeOfAppointment(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setLbz(UUID.randomUUID().toString());
        requestDTO.setLbp(UUID.randomUUID().toString());
        ResponseEntity<?> response = managementController.setAppointment(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testSetAppointmentInvalidRequestTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        CreateScheduledAppointmentRequestDTO requestDTO = new CreateScheduledAppointmentRequestDTO();
        requestDTO.setDateAndTimeOfAppointment(null);
        requestDTO.setLbz(UUID.randomUUID().toString());
        requestDTO.setLbp(UUID.randomUUID().toString());
        ResponseEntity<?> response = managementController.setAppointment(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);

        requestDTO.setDateAndTimeOfAppointment(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setLbz(null);

        response = managementController.setAppointment(requestDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testUpdateAppointmentTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateAppointmentStatusDTO requestDTO = new UpdateAppointmentStatusDTO();
        requestDTO.setAppointmentStatus(StatusPregleda.U_TOKU.toString());
        when(appointmentService.fetchById(any(Long.class))).thenReturn(new ZakazaniPregled());
        when(appointmentService.saveAppointment(any(ZakazaniPregled.class))).thenAnswer(i -> i.getArguments()[0]);
        ResponseEntity<?> response = managementController.updateAppointmentStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(ZakazaniPregled.class);

        ZakazaniPregled pregled = (ZakazaniPregled) response.getBody();

        assertThat(pregled).isNotNull();
        assertThat(pregled.getStatusPregleda().toString()).isEqualTo(requestDTO.getAppointmentStatus());
    }

    @Test
    public void testUpdateAppointmentUnauthorizedTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateAppointmentStatusDTO requestDTO = new UpdateAppointmentStatusDTO();
        requestDTO.setAppointmentStatus(StatusPregleda.U_TOKU.toString());
        ResponseEntity<?> response = managementController.updateAppointmentStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testUpdateAppointmentInvalidTest() {
        UpdateAppointmentStatusDTO requestDTO = new UpdateAppointmentStatusDTO();
        requestDTO.setAppointmentStatus(null);
        ResponseEntity<?> response = managementController.updateAppointmentStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testUpdateArrivalTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateArrivalStatusDTO requestDTO = new UpdateArrivalStatusDTO();
        requestDTO.setArrivalStatus(PrispecePacijenta.PRIMLJEN.toString());
        when(appointmentService.fetchById(any(Long.class))).thenReturn(new ZakazaniPregled());
        when(appointmentService.saveAppointment(any(ZakazaniPregled.class))).thenAnswer(i -> i.getArguments()[0]);
        ResponseEntity<?> response = managementController.updateArrivalStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(ZakazaniPregled.class);

        ZakazaniPregled pregled = (ZakazaniPregled) response.getBody();

        assertThat(pregled).isNotNull();
        assertThat(pregled.getPrispecePacijenta().toString()).isEqualTo(requestDTO.getArrivalStatus());
    }

    @Test
    public void testUpdateArrivalUnauthorizedTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateArrivalStatusDTO requestDTO = new UpdateArrivalStatusDTO();
        requestDTO.setArrivalStatus(PrispecePacijenta.PRIMLJEN.toString());
        ResponseEntity<?> response = managementController.updateArrivalStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testUpdateArrivalInvalidTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.MED_SESTRA);
        UpdateArrivalStatusDTO requestDTO = new UpdateArrivalStatusDTO();
        requestDTO.setArrivalStatus(null);
        ResponseEntity<?> response = managementController.updateArrivalStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testListAppointmentLBZ() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        SearchForAppointmentDTO requestDTO = new SearchForAppointmentDTO();
        requestDTO.setLbz(UUID.randomUUID().toString());
        List<ZakazaniPregled> appointments = new LinkedList<>();
        ZakazaniPregled zp1 = new ZakazaniPregled();
        zp1.setZakazaniPregledId(1);
        zp1.setDatumIVremePregleda(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
        ZakazaniPregled zp2 = new ZakazaniPregled();
        zp2.setZakazaniPregledId(2);
        zp2.setDatumIVremePregleda(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
        appointments.add(zp1);
        appointments.add(zp2);
        when(appointmentService.getAppointmentByLBZ(any(UUID.class))).thenReturn(appointments);
        ResponseEntity<?> response = managementController.listAppointmentsByLBZ(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        List<ZakazaniPregled> l = (List)response.getBody();

        assertThat(l.size()).isEqualTo(1);
        assertThat(l.get(0).getZakazaniPregledId()).isEqualTo(zp2.getZakazaniPregledId());
    }

    @Test
    public void testListAppointmentLBZFixedDate() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        SearchForAppointmentDTO requestDTO = new SearchForAppointmentDTO();
        requestDTO.setLbz(UUID.randomUUID().toString());
        requestDTO.setDate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
        List<ZakazaniPregled> appointments = new LinkedList<>();
        ZakazaniPregled zp1 = new ZakazaniPregled();
        zp1.setZakazaniPregledId(1);
        zp1.setDatumIVremePregleda(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
        appointments.add(zp1);
        when(appointmentService.getAppointmentByLBZAndDate(any(UUID.class),any(Timestamp.class))).thenReturn(appointments);
        ResponseEntity<?> response = managementController.listAppointmentsByLBZ(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        List<ZakazaniPregled> l = (List)response.getBody();

        assertThat(l.size()).isEqualTo(1);
        assertThat(l.get(0).getZakazaniPregledId()).isEqualTo(zp1.getZakazaniPregledId());
    }

    @Test
    public void testListAppointmentLBZUnauthorized() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.NACELNIK_ODELJENJA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        SearchForAppointmentDTO requestDTO = new SearchForAppointmentDTO();
        ResponseEntity<?> response = managementController.listAppointmentsByLBZ(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }
}
