package raf.si.bolnica.user.service;

import org.springframework.data.jpa.domain.Specification;
import raf.si.bolnica.user.models.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User fetchUserByEmail(String email);

    User fetchUserByUsername(String username);

    User fetchUserByLBZ(UUID lbz);

    User saveEmployee(User user);

    String generateNewPassword(User user);

    List<User> fetchUsersByPBO(Long id);

    User savePassword(User user, String password);

    void deleteById(Long id);

}
