package banking.menu;

import java.util.Scanner;

abstract class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    private boolean leaveMenu = false;
    private static boolean exitProgram = false;

    protected void doLeave() { leaveMenu = true; }
    protected void doExit() { exitProgram = true; }

    protected abstract void showMenu();
    protected abstract void doAction(int action);

    public final void run() {
        int action;
        do {
            showMenu();
            try {
                action = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Incorrect value.");
                continue;
            }
            doAction(action);
        } while (!leaveMenu && !exitProgram);
    }
}
