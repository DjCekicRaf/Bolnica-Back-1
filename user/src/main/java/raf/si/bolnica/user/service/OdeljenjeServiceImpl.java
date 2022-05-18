package raf.si.bolnica.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.repositories.OdeljenjeRepository;

import java.util.List;

@Service
@Transactional("transactionManager")
public class OdeljenjeServiceImpl implements OdeljenjeService {

    @Autowired
    OdeljenjeRepository odeljenjeRepository;

    @Override
    public Odeljenje fetchOdeljenjeById(long id) {
        return odeljenjeRepository.findById(id);
    }

    @Override
    public List<Odeljenje> findAll() {
        return odeljenjeRepository.findAll();
    }
}
