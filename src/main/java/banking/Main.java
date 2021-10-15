package banking;

import banking.core.Bank;
import banking.repository.Repository;

public class Main {

    private static String getDBName(String[] args) {
        String dbName = "card.s3db";

        for (int i = 1; i < args.length; i += 2) {
            if ("-fileName".equals(args[i - 1])) {
                dbName = args[i];
                break;
            }
        }
        return dbName;
    }

    public static void main(String[] args) {
        String dbName = getDBName(args);

        Repository.initDatabase(dbName);
        if(Repository.isInit()) {
            Bank.getInstance().start();
        }
        Repository.closeDatabase();
    }
}