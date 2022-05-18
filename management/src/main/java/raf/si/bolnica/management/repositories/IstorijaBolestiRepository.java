package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.ZdravstveniKarton;

public interface IstorijaBolestiRepository extends JpaRepository<IstorijaBolesti, Long> {
    IstorijaBolesti getIstorijaBolestiByZdravstveniKartonAndPodaciValidni(ZdravstveniKarton zdravstveniKarton, boolean podaciValidni);
}
