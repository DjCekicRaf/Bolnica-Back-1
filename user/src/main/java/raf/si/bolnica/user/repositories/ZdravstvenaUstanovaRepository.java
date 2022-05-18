package raf.si.bolnica.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;

@Repository
public interface ZdravstvenaUstanovaRepository extends JpaRepository<ZdravstvenaUstanova, Long> {}
