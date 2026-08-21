/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package quickchat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserLoginTest {

    public UserLoginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // ---- checkUserName ----
    @Test
    public void testCheckUserName_Valid() {
        UserLogin instance = new UserLogin();
        assertTrue(instance.checkUserName("kb_23"));
    }

    @Test
    public void testCheckUserName_NoUnderscore() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkUserName("kb123"));
    }

    @Test
    public void testCheckUserName_TooLong() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkUserName("kb_2345"));
    }

    // ---- checkPasswordComplexity ----
    @Test
    public void testCheckPasswordComplexity_Valid() {
        UserLogin instance = new UserLogin();
        assertTrue(instance.checkPasswordComplexity("Ch@se123"));
    }

    @Test
    public void testCheckPasswordComplexity_TooShort() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkPasswordComplexity("Ch@1"));
    }

    @Test
    public void testCheckPasswordComplexity_NoSpecialChar() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkPasswordComplexity("Chase123"));
    }

    @Test
    public void testCheckPasswordComplexity_NoCapital() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkPasswordComplexity("ch@se123"));
    }

    @Test
    public void testCheckPasswordComplexity_NoNumber() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkPasswordComplexity("Ch@seword"));
    }

    @Test
    public void testCheckPasswordComplexity_SpecialCharAtEnd() {
        // regression test for the off-by-one loop bug
        UserLogin instance = new UserLogin();
        assertTrue(instance.checkPasswordComplexity("Chase12@"));
    }

    // ---- checkCellPhone ----
    @Test
    public void testCheckCellPhone_Valid() {
        UserLogin instance = new UserLogin();
        assertTrue(instance.checkCellPhone("+27838968976"));
    }

    @Test
    public void testCheckCellPhone_MissingCountryCode() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkCellPhone("0838968976"));
    }

    @Test
    public void testCheckCellPhone_TooLong() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkCellPhone("+2783896897654"));
    }

    @Test
    public void testCheckCellPhone_TooShort() {
        UserLogin instance = new UserLogin();
        assertFalse(instance.checkCellPhone("+2783"));
    }

    // ---- registerUser ----
    @Test
    public void testRegisterUser_Success() {
        UserLogin instance = new UserLogin();
        String result = instance.registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "+27838968976");
        assertEquals("User successfully registered", result);
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        UserLogin instance = new UserLogin();
        String result = instance.registerUser("Keamo", "Mgazi", "kb23", "Ch@se123", "+27838968976");
        assertTrue(result.contains("Username is not correctly formatted"));
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        UserLogin instance = new UserLogin();
        String result = instance.registerUser("Keamo", "Mgazi", "kb_23", "weak", "+27838968976");
        assertTrue(result.contains("Password is not correctly formatted"));
    }

    @Test
    public void testRegisterUser_InvalidCellPhone() {
        UserLogin instance = new UserLogin();
        String result = instance.registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "0838968976");
        assertTrue(result.contains("Cell phone number is incorrectly formatted"));
    }

    // ---- loginUser ----
    @Test
    public void testLoginUser_Success() {
        UserLogin instance = new UserLogin();
        instance.registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "+27838968976");
        assertTrue(instance.loginUser("kb_23", "Ch@se123"));
    }

    @Test
    public void testLoginUser_WrongPassword() {
        UserLogin instance = new UserLogin();
        instance.registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "+27838968976");
        assertFalse(instance.loginUser("kb_23", "wrongpass"));
    }

    @Test
    public void testLoginUser_WrongUsername() {
        UserLogin instance = new UserLogin();
        instance.registerUser("Keamo", "Mgazi", "kb_23", "Ch@se123", "+27838968976");
        assertFalse(instance.loginUser("kb_99", "Ch@se123"));
    }

    // ---- returnLoginStatus ----
    @Test
    public void testReturnLoginStatus_Success() {
        UserLogin instance = new UserLogin();
        String result = instance.returnLoginStatus(true, "Keamo", "Mgazi");
        assertEquals("Welcome Keamo, Mgazi, it is great to see you again.", result);
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        UserLogin instance = new UserLogin();
        String result = instance.returnLoginStatus(false, "Keamo", "Mgazi");
        assertEquals("Username or password incorrect, please try again.", result);
    }
}