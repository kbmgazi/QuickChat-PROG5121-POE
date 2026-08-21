/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

/**
 *
 * @author Kea
 */
import quickchat.Main;
public class UserLogin {
    
    //declarations
    private String registeredUsername;
    private String registeredPassword;
    private String registeredFirstName;
    private String registeredLastName;
    private String registeredCellPhoneNumber;
    
    //username validation method 
    public boolean checkUserName(String username) {
        boolean hasUnderscore = false;
        
        if (username.length() <= 5) {
            for (int i = 0; i < username.length(); i++) {
                if (username.contains("_")) {
                    hasUnderscore = true;
                }
            }
        }
        return hasUnderscore;
    }
    
    //password complexity validation method 
    public boolean checkPasswordComplexity(String password) {
        //declaration 
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        char c;
        
        if (password.length() >= 8) {
            for (int i = 0 ; i < password.length() - 1; i++) {
                c = password.charAt(i);
                if (Character.isUpperCase(c)) {
                    hasCapital = true;
                }else if (Character.isDigit(c)) {
                    hasNumber = true;
                } else if (!Character.isLetterOrDigit(c) && c != " ") {
                    hasSpecial = true;
                }
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }
    
    //cell phone number validation method 
    public boolean checkCellPhone(String cellPhoneNumber) {
        //declaration 
          boolean isValid = false;
          String zaCode = "";
          
          if (cellPhoneNumber.length() <= 10 && cellPhoneNumber.matches(zaCode)) {
              isValid = true;
          }
          return isValid;
    }
    
    //user registration method 
    public String registerUser(String firstName, String lastName, String username,String password, String cellPhoneNumber ) {
        //declaration 
        boolean usernameValid;
        boolean passwordValid;
        boolean cellPhoneNumberValid;
        String message;
        
        usernameValid = checkUserName(username);
        passwordValid = checkPasswordComplexity(password);
        cellPhoneNumberValid = checkCellPhone(cellPhoneNumber);
        
        //register user if username and password requirement are valid
        if (usernameValid && passwordValid) {
            registeredFirstName = firstName;
            registeredLastName = lastName;
            registeredUsername = username;
            registeredPassword = password;
            registeredCellPhoneNumber = cellPhoneNumber;
            
            message = "";
            message = "";
            
            //cellphone 
            if (cellPhoneNumberValid) {
                message = "";
            }
            
        }else{
            if (!usernameValid) {
                message = "";
            }
            if (!passwordValid) {
                message = "";
            }
            if (!cellPhoneNumberValid) {
                message = "";
            }
        }
        
        return message;
    }
    
    //login authentication method 
    public boolean loginUser(String username, String password){
        //declaration 
            boolean loginSuccess = false;
            
            if (username == registeredUsername && password == registeredPassword) {
                loginSuccess = true;
            }
            
     return loginSuccess;
    }
    
    //authentication status reporter method
    public String returnLoginStatus(boolean loginSuccess, String firstName, String lastName) {
        //declaration 
        String statusMessage;
        
        if (loginSuccess) {
            statusMessage = "";
        }else {
            statusMessage = "";
        }
    return statusMessage;
    }
    
    
}
