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
import raf.si.bolnica.management.requests.FilterPatientsRequestDTO;
import raf.si.bolnica.management.requests.IstorijaBolestiRequestDTO;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.requests.PreglediRequestDTO;
import raf.si.bolnica.management.response.*;
import raf.si.bolnica.management.services.PacijentService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacijentQueriesTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    EntityManager entityManager;

    @Mock
    PacijentService pacijentService;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

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

        return request;
    }

    Pacijent getPacijent() {
        Pacijent pacijent = new Pacijent();

        pacijent.setLbp(UUID.randomUUID());
        pacijent.setJmbg("123456789");
        pacijent.setIme("Pacijent");
        pacijent.setImeRoditelja("Pacijent");
        pacijent.setPrezime("Pacijent");
        pacijent.setPol(Pol.MUSKI);
        pacijent.setDatumRodjenja(Date.valueOf("1990-01-01"));
        pacijent.setMestoRodjenja("Loznica");
        pacijent.setZemljaDrzavljanstva(CountryCode.SRB);
        pacijent.setZemljaStanovanja(CountryCode.SRB);

        return pacijent;
    }

    List<Pregled> getPregledi() {
        List<Pregled> pregledi = new ArrayList<>();
        Pregled pregled1 = new Pregled();
        pregled1.setDatumPregleda(Date.valueOf(LocalDate.now()));
        pregled1.setObjektivniNalaz("nalaz1");
        pregled1.setIndikatorPoverljivosti(false);
        pregled1.setDijagnoza("dijagnoza");
        pregled1.setLbz(UUID.randomUUID());
        pregled1.setObrisan(false);
        pregled1.setPregledId(12);
        pregled1.setGlavneTegobe("tegobe");
        pregled1.setLicnaAnamneza("anamneza");
        pregled1.setMisljenjePacijenta("misljenje");
        pregled1.setPorodicnaAnamneza("porodica");
        pregled1.setPredlozenaTerapija("terapija");
        pregled1.setSadasnjaBolest("bolest");
        pregled1.setSadasnjaBolest("savet");
        pregled1.setZdravstveniKarton(new ZdravstveniKarton());

        Pregled pregled2 = new Pregled();
        pregled1.setDatumPregleda(Date.valueOf(LocalDate.now()));
        pregled1.setObjektivniNalaz("nalaz2");
        pregled1.setIndikatorPoverljivosti(false);


        pregledi.add(pregled1);
        pregledi.add(pregled2);

        return  pregledi;
    }

    List<IstorijaBolesti> getIstorija() {
        List<IstorijaBolesti> istorija = new ArrayList<>();
        IstorijaBolesti i1 = new IstorijaBolesti();
        i1.setDijagnoza("dijagnoza");
        i1.setIndikatorPoverljivosti(true);
        i1.setDatumPocetkaZdravstvenogProblema(Date.valueOf(LocalDate.now()));
        i1.setRezultatLecenja(RezultatLecenja.OPORAVLJEN);
        i1.setOpisTekucegStanja("stanje");
        i1.setPodatakValidanOd(Date.valueOf(LocalDate.now()));
        i1.setPodatakValidanDo(Date.valueOf(LocalDate.now()));
        i1.setPodaciValidni(true);
        i1.setObrisan(false);
        i1.setBolestPacijentaId(12);

        istorija.add(i1);
        return istorija;
    }

    @Test
    public void testUnauthorizedQueries() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        String s = UUID.randomUUID().toString();

        assertThat(managementController.fetchPatientDataLbp(s).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.fetchPreglediLbp(new PreglediRequestDTO(),s,1,2).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.fetchIstorijaBolestiLbp(new IstorijaBolestiRequestDTO(),s,1,2).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.filterPatients(new FilterPatientsRequestDTO()).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.fetchPatientLbp(s).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.fetchZdravstveniKartonLbp(s).getStatusCodeValue()).isNotEqualTo(200);
    }

    @Test
    public void testPacijentQueriesNoPatient() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        when(loggedInUser.getRoles()).thenReturn(roles);


        String s = UUID.randomUUID().toString();

        assertThat(managementController.fetchPatientDataLbp(s).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.fetchPreglediLbp(new PreglediRequestDTO(),s,1,2).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.fetchIstorijaBolestiLbp(new IstorijaBolestiRequestDTO(),s,1,2).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.fetchPatientLbp(s).getStatusCodeValue()).isNotEqualTo(200);

        assertThat(managementController.fetchZdravstveniKartonLbp(s).getStatusCodeValue()).isNotEqualTo(200);
    }

    @Test
    public void testFetchPatientData() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        Pacijent p = getPacijent();

        ZdravstveniKarton zk = new ZdravstveniKarton();
        zk.setKrvnaGrupa(KrvnaGrupa.A);
        zk.setRhFaktor(RhFaktor.MINUS);

        p.setZdravstveniKarton(zk);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.fetchPacijentByLbp(p.getLbp())).thenReturn(p);

        LinkedList<AlergenZdravstveniKarton> list1= new LinkedList<>();
        AlergenZdravstveniKarton alergenZdravstveniKarton = new AlergenZdravstveniKarton();
        Alergen alergen = new Alergen();
        alergen.setAlergenId(142);
        alergenZdravstveniKarton.setAlergen(alergen);
        list1.add(alergenZdravstveniKarton);
        TypedQuery query1 = mock(TypedQuery.class);
        when(query1.getResultList()).thenReturn(list1);

        LinkedList<Vakcinacija> list2= new LinkedList<>();
        Vakcinacija vakcinacija = new Vakcinacija();
        Vakcina vak  = new Vakcina();
        vak.setVakcinaId(132);
        vakcinacija.setVakcina(vak);
        list2.add(vakcinacija);
        TypedQuery query2 = mock(TypedQuery.class);
        when(query2.getResultList()).thenReturn(list2);

        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            if(i.getArguments()[1].equals(AlergenZdravstveniKarton.class)) return query1;
            else if(i.getArguments()[1].equals(Vakcinacija.class)) return query2;
            else return null;
        });

        ResponseEntity<?> response = managementController.fetchPatientDataLbp(p.getLbp().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(PacijentPodaciResponseDTO.class);

        assertThat(((PacijentPodaciResponseDTO)response.getBody()).getRhFaktor()).isEqualTo(RhFaktor.MINUS);

        assertThat(((PacijentPodaciResponseDTO)response.getBody()).getKrvnaGrupa()).isEqualTo(KrvnaGrupa.A);

        Set<Vakcina> vakcine = ((PacijentPodaciResponseDTO)response.getBody()).getVakcine();

        assertThat(vakcine.size()).isEqualTo(1);
        assertThat(vakcine.contains(vak)).isTrue();

        Set<Alergen> alergeni = ((PacijentPodaciResponseDTO)response.getBody()).getAlergeni();

        assertThat(alergeni.size()).isEqualTo(1);
        assertThat(alergeni.contains(alergen)).isTrue();
    }

    @Test
    public void testFetchPregledi() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        Pacijent p = getPacijent();

        List<Pregled> pregledi = getPregledi();


        ZdravstveniKarton zk = new ZdravstveniKarton();

        p.setZdravstveniKarton(zk);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.fetchPacijentByLbp(p.getLbp())).thenReturn(p);

        TypedQuery<Pregled> query1 = mock(TypedQuery.class);
        when(entityManager.createQuery(eq("SELECT p FROM Pregled p WHERE p.zdravstveniKarton = :zk AND p.indikatorPoverljivosti = false"),
                any(Class.class))).thenReturn(query1);

        when(query1.getResultList()).thenReturn(pregledi);

        ResponseEntity<?> response = managementController.fetchPreglediLbp(new PreglediRequestDTO(),p.getLbp().toString(),1,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        assertThat(((List)response.getBody()).size()).isEqualTo(pregledi.size());

        for(int i=0;i<pregledi.size();i++) {
            Pregled data = pregledi.get(i);
            PregledResponseDTO responseDTO = (PregledResponseDTO) ((List)response.getBody()).get(i);
            assertThat(responseDTO.getDatumPregleda()).isEqualTo(data.getDatumPregleda());
            assertThat(responseDTO.getDijagnoza()).isEqualTo(data.getDijagnoza());
            assertThat(responseDTO.getIndikatorPoverljivosti()).isEqualTo(data.getIndikatorPoverljivosti());
            assertThat(responseDTO.getObrisan()).isEqualTo(data.getObrisan());
            assertThat(responseDTO.getGlavneTegobe()).isEqualTo(data.getGlavneTegobe());
            assertThat(responseDTO.getLicnaAnamneza()).isEqualTo(data.getLicnaAnamneza());
            assertThat(responseDTO.getMisljenjePacijenta()).isEqualTo(data.getMisljenjePacijenta());
            assertThat(responseDTO.getObjektivniNalaz()).isEqualTo(data.getObjektivniNalaz());
            assertThat(responseDTO.getPorodicnaAnamneza()).isEqualTo(data.getPorodicnaAnamneza());
            assertThat(responseDTO.getPredlozenaTerapija()).isEqualTo(data.getPredlozenaTerapija());
            assertThat(responseDTO.getSadasnjaBolest()).isEqualTo(data.getSadasnjaBolest());
            assertThat(responseDTO.getSavet()).isEqualTo(data.getSavet());
        }
    }

    @Test
    public void testFetchPreglediWithParams() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);
        roles.add(Constants.SPECIJLISTA_POV);

        Pacijent p = getPacijent();

        List<Pregled> pregledi = getPregledi();


        ZdravstveniKarton zk = new ZdravstveniKarton();

        p.setZdravstveniKarton(zk);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.fetchPacijentByLbp(p.getLbp())).thenReturn(p);

        TypedQuery<Pregled> query1 = mock(TypedQuery.class);
        when(entityManager.createQuery(eq("SELECT p FROM Pregled p WHERE p.zdravstveniKarton = :zk AND p.datumPregleda = :on AND p.datumPregleda >= :from AND p.datumPregleda <= :to"),
                any(Class.class))).thenReturn(query1);

        when(query1.getResultList()).thenReturn(pregledi);

        PreglediRequestDTO requestDTO = new PreglediRequestDTO();
        requestDTO.setFrom(Date.valueOf(LocalDate.now().minusDays(1)));
        requestDTO.setOn(Date.valueOf(LocalDate.now()));
        requestDTO.setTo(Date.valueOf(LocalDate.now().plusDays(1)));

        ResponseEntity<?> response = managementController.fetchPreglediLbp(requestDTO,p.getLbp().toString(),1,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        assertThat(((List)response.getBody()).size()).isEqualTo(pregledi.size());

    }

    @Test
    public void testFetchIstorijaBolesti() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        roles.add(Constants.SPECIJLISTA_POV);

        Pacijent p = getPacijent();

        List<IstorijaBolesti> istorija = getIstorija();


        ZdravstveniKarton zk = new ZdravstveniKarton();

        p.setZdravstveniKarton(zk);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.fetchPacijentByLbp(p.getLbp())).thenReturn(p);

        TypedQuery<IstorijaBolesti> query1 = mock(TypedQuery.class);
        when(entityManager.createQuery(eq("SELECT i FROM IstorijaBolesti i WHERE i.zdravstveniKarton = :zk"),
                any(Class.class))).thenReturn(query1);

        when(query1.getResultList()).thenReturn(istorija);

        ResponseEntity<?> response = managementController.fetchIstorijaBolestiLbp(new IstorijaBolestiRequestDTO(),p.getLbp().toString(),1,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        assertThat(((List)response.getBody()).size()).isEqualTo(istorija.size());

        for(int i=0;i<istorija.size();i++) {
            IstorijaBolesti data = istorija.get(i);
            IstorijaBolestiResponseDTO responseDTO = (IstorijaBolestiResponseDTO) ((List) response.getBody()).get(i);
            assertThat(responseDTO.getDatumPocetkaZdravstvenogProblema()).isEqualTo(data.getDatumPocetkaZdravstvenogProblema());
            assertThat(responseDTO.getDijagnoza()).isEqualTo(data.getDijagnoza());
            assertThat(responseDTO.getDatumZavrsetkaZdravstvenogProblema()).isEqualTo(data.getDatumZavrsetkaZdravstvenogProblema());
            assertThat(responseDTO.getIndikatorPoverljivosti()).isEqualTo(data.getIndikatorPoverljivosti());
            assertThat(responseDTO.getObrisan()).isEqualTo(data.getObrisan());
            assertThat(responseDTO.getOpisTekucegStanja()).isEqualTo(data.getOpisTekucegStanja());
            assertThat(responseDTO.getPodaciValidni()).isEqualTo(data.getPodaciValidni());
            assertThat(responseDTO.getRezultatLecenja()).isEqualTo(data.getRezultatLecenja());
            assertThat(responseDTO.getPodatakValidanDo()).isEqualTo(data.getPodatakValidanDo());
            assertThat(responseDTO.getPodatakValidanOd()).isEqualTo(data.getPodatakValidanOd());
        }

    }

    @Test
    public void testFetchIstorijaBolestiWithParams() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        Pacijent p = getPacijent();

        List<IstorijaBolesti> istorija = getIstorija();


        ZdravstveniKarton zk = new ZdravstveniKarton();

        p.setZdravstveniKarton(zk);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.fetchPacijentByLbp(p.getLbp())).thenReturn(p);

        TypedQuery<IstorijaBolesti> query1 = mock(TypedQuery.class);
        when(entityManager.createQuery(eq("SELECT i FROM IstorijaBolesti i WHERE i.zdravstveniKarton = :zk AND i.indikatorPoverljivosti = false AND i.dijagnoza like :dijagnoza"),
                any(Class.class))).thenReturn(query1);

        when(query1.getResultList()).thenReturn(istorija);

        IstorijaBolestiRequestDTO requestDTO = new IstorijaBolestiRequestDTO();
        requestDTO.setDijagnoza("dijagnoza");

        ResponseEntity<?> response = managementController.fetchIstorijaBolestiLbp(requestDTO,p.getLbp().toString(),1,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        assertThat(((List)response.getBody()).size()).isEqualTo(istorija.size());

    }

    @Test
    public void testFilterPacijent() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        roles.add(Constants.SPECIJLISTA_POV);

        Pacijent p = getPacijent();

        List<Pacijent> pacijenti = new ArrayList<>();

        pacijenti.add(p);

        when(loggedInUser.getRoles()).thenReturn(roles);

        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(eq("SELECT p FROM Pacijent p WHERE p.ime = :ime AND p.prezime = :prezime"),
                any(Class.class))).thenReturn(query);

        when(query.getResultList()).thenReturn(pacijenti);

        FilterPatientsRequestDTO requestDTO = new FilterPatientsRequestDTO();
        requestDTO.setIme("ime");
        requestDTO.setPrezime("prezime");

        ResponseEntity<?> response = managementController.filterPatients(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        assertThat(((List)response.getBody()).size()).isEqualTo(pacijenti.size());

    }

    @Test
    public void testCreateFetchPatientZdravstveniKarton() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        roles.add(Constants.NACELNIK);

        when(loggedInUser.getRoles()).thenReturn(roles);

        PacijentCRUDRequestDTO request = getRequest();

        when(pacijentService.savePacijent(any(Pacijent.class))).thenAnswer(i -> i.getArguments()[0]);


        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenReturn(new ZdravstveniKarton());

        ResponseEntity<?> responseCreate = managementController.createPatient(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseCreate.getBody()).isInstanceOf(PacijentResponseDTO.class);

        UUID lbp = ((PacijentResponseDTO) Objects.requireNonNull(responseCreate.getBody())).getLbp();


        Pacijent p = new Pacijent();

        ZdravstveniKarton zk = new ZdravstveniKarton();

        p.setZdravstveniKarton(zk);

        p.setLbp(lbp);

        zk.setPacijent(p);

        zk.setKrvnaGrupa(KrvnaGrupa.AB);

        zk.setRhFaktor(RhFaktor.MINUS);

        zk.setObrisan(true);

        zk.setDatumRegistracije(Date.valueOf(LocalDate.now()));

        when(pacijentService.fetchPacijentByLbp(lbp)).thenReturn(p);

        ResponseEntity<?> responseFetchPatient = managementController.fetchPatientLbp(lbp.toString());

        assertThat(responseFetchPatient.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseFetchPatient.getBody()).isInstanceOf(PacijentResponseDTO.class);

        assertThat(((PacijentResponseDTO)responseFetchPatient.getBody()).getLbp()).isEqualTo(lbp);

        ResponseEntity<?> responseFetchZdravstveniKarton = managementController.fetchZdravstveniKartonLbp(lbp.toString());

        assertThat(responseFetchZdravstveniKarton.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseFetchZdravstveniKarton.getBody()).isInstanceOf(ZdravstveniKartonResponseDTO.class);

        ZdravstveniKartonResponseDTO responseDTO = (ZdravstveniKartonResponseDTO) responseFetchZdravstveniKarton.getBody();

        assertThat(responseDTO).isNotNull();


        assertThat(responseDTO.getKrvnaGrupa()).isEqualTo(zk.getKrvnaGrupa());
        assertThat(responseDTO.getRhFaktor()).isEqualTo(zk.getRhFaktor());
        assertThat(responseDTO.getObrisan()).isEqualTo(zk.getObrisan());
        assertThat(responseDTO.getDatumRegistracije()).isEqualTo(zk.getDatumRegistracije());
        assertThat(responseDTO.getPacijent().getLbp()).isEqualTo(p.getLbp());
    }

}
