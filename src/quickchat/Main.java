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
        boolean bolValidField;
        final int OPTION_REGISTER = 1;
        final int OPTION_LOGIN = 2;
        final int OPTION_QUIT = 3;
        
        //prompt the user to choose options
        System.out.println("Please select an option:");
        System.out.println("1. Register New Account");
        System.out.println("2. Log In");
        System.out.println("3. Exit");

        while (!input.hasNextInt()) {
            System.out.println("Invalid Input. Enter a number (1-3)");
            input.next(); 
        }

        intMenuChoice = input.nextInt();
        input.nextLine(); 

        //exucutes based on the users actions
        while (intMenuChoice != OPTION_QUIT) {
            if (intMenuChoice == OPTION_REGISTER) {
                if (bolRegistered) {
                    System.out.println("System Error: A user is already registered in this session.");
                } else {
                    //user registration
                    System.out.println("User Registration");
                    
                    System.out.println("First Name:");
                    strFirstName = input.nextLine();

                    System.out.println("Last Name:");
                    strLastName = input.nextLine();

                    
                    bolValidField = false;
                    while (!bolValidField) {
                        System.out.println("Username (max of 5 characters & must contain an underscore):");
                        strUsername = input.nextLine();

                        bolValidField = loginSystem.checkUserName(strUsername);
                        if (!bolValidField) {
                            System.out.println("Username is not correctly formatted, please ensure that your "
                                    + "username contains an underscore and is no more than 5 characters in length.");
                        }
                    }

                    
                    bolValidField = false;
                    while (!bolValidField) {
                        System.out.println("Password (min 8 characters, 1 capital, 1 number, 1 special character):");
                        strPassword = input.nextLine();

                        bolValidField = loginSystem.checkPasswordComplexity(strPassword);
                        if (!bolValidField) {
                            System.out.println("Password is not correctly formatted, please ensure that the password "
                                    + "contains at least 8 characters, a capital letter, a number and a special character.");
                        }
                    }

                    
                    bolValidField = false;
                    while (!bolValidField) {
                        System.out.println("Cellphone Number (international code, max 10 digits after the code):");
                        strCellPhoneNumber = input.nextLine();

                        bolValidField = loginSystem.checkCellPhone(strCellPhoneNumber);
                        if (!bolValidField) {
                            System.out.println("Cell phone number incorrectly formatted or does not contain "
                                    + "an international code.");
                        }
                    }

                    //call registration method to check all three fields are already valid here
                    String regResult = loginSystem.registerUser(strFirstName, strLastName,
                            strUsername, strPassword, strCellPhoneNumber);
                    System.out.println(regResult);

                    bolRegistered = true;
                }
            } else if (intMenuChoice == OPTION_LOGIN) {
                if (!bolRegistered) {
                    System.out.println("System Error: No account registered yet. Please register first.");
                } else {
                    
                    //user login
                    System.out.println("USER LOGIN");
                    System.out.println("Username:");
                    String strLoginUsername = input.nextLine();
                    System.out.println("Password:");
                    String strLoginPassword = input.nextLine();

                    //authenticate credentials
                    bolLoginSuccess = loginSystem.loginUser(strLoginUsername, strLoginPassword);

                    String statusMessage = loginSystem.returnLoginStatus(bolLoginSuccess, strFirstName, strLastName);
                    System.out.println(statusMessage);

                    if (bolLoginSuccess) {
                        System.out.println("Access Granted. Redirecting to main application.");
                        intMenuChoice = OPTION_QUIT;
                    }
                }
            } else {
                System.out.println("Invalid selection (Please choose options 1-3)");
            }

            if (!bolLoginSuccess && intMenuChoice != OPTION_QUIT) {
                System.out.println("Select Option:");
                System.out.println("1. Register New Account");
                System.out.println("2. Log In");
                System.out.println("3. Exit");

                while (!input.hasNextInt()) {
                    System.out.println("Invalid input. Enter a number (1-3):");
                    input.next();
                }
                intMenuChoice = input.nextInt();
                input.nextLine(); 
            }
        }

        System.out.println("Thank you for using QuickChat");
    }
}