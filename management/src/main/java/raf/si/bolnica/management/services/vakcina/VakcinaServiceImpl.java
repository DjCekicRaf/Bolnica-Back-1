package raf.si.bolnica.management.services.vakcina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.Vakcina;
import raf.si.bolnica.management.repositories.VakcinaRepository;

@Service
@Transactional("transactionManager")
public class VakcinaServiceImpl implements VakcinaService {

    @Autowired
    VakcinaRepository vakcinaRepository;

    @Override
    public Vakcina findVakcinaByNaziv(String naziv) {
        return vakcinaRepository.findVakcinaByNaziv(naziv);
    }

    @Override
    public Vakcina save(Vakcina vakcina) {
        return vakcinaRepository.save(vakcina);
    }
}
