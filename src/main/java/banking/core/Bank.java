package banking.core;

import banking.entity.Card;
import banking.menu.LoginMenu;

import java.util.Scanner;

public class Bank {
    private static final Bank instance = new Bank();
    public static Bank getInstance() {
        return instance;
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static final Ledger ledger = new Ledger();
    private Card card;
    private static boolean isStarted = false;

    public Bank() {
        setCard(null);
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            new LoginMenu(this).run();
        }
    }

    private void setCard(Card card) {
        this.card = card;
    }

    public void createAccount() {
        Card card = ledger.createAccountAndCard();
        System.out.println("\n");
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(card.getNumber());
        System.out.println("Your card PIN:");
        System.out.println(card.getPin());
    }

    public boolean logIntoAccount() {
        System.out.println("\n");
        System.out.println("Enter your card number:");
        String number = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();

        Card card = ledger.getCard(number);
        if (card != null && card.getPin().equals(pin)) {
            setCard(card);
            System.out.println("You have successfully logged in!");
            return true;
        }
        System.out.println("Wrong card number or PIN!");
        return false;
    }

    public void logOutFromAccount() {
        setCard(null);
        System.out.println("You have successfully logged out!");
    }

    public void closeAccount() {
        if (card != null) {
            if(ledger.deleteAccountAndCard(card.getNumber())) {
                setCard(null);
                System.out.println("The account has been closed!");
            } else {
                System.out.println("Something was wrong!");
            }
        }
    }

    public void getBalance() {
        if (card != null) {
            System.out.println("Balance: " + card.getAccount().getBalance());
        }
    }

    public void addIncome() {
        if (card != null) {
            System.out.println("Enter income:");

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
            } else {
                int income = scanner.nextInt();

                if (income > 0) {
                    if(ledger.registerIncome(card.getAccount().getNumber(), income)) {
                        card = ledger.getCard(card.getId());
                        System.out.println("Income was added!");
                        return;
                    } else {
                        System.out.println("Something was wrong!");
                    }
                }
            }
            System.out.println("Incorrect value.");
        }
    }

    public void doTransfer() {
        if (card != null) {
            System.out.println("Transfer");
            System.out.println("Enter card number:");
            String cardNumber = scanner.next();

            if (isCardValid(cardNumber)) {
                System.out.println("Enter how much money you want to transfer:");

                if (!scanner.hasNextInt()) {
                    System.out.println("Incorrect value.");
                    scanner.nextLine();
                } else {
                    int amount = scanner.nextInt();

                    if (isAmountValid(amount)) {
                        String debitAccountNumber = card.getAccount().getNumber();
                        String creditAccountNumber = ledger.getCard(cardNumber).getAccount().getNumber();
                        if (ledger.registerTransfer(debitAccountNumber, creditAccountNumber, amount)) {
                            card = ledger.getCard(card.getId());
                            System.out.println("Success!");
                        } else {
                            System.out.println("Something was wrong!");
                        }
                    }
                }
            }
        }
    }

    private boolean isCardValid(String cardNumber) {
        boolean isValid = false;

        if (!Card.isCardNumberCorrect(cardNumber) || cardNumber.equals(card.getNumber())) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        } else if (ledger.getCard(cardNumber) == null) {
            System.out.println("Such a card does not exist.");
        } else {
            isValid = true;
        }
        return isValid;
    }

    private boolean isAmountValid(int amount) {
        boolean isValid = false;

        if (amount < 0) {
            System.out.println("Incorrect value.");
        } else if (amount > card.getAccount().getBalance()) {
            System.out.println("Not enough money!");
        } else {
            isValid = true;
        }
        return isValid;
    }

}
