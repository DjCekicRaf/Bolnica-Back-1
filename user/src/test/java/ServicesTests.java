import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.util.ReflectionTestUtils;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.jwt.JwtTokenProvider;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.properties.EmailProperties;
import raf.si.bolnica.user.repositories.OdeljenjeRepository;
import raf.si.bolnica.user.repositories.UserRepository;
import raf.si.bolnica.user.requests.LoginRequestDTO;
import raf.si.bolnica.user.service.EmailServiceImpl;
import raf.si.bolnica.user.service.LoginServiceImpl;
import raf.si.bolnica.user.service.OdeljenjeServiceImpl;
import raf.si.bolnica.user.service.UserServiceImpl;

import javax.mail.AuthenticationFailedException;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServicesTests {

    @Mock
    EmailProperties emailProperties;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    OdeljenjeRepository odeljenjeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserExceptionHandler userExceptionHandler;

    @InjectMocks
    EmailServiceImpl emailService;

    @InjectMocks
    LoginServiceImpl loginService;

    @InjectMocks
    OdeljenjeServiceImpl odeljenjeService;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void sendEmailTest() {
        when(emailProperties.getEmail()).thenReturn("mejl");
        when(emailProperties.getPassword()).thenReturn("sifra");
        emailService.sendEmail("username", "pass");
    }

    @Test
    public void loginTest() {
        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler,"validateUserUsername",handler.validateUserUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validatePassword",handler.validatePassword);

        when(userRepository.findByEmail(any(String.class))).thenAnswer(i -> {
           User u = new User();
           u.setPassword(BCrypt.hashpw("pass",BCrypt.gensalt()));
           u.setEmail((String) i.getArguments()[0]);
           return u;
        });

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("mejl");
        loginRequestDTO.setPassword("pass");

        when(jwtTokenProvider.createToken(any(String.class),any(User.class))).thenReturn("token");

        String token = loginService.login(loginRequestDTO,mock(HttpServletRequest.class));

        assertThat(token).isEqualTo("token");
    }

    @Test
    public void OdeljenjeTests() {
        when(odeljenjeRepository.findAll()).thenReturn(new ArrayList<>());
        when(odeljenjeRepository.findById(eq(1L))).thenAnswer(i -> {
            Odeljenje o = new Odeljenje();
            o.setOdeljenjeId(1L);
            return o;
        });

        Odeljenje o = odeljenjeService.fetchOdeljenjeById(1L);
        assertThat(o.getOdeljenjeId()).isEqualTo(1L);

        List<Odeljenje> lista = odeljenjeService.findAll();
        assertThat(lista.size()).isEqualTo(0);
    }

    @Test
    public void generatePasswordTest() {
        User u = new User();
        String newPassword = userService.generateNewPassword(u);
        assertThat(newPassword.length()).isEqualTo(8);
    }

    @Test
    public void savePasswordTest() {
        User u = new User();
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        User newUser = userService.savePassword(u,"pass");
        assertThat(BCrypt.checkpw("pass",newUser.getPassword())).isTrue();
    }
}
