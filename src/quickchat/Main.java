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
//part2 imports
import java.util.Random;
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
        
        //part 2
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        
        boolean isLoggedIn = true;
        int menuChoice = 0;
        menuChoice = scanner.nextInt();
        
        //print welcome message 
        if (isLoggedIn) {
            System.out.println("Welcome to QuickChat");
        }
        
        do {
            System.out.println("\n--- QuickChat Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Enter your choice (1-3): ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number (1-3).");
                scanner.nextLine(); // Clear bad input
                continue;  
            }
            
            
            scanner.nextLine(); // Clear newline buffer
            
            if (menuChoice == 1) {
                if (!isLoggedIn) {
                    System.out.println("Please log in first.");
                }else {
                    System.out.print("\nEnter the number of messages you wish to send: ");
                    int numMessages = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer

                    Message validator = new Message();
                    
                    //loop to process each message requests
                    for (int i = 0; i < numMessages; i = i + 1) {
                        System.out.println("Processing Message " + (i + 1) + " of " + numMessages);
                        
                        //recipienct input validation loop
                        String recipient = "";
                        boolean validRecipient = false;
                        
                        while (!validRecipient) {
                            System.out.print("Enter recipient cell number (e.g., +27718693002): ");
                            recipient = scanner.nextLine();
                            
                            if (validator.checkRecipientCell(recipient)) {
                                System.out.println("Cell phone number successfully captured.");
                                validRecipient = true;
                            }else {
                                System.out.println("Cell phone number is incorrectly formatted or does not contain an international code (+). Please try again.");
                                
                            }
                        }
                        
                        //input message text validation
                        String text = "";
                        boolean validText = false;
                        
                        while (!validText) {
                            System.out.print("Enter message text (max 250 characters): ");
                            text = scanner.nextLine();
                            
                            String lengthStatus = validator.checkMessageLengthStatus(text);
                            System.out.println(lengthStatus);
                            
                            if (text.length() <= 250) {
                                 validText = true;
                            }
                        }
                        
                        //generate message ID 
                        long rawRandomNum = (long) (random.nextDouble() * 10000000000L);
                        String messageID = String.format("%010d", rawRandomNum);
                        System.out.println("Message ID generated: " + messageID);
                        
                        // message object 
                        Message msg = new Message(recipient, text);
                        msg.setMessageID(messageID);
                        msg.setMessageNumber(i);
                        
                        //generate message hash
                        String hash = msg.createMessageHash(messageID, i, text);
                        System.out.println("Message Hash: " + hash);
                        
                        // action selection 
                        System.out.println("1) Send Message");
                        System.out.println("2) Discard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Enter choice (1-3): ");
                        int actionChoice = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer
                        
                        String actionResult = msg.sentMessage(actionChoice);
                        System.out.println("Status: " + actionResult);
                        
                        //log sessions
                         if (actionChoice == 1) {
                            String logEntry = "Message ID: " + msg.getMessageID() +
                                              "\nMessage Hash: " + msg.getMessageHash() +
                                              "\nRecipient: " + msg.getRecipientCell() +
                                              "\nMessage: " + msg.getMessageText();
                            
                            Message.addSessionLog(logEntry);
                         }
                         
                         //print message summary 
                        System.out.println("\n--- Message Summary ---");
                        System.out.println("Message ID: " + msg.getMessageID());
                        System.out.println("Message Hash: " + msg.getMessageHash());
                        System.out.println("Recipient: " + msg.getRecipientCell());
                        System.out.println("Message: " + msg.getMessageText());
                        
                    }
                }
            }else if (menuChoice == 2){
                System.out.println("Recently Sent Messages");
                System.out.println(Message.printMessages);
                    
            }else if (menuChoice == 3) {
                System.out.println("\nQuitting QuickChat. Goodbye!");
                System.out.println("Total messages sent during session: " + Message.returnTotalMessages());
            }else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }while (menuChoice != 3);
        
        scanner.close();
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