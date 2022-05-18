import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.user.controllers.LoginController;
import raf.si.bolnica.user.requests.LoginRequestDTO;
import raf.si.bolnica.user.service.LoginService;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    LoginService loginService;

    @InjectMocks
    LoginController loginController;

    @Test
    public void loginTest() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("email");
        requestDTO.setPassword("pass");
        when(loginService.login(any(LoginRequestDTO.class),any(HttpServletRequest.class))).thenReturn("token");

        ResponseEntity<?> response = loginController.loginUser(requestDTO,mock(HttpServletRequest.class));

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
