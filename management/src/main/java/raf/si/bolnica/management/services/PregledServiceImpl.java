package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;
import raf.si.bolnica.management.repositories.IstorijaBolestiRepository;
import raf.si.bolnica.management.repositories.PacijentRepository;
import raf.si.bolnica.management.repositories.PregledRepository;
import raf.si.bolnica.management.repositories.ZdravstveniKartonRepository;
import raf.si.bolnica.management.requests.CreatePregledReportRequestDTO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.UUID;

@Service
@Transactional("transactionManager")
public class PregledServiceImpl implements PregledService {

    @Autowired
    PregledRepository pregledRepository;

    @Autowired
    private IstorijaBolestiRepository istorijaBolestiRepository;

    @Autowired
    private ZdravstveniKartonRepository zdravstveniKartonRepository;

    @Autowired
    private PacijentRepository pacijentRepository;


    @Override
    public Pregled savePregled(Pregled pregled) {
        return pregledRepository.save(pregled);
    }

}
