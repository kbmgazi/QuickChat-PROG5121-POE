package quickchat;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for UserLogin (Part 1). Uses the exact test data from the brief plus extra edge cases. */
public class UserLoginTest {

    // ---- Brief test data: assertEquals ----
    @Test
    public void testUsernameCorrectlyFormatted_Message() {
        assertEquals("Username successfully captured.", new UserLogin().usernameMessage("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted_Message() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains "
                + "an underscore and is no more than five characters in length.",
                new UserLogin().usernameMessage("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity_Message() {
        assertEquals("Password successfully captured.", new UserLogin().passwordMessage("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordFailsComplexity_Message() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains "
                + "at least eight characters, a capital letter, a number, and a special character.",
                new UserLogin().passwordMessage("password"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted_Message() {
        assertEquals("Cell number successfully captured.", new UserLogin().cellPhoneMessage("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted_Message() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; "
                + "please correct the number and try again.", new UserLogin().cellPhoneMessage("08966553"));
    }

    // ---- Brief test data: assertTrue / assertFalse ----
    @Test
    public void testUsernameCorrect_True() {
        assertTrue(new UserLogin().checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrect_False() {
        assertFalse(new UserLogin().checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity_True() {
        assertTrue(new UserLogin().checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordFailsComplexity_False() {
        assertFalse(new UserLogin().checkPasswordComplexity("password"));
    }

    @Test
    public void testCellPhoneCorrect_True() {
        assertTrue(new UserLogin().checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrect_False() {
        assertFalse(new UserLogin().checkCellPhoneNumber("08966553"));
    }

    // ---- Extra edge cases ----
    @Test
    public void testUsernameNoUnderscore() {
        assertFalse(new UserLogin().checkUserName("kb123"));
    }

    @Test
    public void testUsernameTooLong() {
        assertFalse(new UserLogin().checkUserName("kb_2345"));
    }

    @Test
    public void testPasswordNoSpecialChar() {
        assertFalse(new UserLogin().checkPasswordComplexity("Chase123"));
    }

    @Test
    public void testPasswordNoCapital() {
        assertFalse(new UserLogin().checkPasswordComplexity("ch@se123"));
    }

    @Test
    public void testPasswordNoNumber() {
        assertFalse(new UserLogin().checkPasswordComplexity("Ch@seword"));
    }

    @Test
    public void testPasswordSpecialCharAtEnd() {
        assertTrue(new UserLogin().checkPasswordComplexity("Chase12@"));
    }

    @Test
    public void testCellPhoneTooLong() {
        assertFalse(new UserLogin().checkCellPhoneNumber("+2783896897654"));
    }

    @Test
    public void testCellPhoneTooShort() {
        assertFalse(new UserLogin().checkCellPhoneNumber("+2783"));
    }

    // ---- registerUser ----
    @Test
    public void testRegisterUser_Success() {
        assertEquals("User successfully registered",
                new UserLogin().registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "+27838968976"));
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        assertEquals(UserLogin.USERNAME_BAD,
                new UserLogin().registerUser("Keamo", "Mgazi", "kb23", "Ch@se123", "+27838968976"));
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        assertEquals(UserLogin.PASSWORD_BAD,
                new UserLogin().registerUser("Keamo", "Mgazi", "kb_23", "weak", "+27838968976"));
    }

    @Test
    public void testRegisterUser_InvalidCellPhone() {
        assertEquals(UserLogin.CELL_BAD,
                new UserLogin().registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "0838968976"));
    }

    // ---- loginUser / returnLoginStatus ----
    @Test
    public void testLoginSuccessful_True() {
        UserLogin login = new UserLogin();
        login.registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "+27838968976");
        assertTrue(login.loginUser("kb_23", "Ch@se123"));
    }

    @Test
    public void testLoginFailed_WrongPassword() {
        UserLogin login = new UserLogin();
        login.registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "+27838968976");
        assertFalse(login.loginUser("kb_23", "wrongpass"));
    }

    @Test
    public void testLoginFailed_WrongUsername() {
        UserLogin login = new UserLogin();
        login.registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "+27838968976");
        assertFalse(login.loginUser("kb_99", "Ch@se123"));
    }

    @Test
    public void testReturnLoginStatus_Success() {
        assertEquals("Welcome Keamo, Mgazi, it is great to see you again.",
                new UserLogin().returnLoginStatus(true, "Keamo", "Mgazi"));
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        assertEquals("Username or password incorrect, please try again.",
                new UserLogin().returnLoginStatus(false, "Keamo", "Mgazi"));
    }
}