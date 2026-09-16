# QuickChat

**Name:** Keamohetse Boitshepo Mgazi (KB)
**Student Number:** ST10511607
**Module:** PROG5121 – Programming 1A
**Project:** QuickChat – Portfolio of Evidence (Part 1)

---

## Project Description

QuickChat is a Java console application that simulates the registration and
login functionality of a messaging platform. The program validates a new
user's details against a set of formatting rules before allowing an account
to be created, and then authenticates that user against their stored
credentials before granting access to the application. The project was
built using object-oriented principles, separating the input/output logic
(`Main`) from the business logic and validation rules (`UserLogin`).

## How Part 1 Functions

Part 1 of the project implements the account **registration** and **login**
workflow:

1. On startup, the user is shown a menu with three options: Register New
   Account, Log In, and Exit.
2. **Registration**
   - The user is prompted for their first name and last name.
   - The user is then prompted for a username. The system checks that the
     username is no more than five characters long and contains an
     underscore, re-prompting until the input is valid.
   - The user is prompted for a password. The system checks that the
     password is at least eight characters long and contains at least one
     capital letter, one number, and one special character, re-prompting
     until the input is valid.
   - The user is prompted for a cell phone number. The system checks that
     the number includes an international dialling code and does not
     exceed the required length, re-prompting until the input is valid.
   - Once every field passes validation, the account is registered and a
     confirmation message is displayed.
3. **Login**
   - The user enters their username and password.
   - The system checks the entered credentials against the registered
     account and displays a personalised success or failure message.
4. **Exit** ends the program.

Each validation rule was implemented as its own method in `UserLogin` so
that it could be tested independently of the console input/output logic.

## Testing

- Unit tests were written using **JUnit 4**, with one test method per
  validation rule (username format, password complexity, cell phone
  number format, login authentication, and status messages).
- Tests cover both valid and invalid input cases for each rule, to confirm
  that correct data is accepted and incorrect data is rejected with the
  expected message.
- **GitHub Actions** was configured to automatically run the JUnit test
  suite on every push, so that regressions are caught before code is
  merged.
- Manual console testing was also carried out by running the compiled
  program in NetBeans and stepping through the registration and login
  menu options with a mix of valid and invalid input.

## Tools Used

- **NetBeans IDE** – used to write, compile, and run the Java application.
- **JUnit 4** – used to write and run automated unit tests against the
  `UserLogin` class.
- **GitHub / GitHub Actions** – used for version control and to run the
  automated test suite on every commit.
- **W3Schools** – used as a reference for Java syntax, `Scanner` input
  handling, and regular expression syntax.
- **Oracle Java Documentation** – used as the primary reference for the
  Java Standard Library, including the `Scanner` and `String` classes.

## References (Harvard Style)

Oracle, 2024. *Java Platform, Standard Edition documentation*. [online]
Available at: <https://docs.oracle.com/en/java/javase/> [Accessed 16
September 2026].

W3Schools, 2024. *Java Tutorial*. [online] Available at:
<https://www.w3schools.com/java/> [Accessed 16 September 2026].

GitHub, 2024. *GitHub Actions documentation*. [online] Available at:
<https://docs.github.com/en/actions> [Accessed 16 September 2026].

JUnit Team, 2024. *JUnit 4*. [online] Available at:
<https://junit.org/junit4/> [Accessed 16 September 2026].

Apache NetBeans, 2024. *Apache NetBeans documentation*. [online] Available
at: <https://netbeans.apache.org/front/main/docs/> [Accessed 16 September
2026].
