package raf.si.bolnica.management.services.zdravstveniKarton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.repositories.ZdravstveniKartonRepository;

import java.util.UUID;


@Service
@Transactional("transactionManager")
public class ZdravstveniKartonServiceImpl implements ZdravstveniKartonService {

    @Autowired
    ZdravstveniKartonRepository zdravstveniKartonRepository;

    @Override
    public ZdravstveniKarton findZdravstveniKartonById(Long id) {
        return zdravstveniKartonRepository.findZdravstveniKartonByZdravstveniKartonId(id);
    }

    @Override
    public ZdravstveniKarton saveZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
        return zdravstveniKartonRepository.save(zdravstveniKarton);
    }

    @Override
    public ZdravstveniKarton findZdravstveniKartonByPacijentLbp(UUID lbp) {
        return zdravstveniKartonRepository.findZdravstveniKartonByPacijentLbp(lbp);
    }
}
