/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package quickchat;

/**
 *
 * @author Kea
 */
import java.util.Scanner;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //declaration 
        UserLogin loginSystem = new UserLogin();
        Scanner input = new Scanner(System.in);
        //user input variables
        String strUsername = "";
        String strPassword = "";
        String strFirstName = "";
        String strLastName = "";
        String strCellPhoneNumber = "";
        //system status variables
        int intMenuChoice;
        boolean bolLoginSuccess = false;
        boolean bolRegistered = false;
        final int OPTION_REGISTER = 1;
        final int OPTION_LOGIN = 2;
        final int OPTION_QUIT = 3;
        
        System.out.println("Please select an option:");
        System.out.println("1. Register New Account");
        System.out.println("2. Log In");
        System.out.println("3. Exit");
        
        while (!input.hasNextInt()) {
            System.out.println("Invalid Input. Enter a number (1-3)");
            input.nextInt();
        }
        
        intMenuChoice = input.nextInt();
        
        //Continue executing tasks until the user chooses to exit
        while (intMenuChoice != OPTION_QUIT) {
            if (intMenuChoice == OPTION_REGISTER) {
                if (bolRegistered) {
                    System.out.println("System Error: A user is already registered in this session.");
                }else {
                    System.out.println("First Name:");
                    strFirstName = input.nextLine();
                    
                    System.out.println("Last Name:");
                    strLastName = input.nextLine();
                    
                    System.out.println("Username (max of 5 characters & must contain an underscore):");
                    strUsername = input.nextLine();
                    
                    System.out.println("Password (min 8 characters, 1 capital, 1 number, 1 special character):");
                    strPassword = input.nextLine();
                    
                    System.out.println("Cellphone Number:");
                    strCellPhoneNumber = input.nextLine();
                
                    //call regstration method 
                    String regResult = loginSystem.registerUser(strFirstName, strLastName, strUsername, strPassword, strCellPhoneNumber);
                    System.out.println(regResult);
                
                    //verify registration if it is successful
                    if (loginSystem.checkUserName(strUsername) && loginSystem.checkPasswordComplexity(strPassword)){
                        bolRegistered = true;
                    }
                }
            }else if (intMenuChoice == OPTION_LOGIN) {
                if (!bolRegistered) {
                    System.out.println("System Error: No account registered yet. Please register first.");
                }else {
                    System.out.println("USER LOGIN");
                    System.out.println("Username:");
                    strUsername = input.nextLine();
                    System.out.println("Password:");
                    strPassword = input.nextLine();
                    
                    //authenticate credentials
                    bolLoginSuccess = loginSystem.loginUser(strUsername, strPassword);
                    
                    String statusMessage = loginSystem.returnLoginStatus(bolLoginSuccess, strFirstName, strLastName);
                    System.out.println(statusMessage);
                    
                    if (bolLoginSuccess) {
                        System.out.println("Access Granted. Redirecting to main application.");
                        intMenuChoice = OPTION_QUIT;
                    }
                }
            }else {
                System.out.println("Invalid select (Please choose options 1-3)");
            }
            
            if (!bolLoginSuccess && intMenuChoice != OPTION_QUIT) {
                System.out.println("Select Option:");
                System.out.println("1. Register New Account");
                System.out.println("2. Log In");
                System.out.println("3. Exit");
                
                while (!input.hasNextInt()){
                    System.out.println("Invalid input. Enter a number (1-3):");
                    input.nextInt();
                }
                intMenuChoice = input.nextInt();
                
            }
        }
        System.out.println("Thank you fo using the QuickChat");
    }
   
    
}
