package raf.si.bolnica.management.services.alergen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.Alergen;
import raf.si.bolnica.management.repositories.AlergenRepository;

@Service
@Transactional("transactionManager")
public class AlergenServiceImpl implements AlergenService {

    @Autowired
    AlergenRepository alergenRepository;

    @Override
    public Alergen findAlergenByNaziv(String naziv) {
        return alergenRepository.findAlergenByNaziv(naziv);
    }

    @Override
    public Alergen saveAlergen(Alergen alergen) {
        return alergenRepository.save(alergen);
    }
}
