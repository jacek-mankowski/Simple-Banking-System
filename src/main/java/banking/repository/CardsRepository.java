package banking.repository;

import banking.entity.Account;
import banking.entity.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CardsRepository extends Repository {
    private static final CardsRepository instance = new CardsRepository();

    public static CardsRepository getInstance() {
        return instance;
    }

    public Card findCard(String number) {
        String sql = "SELECT * FROM card where number = ?;";
        Card card = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (Objects.equals(number, resultSet.getString("number"))) {
                    int id = resultSet.getInt("id");
                    String pin = resultSet.getString("pin");
                    int accountId = resultSet.getInt("account_id");
                    Account account = AccountsRepository.getInstance().findAccount(accountId);

                    card = new Card(id, number, pin, account);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return card;
    }

    public Card findCard(int id) {
        String sql = "SELECT * FROM card where id = ?;";
        Card card = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String number = resultSet.getString("number");
                String pin = resultSet.getString("pin");
                int accountId = resultSet.getInt("account_id");
                Account account = AccountsRepository.getInstance().findAccount(accountId);

                card = new Card(id, number, pin, account);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return card;
    }

    public Card createCard(Account account) {
        if (account != null) {
            Card card = new Card(account);

            if (card != null && findCard(card.getNumber()) == null) {
                if (insertCard(card)) {
                    card = findCard(card.getNumber());
                    return card;
                }
            }
        }
        return null;
    }

    private boolean insertCard(Card card) {
        String sql = "INSERT INTO card (number, pin, account_id) VALUES (?, ?, ?);";

        if (card != null) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, card.getNumber());
                statement.setString(2, card.getPin());
                statement.setInt(3, card.getAccount().getId());
                return 0 < statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean deleteCard(String number) {
        String sql = "DELETE FROM card where number = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            return 0 < statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean deleteCard(int id) {
        String sql = "DELETE FROM card where id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return 0 < statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateCardPin(Card card) {
        String sql = "UPDATE card SET pin = ? WHERE id = ?;";

        if (card != null) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, card.getPin());
                statement.setInt(2, card.getId());
                return 0 < statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
}
