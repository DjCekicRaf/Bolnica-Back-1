package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.Alergen;

import java.util.List;

@Repository
public interface AlergenRepository extends JpaRepository<Alergen, Long> {

    Alergen findByAlergenId(Long id);

    @Override
    List<Alergen> findAll();

    @Query("select a from Alergen a where a.naziv like :naziv")
    Alergen findAlergenByNaziv(@Param("naziv") String naziv);

}
