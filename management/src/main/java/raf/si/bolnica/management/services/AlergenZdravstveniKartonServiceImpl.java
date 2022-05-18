package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.AlergenZdravstveniKarton;
import raf.si.bolnica.management.repositories.AlergenZdravstveniKartonRepository;

@Service
@Transactional("transactionManager")
public class AlergenZdravstveniKartonServiceImpl implements AlergenZdravstveniKartonService{

    @Autowired
    AlergenZdravstveniKartonRepository alergenZdravstveniKartonRepository;

    @Override
    public AlergenZdravstveniKarton save(AlergenZdravstveniKarton alergenZdravstveniKarton) {
        return alergenZdravstveniKartonRepository.save(alergenZdravstveniKarton);
    }
}
