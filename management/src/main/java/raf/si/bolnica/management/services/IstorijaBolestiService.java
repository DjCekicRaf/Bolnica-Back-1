package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.ZdravstveniKarton;

import java.util.UUID;

public interface IstorijaBolestiService {

    IstorijaBolesti saveIstorijaBolesti(IstorijaBolesti istorijaBolesti);

    IstorijaBolesti fetchByZdravstveniKartonPodaciValidni(ZdravstveniKarton zk, boolean podaci);
}
