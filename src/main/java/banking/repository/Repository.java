package banking.repository;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Repository {
    protected static SQLiteDataSource dataSource = null;
    protected static Connection connection = null;

    public static void initDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;

        try {
            dataSource = new SQLiteDataSource();
            dataSource.setUrl(url);
            connection = dataSource.getConnection();
            if (connection != null) {
                createTables();
            }
        } catch (SQLException e) {
            closeDatabase();
            System.out.println(e.getMessage());
        }
    }

    public static boolean isInit() {
        return connection != null;
    }

    public static void closeDatabase() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection = null;
    }

    public static void beginTransaction() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commit() {
        endTransaction();
    }

    public static void rollback() {
        try {
            connection.rollback();
            endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void endTransaction() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables() {
        String createAccountSQL = "CREATE TABLE IF NOT EXISTS account (\n" +
                " id      INTEGER NOT NULL UNIQUE,\n" +
                " number  TEXT NOT NULL UNIQUE,\n" +
                " balance INTEGER NOT NULL DEFAULT 0,\n" +
                " PRIMARY KEY(id AUTOINCREMENT)\n" +
                ");";

        String createCardSQL = "CREATE TABLE IF NOT EXISTS card (\n" +
                " id     INTEGER NOT NULL,\n" +
                " number TEXT NOT NULL,\n" +
                " pin    TEXT,\n" +
                " account_id INTEGER NOT NULL,\n" +
                " PRIMARY KEY(id AUTOINCREMENT),\n" +
                " FOREIGN KEY(account_id) REFERENCES account(id)\n" +
                ");";

        String createIdxAccountSQL = "CREATE INDEX IF NOT EXISTS idx_account_id ON account (\n" +
                " id \n" +
                ");";

        String createIdxCardSQL = "CREATE INDEX IF NOT EXISTS idx_card_id ON account (\n" +
                " id \n" +
                ");";

        String[] sqls = {createAccountSQL, createCardSQL, createIdxAccountSQL, createIdxCardSQL};

        beginTransaction();
        try (Statement statement = connection.createStatement()) {
            for (String sql : sqls) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            rollback();
            System.out.println(e.getMessage());
        }
        endTransaction();
    }
}
