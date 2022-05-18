package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.Vakcinacija;
import raf.si.bolnica.management.repositories.VakcinacijaRepository;

@Service
@Transactional("transactionManager")
public class VakcinacijaServiceImpl implements VakcinacijaService {

    @Autowired
    VakcinacijaRepository vakcinacijaRepository;

    @Override
    public Vakcinacija save(Vakcinacija vakcinacija) {
        return vakcinacijaRepository.save(vakcinacija);
    }
}
