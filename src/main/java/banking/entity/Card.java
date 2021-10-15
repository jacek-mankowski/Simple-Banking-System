package banking.entity;

import java.util.Random;

public class Card {
    private final int id;
    private final String number;
    private String pin;
    private final Account account;

    private static final String CARD_NUMBER_PREFIX = "400000";

    public Card(Account account) {
        this.id = 0;
        this.account = account;
        int digit = countControlDigit(CARD_NUMBER_PREFIX + account.getNumber());
        this.number = CARD_NUMBER_PREFIX + account.getNumber() + digit;
        this.pin = String.format("%04d", new Random().nextInt(10000));
    }

    public Card(String number, String pin, Account account) {
        this(0, number, pin, account);
    }

    public Card(int id, String number, String pin, Account account) {
        this.id = id;
        this.number = number;
        this.pin = pin;
        this.account = account;
    }

    private static int countControlDigit(String number) {
        int sum = 0;

        for (int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, i + 1));
            if (i % 2 == 0) {
                digit *= 2;
            }
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }

    public static boolean isCardNumberCorrect(String number) {
        boolean isCorrect = number.length() > 0;
        if (isCorrect) {
            try {
                int digit = Integer.parseInt(number.substring(number.length() - 1));
                int countedDigit = countControlDigit(number.substring(0, number.length() - 1));

                isCorrect = isCorrect && digit == countedDigit;
            } catch (Exception e) {
                isCorrect = false;
            }
        }
        return isCorrect;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Account getAccount() {
        return account;
    }
}
