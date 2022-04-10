package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.*;
import raf.si.bolnica.management.response.*;
import raf.si.bolnica.management.services.*;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.SearchForAppointmentDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;
import raf.si.bolnica.management.service.ScheduledAppointmentService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ManagementController {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private PacijentService pacijentService;

    @Autowired
    private ZdravstveniKartonService zdravstveniKartonService;

    @Autowired
    private AlergenZdravstveniKartonService alergenZdravstveniKartonService;

    @Autowired
    private VakcinacijaService vakcinacijaService;

    @Autowired
    private OperacijaService operacijaService;

    @Autowired
    private PregledService pregledService;

    @Autowired
    private IstorijaBolestiService istorijaBolestiService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ScheduledAppointmentService appointmentService;

    @PostMapping(value = "/create-pregled-report")
    public ResponseEntity<?> createPregledReport(@RequestBody CreatePregledReportRequestDTO requestDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.ADMIN);
        acceptedRoles.add(Constants.NACELNIK);
        acceptedRoles.add(Constants.SPECIJALISTA);
        acceptedRoles.add(Constants.SPECIJLISTA_POV);
        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {

            String msg = PregledReportRequestValidator.validate(requestDTO);

            if (!msg.equals("OK")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(msg);
            }

            if (!loggedInUser.getRoles().contains(Constants.SPECIJLISTA_POV)
                    && requestDTO.getIndikatorPoverljivosti()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nemate privilegije za postavljanje poverljivosti!");
            }

            Pregled pregledToSave = pregledService.createPregledReport(requestDTO);
            Pregled pregledToReturn = this.pregledService.savePregled(pregledToSave);
            return ok(pregledToReturn);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/create-patient")
    public ResponseEntity<?> createPatient(@RequestBody PacijentCRUDRequestDTO request) {
        if (loggedInUser.getRoles().contains(Constants.ADMIN) || (loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA) ||
                loggedInUser.getRoles().contains(Constants.MED_SESTRA))) {

            String msg = PacijentCRUDRequestValidator.checkValid(request);

            if (!msg.equals("ok")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
            }

            Pacijent pacijent = new Pacijent();

            pacijent.setLbp(UUID.randomUUID());

            request.updatePacijentWithData(pacijent);

            Pacijent kreiranPacijent = pacijentService.savePacijent(pacijent);

            ZdravstveniKarton zdravstveniKarton = new ZdravstveniKarton();

            zdravstveniKarton.setDatumRegistracije(Date.valueOf(LocalDate.now()));

            zdravstveniKarton.setPacijent(kreiranPacijent);

            ZdravstveniKarton kreiranZdravstveniKarton = zdravstveniKartonService.saveZdravstveniKarton(zdravstveniKarton);

            kreiranPacijent.setZdravstveniKarton(kreiranZdravstveniKarton);

            return ok(new PacijentResponseDTO(kreiranPacijent));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/update-patient/{id}")
    public ResponseEntity<?> updatePatient(@RequestBody PacijentCRUDRequestDTO request, @PathVariable Long id) {
        if (loggedInUser.getRoles().contains(Constants.ADMIN) || loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA) ||
                loggedInUser.getRoles().contains(Constants.MED_SESTRA)) {

            String msg = PacijentCRUDRequestValidator.checkValid(request);

            if (!msg.equals("ok")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
            }

            Pacijent pacijent = pacijentService.fetchPacijentById(id);

            if (pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            request.updatePacijentWithData(pacijent);

            Pacijent azuriranPacijent = pacijentService.savePacijent(pacijent);

            return ok(new PacijentResponseDTO(azuriranPacijent));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/remove-patient/{id}")
    public ResponseEntity<?> removePatient(@PathVariable Long id) {
        if (loggedInUser.getRoles().contains(Constants.ADMIN) || loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA)) {

            Pacijent pacijent = pacijentService.fetchPacijentById(id);

            if (pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            pacijent.setObrisan(true);

            pacijentService.savePacijent(pacijent);

            ZdravstveniKarton zdravstveniKarton = pacijent.getZdravstveniKarton();

            zdravstveniKarton.setObrisan(true);

            zdravstveniKartonService.saveZdravstveniKarton(zdravstveniKarton);

            String s = "SELECT az FROM AlergenZdravstveniKarton az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<AlergenZdravstveniKarton> query1 = entityManager.createQuery(s, AlergenZdravstveniKarton.class);

            query1.setParameter("zk", zdravstveniKarton);

            for (AlergenZdravstveniKarton azk : query1.getResultList()) {
                azk.setObrisan(true);
                alergenZdravstveniKartonService.save(azk);
            }

            s = "SELECT az FROM Vakcinacija az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Vakcinacija> query2 = entityManager.createQuery(s, Vakcinacija.class);

            query2.setParameter("zk", zdravstveniKarton);

            for (Vakcinacija v : query2.getResultList()) {
                v.setObrisan(true);
                vakcinacijaService.save(v);
            }

            s = "SELECT az FROM Operacija az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Operacija> query3 = entityManager.createQuery(s, Operacija.class);

            query3.setParameter("zk", zdravstveniKarton);

            for (Operacija o : query3.getResultList()) {
                o.setObrisan(true);
                operacijaService.saveOperacija(o);
            }

            s = "SELECT az FROM Pregled az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Pregled> query4 = entityManager.createQuery(s, Pregled.class);

            query4.setParameter("zk", zdravstveniKarton);

            for (Pregled p : query4.getResultList()) {
                p.setObrisan(true);
                pregledService.savePregled(p);
            }

            s = "SELECT az FROM IstorijaBolesti az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<IstorijaBolesti> query5 = entityManager.createQuery(s, IstorijaBolesti.class);

            query5.setParameter("zk", zdravstveniKarton);

            for (IstorijaBolesti i : query5.getResultList()) {
                i.setObrisan(true);
                istorijaBolestiService.saveIstorijaBolesti(i);
            }

            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-patient/{lbp}")
    public ResponseEntity<?> fetchPatientLbp(@PathVariable String lbp) {
        if (loggedInUser.getRoles().contains(Constants.ADMIN) || loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA) ||
                loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA) ||
                loggedInUser.getRoles().contains(Constants.MED_SESTRA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(UUID.fromString(lbp));

            if (pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ok(new PacijentResponseDTO(pacijent));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-zdravstveni-karton/{lbp}")
    public ResponseEntity<?> fetchZdravstveniKartonLbp(@PathVariable String lbp) {
        if (loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(UUID.fromString(lbp));

            if (pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ok(new ZdravstveniKartonResponseDTO(pacijent.getZdravstveniKarton()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-patient-data/{lbp}")
    public ResponseEntity<?> fetchPatientDataLbp(@PathVariable String lbp) {
        if (loggedInUser.getRoles().contains(Constants.ADMIN) || loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(UUID.fromString(lbp));

            if (pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ZdravstveniKarton zk = pacijent.getZdravstveniKarton();


            String vakcineUpitString = "SELECT az FROM Vakcinacija az WHERE az.zdravstveniKarton = :zk";

            Set<Vakcina> vakcine = new HashSet<>();

            TypedQuery<Vakcinacija> upitVakcine = entityManager.createQuery(vakcineUpitString, Vakcinacija.class);

            upitVakcine.setParameter("zk", zk);

            for (Vakcinacija v : upitVakcine.getResultList()) {
                vakcine.add(v.getVakcina());
            }


            String alergijeUpitString = "SELECT az FROM AlergenZdravstveniKarton az WHERE az.zdravstveniKarton = :zk";

            Set<Alergen> alergeni = new HashSet<>();

            TypedQuery<AlergenZdravstveniKarton> upitAlergeni = entityManager.createQuery(alergijeUpitString, AlergenZdravstveniKarton.class);

            upitAlergeni.setParameter("zk", zk);

            for (AlergenZdravstveniKarton az : upitAlergeni.getResultList()) {
                alergeni.add(az.getAlergen());
            }


            PacijentPodaciResponseDTO pacijentPodaciResponseDTO = new PacijentPodaciResponseDTO();

            pacijentPodaciResponseDTO.setRhFaktor(zk.getRhFaktor());

            pacijentPodaciResponseDTO.setKrvnaGrupa(zk.getKrvnaGrupa());

            pacijentPodaciResponseDTO.setAlergeni(alergeni);

            pacijentPodaciResponseDTO.setVakcine(vakcine);


            return ok(pacijentPodaciResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(value = "/set-appointment")
    public ResponseEntity<?> setAppointment(@RequestBody CreateScheduledAppointmentRequestDTO requestDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add("ROLE_ADMIN");
        acceptedRoles.add("ROLE_VISA_MED_SESTRA");
        acceptedRoles.add("ROLE_MED_SESTRA");
        if (requestDTO.getDateAndTimeOfAppointment() == null || requestDTO.getExaminationEmployeeId() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            Pacijent pacijent = pacijentService.fetchPacijentByLbp(requestDTO.getPatient());
            ZakazaniPregled appointmentToReturn = appointmentService.setAppointment(loggedInUser.getLBZ(), pacijent, requestDTO);
            return ResponseEntity.ok(appointmentToReturn);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-pregledi/{lbp}")
    public ResponseEntity<?> fetchPreglediLbp(@RequestBody PreglediRequestDTO preglediRequestDTO,
                                              @PathVariable String lbp,
                                              @RequestParam int page,
                                              @RequestParam int size) {
        if (loggedInUser.getRoles().contains(Constants.ADMIN) || loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(UUID.fromString(lbp));

            if (pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ZdravstveniKarton zk = pacijent.getZdravstveniKarton();


            String preglediUpitString = "SELECT i FROM Pregled p WHERE p.zdravstveniKarton = :zk";

            if (!loggedInUser.getRoles().contains(Constants.SPECIJLISTA_POV)) {
                preglediUpitString = preglediUpitString + " AND p.indikatorPoverljivosti = false";
            }

            if (preglediRequestDTO.getOn() != null) {
                preglediUpitString = preglediUpitString + " AND p.datumPregleda = :on";
            }

            if (preglediRequestDTO.getFrom() != null) {
                preglediUpitString = preglediUpitString + " AND p.datumPregleda >= :from";
            }

            if (preglediRequestDTO.getTo() != null) {
                preglediUpitString = preglediUpitString + " AND p.datumPregleda <= :to";
            }

            List<PregledResponseDTO> pregledi = new ArrayList<>();

            TypedQuery<Pregled> upitPregledi = entityManager.createQuery(preglediUpitString, Pregled.class);

            upitPregledi.setParameter("zk", zk);

            if (preglediRequestDTO.getOn() != null) {
                upitPregledi.setParameter("on", preglediRequestDTO.getOn());
            }

            if (preglediRequestDTO.getFrom() != null) {
                upitPregledi.setParameter("from", preglediRequestDTO.getFrom());
            }

            if (preglediRequestDTO.getTo() != null) {
                upitPregledi.setParameter("to", preglediRequestDTO.getTo());
            }

            upitPregledi.setFirstResult((page - 1) * size);
            upitPregledi.setMaxResults(size);

            for (Pregled p : upitPregledi.getResultList()) {
                pregledi.add(new PregledResponseDTO(p));
            }

            return ok(pregledi);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-istorija-bolesti/{lbp}")
    public ResponseEntity<?> fetchIstorijaBolestiLbp(@RequestBody IstorijaBolestiRequestDTO istorijaBolestiRequestDTO,
                                                     @PathVariable String lbp,
                                                     @RequestParam int page,
                                                     @RequestParam int size) {
        if (loggedInUser.getRoles().contains(Constants.ADMIN) || loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(UUID.fromString(lbp));

            if (pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ZdravstveniKarton zk = pacijent.getZdravstveniKarton();


            String istorijaUpitString = "SELECT i FROM IstorijaBolesti i WHERE i.zdravstveniKarton = :zk";

            if (!loggedInUser.getRoles().contains(Constants.SPECIJLISTA_POV)) {
                istorijaUpitString = istorijaUpitString + " AND i.indikatorPoverljivosti = false";
            }

            if (istorijaBolestiRequestDTO.getDijagnoza() != null) {
                istorijaUpitString = istorijaUpitString + " AND i.dijagnoza like :dijagnoza";
            }

            List<IstorijaBolestiResponseDTO> istorija = new ArrayList<>();

            TypedQuery<IstorijaBolesti> upitIstorija = entityManager.createQuery(istorijaUpitString, IstorijaBolesti.class);

            upitIstorija.setParameter("zk", zk);

            if (istorijaBolestiRequestDTO.getDijagnoza() != null) {
                upitIstorija.setParameter("dijagnoza", istorijaBolestiRequestDTO.getDijagnoza());
            }
            upitIstorija.setFirstResult((page - 1) * size);
            upitIstorija.setMaxResults(size);

            for (IstorijaBolesti i : upitIstorija.getResultList()) {
                istorija.add(new IstorijaBolestiResponseDTO(i));
            }

            return ok(istorija);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PostMapping("/filter-patients")
    public ResponseEntity<?> filterPatients(@RequestBody FilterPatientsRequestDTO filterPatientsRequestDTO) {
        if (loggedInUser.getRoles().contains(Constants.ADMIN) || loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA) ||
                loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA) ||
                loggedInUser.getRoles().contains(Constants.MED_SESTRA)) {

            String pacijentUpitString = "SELECT p FROM Pacijent p";

            int cnt = 0;

            if (filterPatientsRequestDTO.getLbp() != null) {
                pacijentUpitString = pacijentUpitString + " WHERE p.lbp = :lbp";
                cnt = cnt + 1;
            }

            if (filterPatientsRequestDTO.getJmbg() != null) {
                if (cnt == 0) {
                    pacijentUpitString = pacijentUpitString + " WHERE ";
                } else {
                    pacijentUpitString = pacijentUpitString + " AND ";
                }
                pacijentUpitString = pacijentUpitString + "p.jmbg = :jmbg";
                cnt = cnt + 1;
            }

            if (filterPatientsRequestDTO.getIme() != null) {
                if (cnt == 0) {
                    pacijentUpitString = pacijentUpitString + " WHERE ";
                } else {
                    pacijentUpitString = pacijentUpitString + " AND ";
                }
                pacijentUpitString = pacijentUpitString + "p.ime = :ime";
                cnt = cnt + 1;
            }

            if (filterPatientsRequestDTO.getPrezime() != null) {
                if (cnt == 0) {
                    pacijentUpitString = pacijentUpitString + " WHERE ";
                } else {
                    pacijentUpitString = pacijentUpitString + " AND ";
                }
                pacijentUpitString = pacijentUpitString + "p.prezime = :prezime";
            }

            List<PacijentResponseDTO> pacijenti = new ArrayList<>();

            TypedQuery<Pacijent> upitPacijent =
                    entityManager.createQuery(pacijentUpitString, Pacijent.class);

            if (filterPatientsRequestDTO.getLbp() != null) {
                upitPacijent.setParameter("lbp", filterPatientsRequestDTO.getLbp());
            }

            if (filterPatientsRequestDTO.getJmbg() != null) {
                upitPacijent.setParameter("jmbg", filterPatientsRequestDTO.getJmbg());
            }

            if (filterPatientsRequestDTO.getIme() != null) {
                upitPacijent.setParameter("ime", filterPatientsRequestDTO.getIme());
            }

            if (filterPatientsRequestDTO.getPrezime() != null) {
                upitPacijent.setParameter("prezime", filterPatientsRequestDTO.getPrezime());
            }


            for (Pacijent p : upitPacijent.getResultList()) {
                pacijenti.add(new PacijentResponseDTO(p));
            }

            return ok(pacijenti);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(value = "/update-appointment-status")
    public ResponseEntity<?> updateAppointmentStatus(@RequestBody UpdateAppointmentStatusDTO requestDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add("ROLE_DR_SPEC_ODELJENJA");
        acceptedRoles.add("ROLE_DR_SPEC");
        acceptedRoles.add("ROLE_ADMIN");
        if (requestDTO.getAppointmentStatus() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            ZakazaniPregled appointmentToReturn = appointmentService.updateAppointment(requestDTO);
            return ResponseEntity.ok(appointmentToReturn);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(value = "/update-arrival-status")
    public ResponseEntity<?> updateArrivalStatus(@RequestBody UpdateArrivalStatusDTO requestDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add("ROLE_VISA_MED_SESTRA");
        acceptedRoles.add("ROLE_MED_SESTRA");
        acceptedRoles.add("ROLE_ADMIN");
        if (requestDTO.getArrivalStatus() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            ZakazaniPregled appointmentToReturn = appointmentService.updateArrival(requestDTO);
            return ResponseEntity.ok(appointmentToReturn);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(value = "/list-appointments-by-lbz")
    public ResponseEntity<List<?>> listAppointmentsByLBZ(@RequestBody SearchForAppointmentDTO searchForAppointmentDTO) {
        //Načelnik odeljenja, Doktor specijalista, Viša medicinska sestra i Medicinska sestra
        String[] roles = {"ROLE_ADMIN", "ROLE_DR_SPEC_ODELJENJA", "ROLE_DR_SPEC", "ROLE_VISA_MED_SESTA", "ROLE_MED_SESTRA"};
        for (int i = 0; i < 4; i++) {
            if (loggedInUser.getRoles().contains(roles[i])) {
                if (searchForAppointmentDTO.getDate() == null) {
                    return ResponseEntity.ok(appointmentService.getAppointmentByLBZ(searchForAppointmentDTO.getLbz()));
                } else {
                    return ResponseEntity.ok(appointmentService.getAppointmentByLBZAndDate(searchForAppointmentDTO.getLbz(), searchForAppointmentDTO.getDate()));
                }
            }

        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
