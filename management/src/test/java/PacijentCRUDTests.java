import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.entities.enums.*;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.response.PacijentResponseDTO;
import raf.si.bolnica.management.services.*;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PacijentCRUDTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    PacijentService pacijentService;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

    @Mock
    AlergenZdravstveniKartonService alergenZdravstveniKartonService;

    @Mock
    VakcinacijaService vakcinacijaService;

    @Mock
    OperacijaService operacijaService;

    @Mock
    PregledService pregledService;

    @Mock
    IstorijaBolestiService istorijaBolestiService;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    ManagementController managementController;



    PacijentCRUDRequestDTO getRequest() {
        PacijentCRUDRequestDTO request = new PacijentCRUDRequestDTO();

        request.setJmbg("123456789");
        request.setIme("Pacijent");
        request.setImeRoditelja("Pacijent");
        request.setPrezime("Pacijent");
        request.setPol(Pol.MUSKI);
        request.setDatumRodjenja(Date.valueOf("1990-01-01"));
        request.setMestoRodjenja("Loznica");
        request.setZemljaDrzavljanstva(CountryCode.SRB);
        request.setZemljaStanovanja(CountryCode.SRB);
        request.setStepenStrucneSpreme(StrucnaSprema.VISE);
        request.setBracniStatus(BracniStatus.SAMAC);
        request.setPorodicniStatus(PorodicniStatus.JEDAN_RODITELJ);

        return request;
    }


    @Test
    public void testPacijentCreateInvalidRequest() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);


        PacijentCRUDRequestDTO request = getRequest();
        request.setPol(null);

        ResponseEntity<?> response = managementController.createPatient(request);

        assertThat(response.getStatusCodeValue()).isNotEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("Pol je obavezno polje!");
    }

    @Test
    public void testPacijentUpdateInvalidRequest() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);


        PacijentCRUDRequestDTO request = getRequest();
        request.setPol(null);

        ResponseEntity<?> response = managementController.updatePatient(request,"1");

        assertThat(response.getStatusCodeValue()).isEqualTo(403);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("Pol je obavezno polje!");
    }

    @Test
    public void testPacijentUpdateRemoveNoPatient() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);


        PacijentCRUDRequestDTO request = getRequest();

        ResponseEntity<?> response = managementController.removePatient(UUID.randomUUID().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(403);

        response = managementController.updatePatient(request,UUID.randomUUID().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testPacijentUnauthorizedRequest() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJALISTA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        PacijentCRUDRequestDTO request = getRequest();

        assertThat(managementController.createPatient(request).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.removePatient(UUID.randomUUID().toString()).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.updatePatient(request, UUID.randomUUID().toString()).getStatusCodeValue()).isNotEqualTo(200);

    }

    @Test
    public void testPacijentCreateRemoveSuccess() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.savePacijent(any(Pacijent.class))).thenAnswer(i -> i.getArguments()[0]);

        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenAnswer(i -> i.getArguments()[0]);

        PacijentCRUDRequestDTO request = getRequest();

        ResponseEntity<?> responseCreate = managementController.createPatient(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseCreate.getBody()).isInstanceOf(PacijentResponseDTO.class);

        PacijentResponseDTO response = (PacijentResponseDTO) responseCreate.getBody();

        assertThat(response).isNotNull();

        assertThat(response.getBracniStatus()).isEqualTo(request.getBracniStatus());

        assertThat(response.getPorodicniStatus()).isEqualTo(request.getPorodicniStatus());

        assertThat(response.getStepenStrucneSpreme()).isEqualTo(request.getStepenStrucneSpreme());

        Pacijent test = new Pacijent();
        test.setZdravstveniKarton(new ZdravstveniKarton());

        when(pacijentService.fetchPacijentByLbp(response.getLbp())).thenReturn(test);

        LinkedList<AlergenZdravstveniKarton> list1= new LinkedList<>();
        list1.add(new AlergenZdravstveniKarton());
        TypedQuery query1 = mock(TypedQuery.class);
        when(query1.getResultList()).thenReturn(list1);

        LinkedList<Vakcinacija> list2= new LinkedList<>();
        list2.add(new Vakcinacija());
        TypedQuery query2 = mock(TypedQuery.class);
        when(query2.getResultList()).thenReturn(list2);

        LinkedList<Operacija> list3= new LinkedList<>();
        Operacija operacija = new Operacija();
        operacija.setOpis("opis");
        operacija.setOperacijaId(123);
        operacija.setDatumOperacije(Date.valueOf(LocalDate.now().minusDays(32)));
        operacija.setObrisan(false);
        operacija.setOdeljenjeId(345);
        ZdravstveniKarton zk = new ZdravstveniKarton();
        operacija.setZdravstveniKarton(zk);
        list3.add(operacija);
        TypedQuery query3 = mock(TypedQuery.class);
        when(query3.getResultList()).thenReturn(list3);

        LinkedList<Pregled> list4= new LinkedList<>();
        list4.add(new Pregled());
        TypedQuery query4 = mock(TypedQuery.class);
        when(query4.getResultList()).thenReturn(list4);

        LinkedList<IstorijaBolesti> list5= new LinkedList<>();
        list5.add(new IstorijaBolesti());
        TypedQuery query5 = mock(TypedQuery.class);
        when(query5.getResultList()).thenReturn(list5);


        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            if(i.getArguments()[1].equals(AlergenZdravstveniKarton.class)) return query1;
            else if(i.getArguments()[1].equals(Vakcinacija.class)) return query2;
            else if(i.getArguments()[1].equals(Operacija.class)) return query3;
            else if(i.getArguments()[1].equals(Pregled.class)) return query4;
            else if(i.getArguments()[1].equals(IstorijaBolesti.class)) return query5;
            else return null;
        });


        ResponseEntity<?> responseRemove = managementController.removePatient(response.getLbp().toString());

        assertThat(responseRemove.getStatusCodeValue()).isEqualTo(200);

        for(AlergenZdravstveniKarton azk:list1) {
            assertThat(azk.getObrisan()).isEqualTo(true);
        }

        for(Vakcinacija vak:list2) {
            assertThat(vak.getObrisan()).isEqualTo(true);
        }

        for(Operacija op:list3) {
            assertThat(op.getObrisan()).isEqualTo(true);
        }

        assertThat(list3.size()).isEqualTo(1);

        Operacija op1 = list3.get(0);

        assertThat(op1.getDatumOperacije()).isEqualTo(Date.valueOf(LocalDate.now().minusDays(32)));
        assertThat(op1.getOdeljenjeId()).isEqualTo(345);
        assertThat(op1.getZdravstveniKarton()).isEqualTo(zk);
        assertThat(op1.getOpis()).isEqualTo("opis");
        assertThat(op1.getOperacijaId()).isEqualTo(123);

        for(Pregled p:list4) {
            assertThat(p.getObrisan()).isEqualTo(true);
        }

        for(IstorijaBolesti ib:list5) {
            assertThat(ib.getObrisan()).isEqualTo(true);
        }
    }

    @Test
    public void testPacijentCreateUpdateSuccess() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.savePacijent(any(Pacijent.class))).thenAnswer(i -> i.getArguments()[0]);

        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenAnswer(i -> i.getArguments()[0]);

        PacijentCRUDRequestDTO request = getRequest();

        ResponseEntity<?> responseCreate = managementController.createPatient(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseCreate.getBody()).isInstanceOf(PacijentResponseDTO.class);

        PacijentResponseDTO response = (PacijentResponseDTO) Objects.requireNonNull(responseCreate.getBody());

        when(pacijentService.fetchPacijentByLbp((response.getLbp()))).thenReturn(new Pacijent());

        request.setBrojDece(5);

        ResponseEntity<?> responseUpdate = managementController.updatePatient(request, response.getLbp().toString());

        assertThat(responseUpdate.getStatusCodeValue()).isEqualTo(200);
    }
}
