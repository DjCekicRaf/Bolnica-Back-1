package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.Operacija;
import raf.si.bolnica.management.repositories.OperacijaRepository;

@Service
@Transactional("transactionManager")
public class OperacijaServiceImpl implements OperacijaService{

    @Autowired
    OperacijaRepository operacijaRepository;

    @Override
    public Operacija saveOperacija(Operacija operacija) {
        return operacijaRepository.save(operacija);
    }
}
