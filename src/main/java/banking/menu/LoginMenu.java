package banking.menu;

import banking.core.Bank;

public class LoginMenu extends Menu {
    private Bank bank;

    public LoginMenu(Bank bank) {
        this.bank = bank;
        this.bank.start();
    }

    private enum MenuItem {
        CREATE(1, "Create an account"),
        LOG_IN(2, "Log into account"),
        EXIT(0, "Exit");

        private final int number;
        private final String label;

        MenuItem(int number, String label) {
            this.number = number;
            this.label = label;
        }

        public int getNumber() {
            return number;
        }

        public String getLabel() {
            return label;
        }

        public static MenuItem valueOf(int number) {
            for (MenuItem item : MenuItem.values()) {
                if (item.getNumber() == number) {
                    return item;
                }
            }
            return null;
        }
    }


    protected void showMenu() {
        System.out.println("\n");
        for (MenuItem item : MenuItem.values()) {
            System.out.println(item.getNumber() + ". " + item.getLabel());
        }
    }

    protected void doAction(int action) {
        MenuItem item = MenuItem.valueOf(action);

        if (item != null) {
            switch (item) {
                case CREATE:
                    bank.createAccount();
                    break;
                case LOG_IN:
                    if (bank.logIntoAccount()) {
                        new AccountMenu(bank).run();
                    }
                    break;
                case EXIT:
                    System.out.println("Bye!");
                    doExit();
                    break;
            }
        }
    }
}
