# BankingApp

A simple Java banking application with user authentication, account management, transactions, and dashboard views.

## Project Structure

- `src/` - Java source files for controllers, models, and application logic.
- `bin/Pages/` - Compiled FXML files used by the JavaFX UI.
- `transactions/` - Transaction data storage folder.
- `accounts.txt`, `users.txt`, `transactions.txt` - Local text files storing account and transaction data.

## Features

- Admin login and dashboard
- Customer login and dashboard
- Create new bank accounts
- Deposit, withdraw, and transfer money
- View transaction history
- Change password

## Running the App

1. Open the project in your Java IDE.
2. Make sure JavaFX is configured correctly.
3. Run `src/BankingApp.java`.

## Notes

- The app uses local text files for data persistence.
- Ensure `accounts.txt`, `users.txt`, and `transactions.txt` are present in the project root.
