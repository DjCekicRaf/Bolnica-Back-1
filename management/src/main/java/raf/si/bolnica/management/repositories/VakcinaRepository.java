package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.Vakcina;

@Repository
public interface VakcinaRepository extends JpaRepository<Vakcina, Long> {

    Vakcina findVakcinaByNaziv(String naziv);

}
