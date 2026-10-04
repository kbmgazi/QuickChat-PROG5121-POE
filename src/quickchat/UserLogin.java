package quickchat;

/**
 * Login class: handles registration validation, login authentication
 * and the status messages for QuickChat (Part 1).
 *
 * @author Kea
 */
public class UserLogin {

    // ---- Messages required by the brief (kept as constants so Main and the tests reuse the exact text) ----
    public static final String USERNAME_OK = "Username successfully captured.";
    public static final String USERNAME_BAD = "Username is not correctly formatted; please ensure that your username "
            + "contains an underscore and is no more than five characters in length.";
    public static final String PASSWORD_OK = "Password successfully captured.";
    public static final String PASSWORD_BAD = "Password is not correctly formatted; please ensure that the password "
            + "contains at least eight characters, a capital letter, a number, and a special character.";
    public static final String CELL_OK = "Cell number successfully captured.";
    public static final String CELL_BAD = "Cell number is incorrectly formatted or does not contain an international "
            + "code; please correct the number and try again.";
    public static final String REGISTER_OK = "User successfully registered";
    public static final String LOGIN_BAD = "Username or password incorrect, please try again.";

    // Regex for a South African cell number: "+27" followed by exactly nine digits.
    // Reference: Oracle (2024) java.util.regex.Pattern, Java SE API documentation,
    // https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html
    // Country code format: ITU-T Recommendation E.164 (international public telecommunication numbering plan).
    private static final String CELL_REGEX = "\\+27\\d{9}";

    // Details stored when the user registers
    private String registeredUsername;
    private String registeredPassword;
    private String registeredFirstName;
    private String registeredLastName;
    private String registeredCellPhoneNumber;

    /** Username must contain an underscore and be no more than five characters long. */
    public boolean checkUserName(String username) {
        return username.length() <= 5 && username.contains("_");
    }

    /** Password: 8+ characters, a capital letter, a number and a special character. */
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (Character.isUpperCase(c)) {
                    hasCapital = true;
                } else if (Character.isDigit(c)) {
                    hasNumber = true;
                } else if (!Character.isLetterOrDigit(c) && c != ' ') {
                    hasSpecial = true;
                }
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    /** Cell number must start with the international code (+27) followed by nine digits. */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber.matches(CELL_REGEX);
    }

    /** Capture message for the username (success or failure text). */
    public String usernameMessage(String username) {
        return checkUserName(username) ? USERNAME_OK : USERNAME_BAD;
    }

    /** Capture message for the password (success or failure text). */
    public String passwordMessage(String password) {
        return checkPasswordComplexity(password) ? PASSWORD_OK : PASSWORD_BAD;
    }

    /** Capture message for the cell number (success or failure text). */
    public String cellPhoneMessage(String cellPhoneNumber) {
        return checkCellPhoneNumber(cellPhoneNumber) ? CELL_OK : CELL_BAD;
    }

    /** Validates all details, stores them if valid and returns the registration message. */
    public String registerUser(String firstName, String lastName, String username,
                               String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return USERNAME_BAD;
        }
        if (!checkPasswordComplexity(password)) {
            return PASSWORD_BAD;
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return CELL_BAD;
        }

        registeredFirstName = firstName;
        registeredLastName = lastName;
        registeredUsername = username;
        registeredPassword = password;
        registeredCellPhoneNumber = cellPhoneNumber;
        return REGISTER_OK;
    }

    /** True when the entered details match the details stored at registration. */
    public boolean loginUser(String username, String password) {
        return username.equals(registeredUsername) && password.equals(registeredPassword);
    }

    /** Returns the welcome message on success or the failure message otherwise. */
    public String returnLoginStatus(boolean loginSuccess, String firstName, String lastName) {
        if (loginSuccess) {
            return "Welcome " + firstName + ", " + lastName + ", it is great to see you again.";
        }
        return LOGIN_BAD;
    }
}