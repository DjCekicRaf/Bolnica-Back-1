package raf.si.bolnica.management.services.zdravstveniKarton;

import raf.si.bolnica.management.entities.ZdravstveniKarton;

import java.util.UUID;

public interface ZdravstveniKartonService {

    ZdravstveniKarton findZdravstveniKartonById(Long id);

    ZdravstveniKarton saveZdravstveniKarton(ZdravstveniKarton zdravstveniKarton);

    ZdravstveniKarton findZdravstveniKartonByPacijentLbp(UUID lbp);

}
