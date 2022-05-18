package raf.si.bolnica.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByKorisnickoIme(String korisnickoIme);

    User findByLbz(UUID lbz);

    List<User> findByOdeljenje(Odeljenje odeljenje);
}
