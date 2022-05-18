package raf.si.bolnica.user.constants;

public class Constants {

    public static final String BASE_API = "/api";
    public static final String LOGIN = "/login";
    public static final String CREATE_EMPLOYEE = "/create-employee";
    public static final String REMOVE_EMPLOYEE = "/remove-employee";
    public static final String GET_EMPLOYEE = "/get-employee/{lbz}";
    public static final String LIST_EMPLOYEES = "/list-employees";
    public static final String UPDATE_EMPLOYEE = "/update-employee";
    public static final String LIST_EMPLOYEES_BY_PBO = "/find-employees-pbo/{pbo}";

    public static final String JWT_KEY = "QnuQblQWn8H9ggiwfGbCxpPA3gdY1oAe";

    public interface ForgetPassword {
        String DEVELOPER_MESSAGE = "Password didn't match with the original one.";
        String MESSAGE = "Incorrect password.Forgot Password?";
    }

    public interface RegistrationInvalidFields {
        String DEVELOPER_MESSAGE_USERNAME_INVALID_MIN_CHARACTERS = "Username not valid";
        String MESSAGE_USERNAME_INVALID_MIN_CHARACTERS = "Min 5 characters";

        String DEVELOPER_MESSAGE_USERNAME_INVALID_MAX_CHARACTERS = "Username not valid";
        String MESSAGE_USERNAME_INVALID_MAX_CHARACTERS = "Max 30 characters";

        String DEVELOPER_MESSAGE_USERNAME_INVALID_FORMAT = "Username not valid";
        String MESSAGE_USERNAME_INVALID_FORMAT = "Only digits and characters";

        String DEVELOPER_MESSAGE_USER_TITLE_INVALID = "User title not valid";
        String MESSAGE_USER_TITLE_INVALID = "There is no such title";

        String DEVELOPER_MESSAGE_USER_PROFESSION_INVALID = "User profession not valid";
        String MESSAGE_USER_PROFESSION_INVALID = "There is no such profession";

        String DEVELOPER_MESSAGE_USER_GENDER_INVALID = "User gender not valid";
        String MESSAGE_USER_GENDER_INVALID = "There is no such gender";
    }

    public interface InvalidAdminUsername {
        String DEVELOPER_MESSAGE = "Admin entity returned null";
        String MESSAGE = "Admin with given username doesn't exits.";
    }
}