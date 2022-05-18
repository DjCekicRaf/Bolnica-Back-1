package raf.si.bolnica.user.exceptionHandler.user;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.exceptions.InvalidRegistrationException;
import raf.si.bolnica.user.exceptions.UnauthorisedException;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.requests.LoginRequestDTO;
import raf.si.bolnica.user.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Component
public class UserExceptionHandler {

    public final Consumer<String> validateUsername = (username) -> {
        String pattern = "[a-zA-Z0-9]+";
        if (!username.matches(pattern)) {
            throw new InvalidRegistrationException(Constants.RegistrationInvalidFields.MESSAGE_USERNAME_INVALID_FORMAT, Constants.RegistrationInvalidFields.DEVELOPER_MESSAGE_USERNAME_INVALID_FORMAT);
        }
        if (username.length() < 5)
            throw new InvalidRegistrationException(Constants.RegistrationInvalidFields.MESSAGE_USERNAME_INVALID_MIN_CHARACTERS, Constants.RegistrationInvalidFields.DEVELOPER_MESSAGE_USERNAME_INVALID_MIN_CHARACTERS);
        if (username.length() > 30)
            throw new InvalidRegistrationException(Constants.RegistrationInvalidFields.MESSAGE_USERNAME_INVALID_MAX_CHARACTERS, Constants.RegistrationInvalidFields.DEVELOPER_MESSAGE_USERNAME_INVALID_MAX_CHARACTERS);

    };

    public final Consumer<String> validateUserTitle = (userTitle) -> {
        List<String> titles = FileUtils.readUserTitles();
        System.out.println(titles);
        System.out.println(userTitle);
        if (!titles.contains(userTitle))
            throw new InvalidRegistrationException(Constants.RegistrationInvalidFields.MESSAGE_USER_TITLE_INVALID, Constants.RegistrationInvalidFields.DEVELOPER_MESSAGE_USER_TITLE_INVALID);
    };

    public final Consumer<String> validateUserProfession = (userProfession) -> {
        List<String> professions = FileUtils.readUserProfessions();
        if (!professions.contains(userProfession))
            throw new InvalidRegistrationException(Constants.RegistrationInvalidFields.MESSAGE_USER_PROFESSION_INVALID, Constants.RegistrationInvalidFields.DEVELOPER_MESSAGE_USER_PROFESSION_INVALID);
    };

    public final Consumer<String> validateUserGender = (gender) -> {
        List<String> genders = new ArrayList<>();
        genders.add("male");
        genders.add("female");
        if (!genders.contains(gender))
            throw new InvalidRegistrationException(Constants.RegistrationInvalidFields.MESSAGE_USER_GENDER_INVALID, Constants.RegistrationInvalidFields.DEVELOPER_MESSAGE_USER_GENDER_INVALID);
    };

    public final Consumer<User> validateUserUsername = (admin) -> {
        if (Objects.isNull(admin))
            throw new UnauthorisedException(Constants.InvalidAdminUsername.MESSAGE, Constants.InvalidAdminUsername.DEVELOPER_MESSAGE);
    };

    public final BiConsumer<LoginRequestDTO, User> validatePassword = (requestDTO, admin) -> {
        if (!BCrypt.checkpw(requestDTO.getPassword(), admin.getPassword())) {
            throw new UnauthorisedException(Constants.ForgetPassword.MESSAGE, Constants.ForgetPassword.DEVELOPER_MESSAGE);
        }
    };

}
