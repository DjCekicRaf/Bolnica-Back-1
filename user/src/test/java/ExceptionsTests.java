import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.util.ReflectionTestUtils;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.exceptions.InvalidRegistrationException;
import raf.si.bolnica.user.exceptions.UnauthorisedException;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.requests.LoginRequestDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@ExtendWith(MockitoExtension.class)
public class ExceptionsTests {

    @Mock
    UserExceptionHandler userExceptionHandler;

    @BeforeEach
    public void setup() {
        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler,"validateUserUsername",handler.validateUserUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validatePassword",handler.validatePassword);
        ReflectionTestUtils.setField(userExceptionHandler,"validateUsername",handler.validateUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserGender",handler.validateUserGender);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserTitle", handler.validateUserTitle);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserProfession",handler.validateUserProfession);
    }

    @Test
    public void invalidUserUsername() {
        Throwable thrown = catchThrowable(() -> {
            userExceptionHandler.validateUserUsername.accept(null);
        });
        assertThat(thrown).isInstanceOf(UnauthorisedException.class);
        assertThat(((UnauthorisedException)thrown).getErrorResponse().getErrorMsg()).isEqualTo(Constants.InvalidAdminUsername.MESSAGE);
    }

    @Test
    public void invalidUserPassword() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("username","password");
        User admin = new User();
        admin.setPassword(BCrypt.hashpw("other",BCrypt.gensalt()));
        Throwable thrown = catchThrowable(() -> {
            userExceptionHandler.validatePassword.accept(requestDTO,admin);
        });
        assertThat(thrown).isInstanceOf(UnauthorisedException.class);
        assertThat(((UnauthorisedException)thrown).getErrorResponse().getErrorMsg()).isEqualTo(Constants.ForgetPassword.MESSAGE);

    }

    @Test
    public void invalidUsernameTests() {
        Throwable thrown = catchThrowable(() -> {
            userExceptionHandler.validateUsername.accept("-/*/**-/");
        });
        assertThat(thrown).isInstanceOf(InvalidRegistrationException.class);
        thrown = catchThrowable(() -> {
            userExceptionHandler.validateUsername.accept("user");
        });
        assertThat(thrown).isInstanceOf(InvalidRegistrationException.class);
        thrown = catchThrowable(() -> {
            userExceptionHandler.validateUsername.accept("usernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusername");
        });
        assertThat(thrown).isInstanceOf(InvalidRegistrationException.class);
    }

    @Test
    public void invalidUserGender() {
        Throwable thrown = catchThrowable(() -> {
            userExceptionHandler.validateUserGender.accept("pol");
        });
        assertThat(thrown).isInstanceOf(InvalidRegistrationException.class);
    }

    @Test
    public void invalidUserTitle() {
        Throwable thrown = catchThrowable(() -> {
            userExceptionHandler.validateUserTitle.accept("Dr");
        });
        assertThat(thrown).isInstanceOf(InvalidRegistrationException.class);
    }

    @Test
    public void invalidUserProfession() {
        Throwable thrown = catchThrowable(() -> {
            userExceptionHandler.validateUserProfession.accept("Programer");
        });
        assertThat(thrown).isInstanceOf(InvalidRegistrationException.class);
    }
}
