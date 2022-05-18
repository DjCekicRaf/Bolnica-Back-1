package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.management.entities.Operacija;

public interface OperacijaRepository extends JpaRepository<Operacija, Long> {

}
