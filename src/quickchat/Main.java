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


/**
 * Console entry point for QuickChat (Part 1): register, then log in.
 *
 * @author Kea
 */
public class Main {

    public static void main(String[] args) {
        UserLogin loginSystem = new UserLogin();
        Scanner input = new Scanner(System.in);

        String strUsername = "";
        String strPassword = "";
        String strFirstName = "";
        String strLastName = "";
        String strCellPhoneNumber = "";

        int intMenuChoice;
        boolean bolLoginSuccess = false;
        boolean bolRegistered = false;
        boolean bolValidField;
        final int OPTION_REGISTER = 1;
        final int OPTION_LOGIN = 2;
        final int OPTION_QUIT = 3;

        intMenuChoice = readMenuChoice(input);

        while (intMenuChoice != OPTION_QUIT) {
            if (intMenuChoice == OPTION_REGISTER) {
                if (bolRegistered) {
                    System.out.println("System Error: A user is already registered in this session.");
                } else {
                    System.out.println("User Registration");

                    System.out.println("First Name:");
                    strFirstName = input.nextLine();

                    System.out.println("Last Name:");
                    strLastName = input.nextLine();

                    // Each field is re-asked until valid; the capture message is printed every time
                    do {
                        System.out.println("Username (max of 5 characters & must contain an underscore):");
                        strUsername = input.nextLine();
                        System.out.println(loginSystem.usernameMessage(strUsername));
                        bolValidField = loginSystem.checkUserName(strUsername);
                    } while (!bolValidField);

                    do {
                        System.out.println("Password (min 8 characters, 1 capital, 1 number, 1 special character):");
                        strPassword = input.nextLine();
                        System.out.println(loginSystem.passwordMessage(strPassword));
                        bolValidField = loginSystem.checkPasswordComplexity(strPassword);
                    } while (!bolValidField);

                    do {
                        System.out.println("Cellphone Number (international code, e.g. +27838968976):");
                        strCellPhoneNumber = input.nextLine();
                        System.out.println(loginSystem.cellPhoneMessage(strCellPhoneNumber));
                        bolValidField = loginSystem.checkCellPhoneNumber(strCellPhoneNumber);
                    } while (!bolValidField);

                    System.out.println(loginSystem.registerUser(strFirstName, strLastName,
                            strUsername, strPassword, strCellPhoneNumber));
                    bolRegistered = true;
                }
            } else if (intMenuChoice == OPTION_LOGIN) {
                if (!bolRegistered) {
                    System.out.println("System Error: No account registered yet. Please register first.");
                } else {
                    System.out.println("USER LOGIN");
                    System.out.println("Username:");
                    String strLoginUsername = input.nextLine();
                    System.out.println("Password:");
                    String strLoginPassword = input.nextLine();

                    bolLoginSuccess = loginSystem.loginUser(strLoginUsername, strLoginPassword);
                    System.out.println(loginSystem.returnLoginStatus(bolLoginSuccess, strFirstName, strLastName));

                    if (bolLoginSuccess) {
                        System.out.println("Access Granted. Redirecting to main application.");
                        intMenuChoice = OPTION_QUIT;
                    }
                }
            } else {
                System.out.println("Invalid selection (Please choose options 1-3)");
            }

            if (!bolLoginSuccess && intMenuChoice != OPTION_QUIT) {
                intMenuChoice = readMenuChoice(input);
            }
        }

        System.out.println("Thank you for using QuickChat");
    }

    /** Shows the menu and returns a validated integer choice. */
    private static int readMenuChoice(Scanner input) {
        System.out.println("Please select an option:");
        System.out.println("1. Register New Account");
        System.out.println("2. Log In");
        System.out.println("3. Exit");

        while (!input.hasNextInt()) {
            System.out.println("Invalid input. Enter a number (1-3):");
            input.next();
        }
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }
}