package banking.entity;

public class Account {
    private final int id;
    private int balance;
    private final String number;

    public Account(String number) {
        this(number, 0);
    }

    public Account(String number, int balance) {
        this(0, number, balance);
    }

    public Account(int id, String number, int balance) {
        this.id = id;
        this.number = number;
        this.balance = balance;
    }

    public int getId() { return id;}

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }
}
