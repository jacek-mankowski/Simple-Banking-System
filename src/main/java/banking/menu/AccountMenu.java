package banking.menu;

import banking.core.Bank;

class AccountMenu extends Menu {
    private Bank bank;

    public AccountMenu(Bank bank) {
        this.bank = bank;
    }

    private enum MenuItem {
        BALANCE(1, "Balance"),
        INCOME(2, "Add income"),
        TRANSFER(3, "Do transfer"),
        CLOSE(4, "Close account"),
        LOG_OUT(5, "Log out"),
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
                case BALANCE:
                    bank.getBalance();
                    break;
                case INCOME:
                    bank.addIncome();
                    break;
                case TRANSFER:
                    bank.doTransfer();
                    break;
                case CLOSE:
                    bank.closeAccount();
                    doLeave();
                    break;
                case LOG_OUT:
                    bank.logOutFromAccount();
                    doLeave();
                    break;
                case EXIT:
                    System.out.println("Bye!");
                    doExit();
                    break;
            }
        }
    }
}
