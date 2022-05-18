package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.management.entities.Vakcinacija;

public interface VakcinacijaRepository extends JpaRepository<Vakcinacija, Long>  {
}
