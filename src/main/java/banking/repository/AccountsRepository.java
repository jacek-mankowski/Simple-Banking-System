package banking.repository;

import banking.entity.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;

public class AccountsRepository extends Repository {
    private static final AccountsRepository instance = new AccountsRepository();
    public static AccountsRepository getInstance() {
        return instance;
    }

    public Account findAccount(String number) {
        String sql = "SELECT * FROM account where number = ?;";
        Account account = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (Objects.equals(number, resultSet.getString("number"))) {
                    int id = resultSet.getInt("id");
                    int balance = resultSet.getInt("balance");
                    account = new Account(id, number, balance);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    public Account findAccount(int id) {
        String sql = "SELECT * FROM account where id = ?;";
        Account account = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String number = resultSet.getString("number");
                int balance = resultSet.getInt("balance");
                account = new Account(id, number, balance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    public Account createAccount() {
        String number;

        do {
            number = String.format("%09d", Math.abs(new Random().nextLong() % 1_000_000_000L));
        } while (findAccount(number) != null);

        Account account = new Account(number);
        if (insertAccount(account)) {
            account = findAccount(number);
            return account;
        }
        return null;
    }

    private boolean insertAccount(Account account) {
        String sql = "INSERT INTO account (number, balance) VALUES (?, ?);";

        if (account != null) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, account.getNumber());
                statement.setInt(2, account.getBalance());
                return 0 < statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean deleteAccount(String number) {
        String sql = "DELETE FROM account where number = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            return 0 < statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean deleteAccount(int id) {
        String sql = "DELETE FROM account where id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return 0 < statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateAccountBalance(Account account) {
        String sql = "UPDATE account SET balance = ? WHERE id = ?;";

        if (account != null) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, account.getBalance());
                statement.setInt(2, account.getId());
                return 0 < statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

}
