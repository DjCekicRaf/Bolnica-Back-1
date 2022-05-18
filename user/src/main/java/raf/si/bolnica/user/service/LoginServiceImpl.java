package raf.si.bolnica.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.jwt.JwtTokenProvider;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.repositories.UserRepository;
import raf.si.bolnica.user.requests.LoginRequestDTO;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Service
@Transactional("transactionManager")
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserExceptionHandler userExceptionHandler;

    @Override
    public String login(LoginRequestDTO requestDTO, HttpServletRequest request) {
        User user = fetchUserDetails.apply(requestDTO);
        userExceptionHandler.validateUserUsername.accept(user);
        userExceptionHandler.validatePassword.accept(requestDTO, user);

        return jwtTokenProvider.createToken(requestDTO.getEmail(), user);
    }

    private final Function<LoginRequestDTO, User> fetchUserDetails = (loginRequestDTO) -> userRepository.findByEmail(loginRequestDTO.getEmail());
    
}
