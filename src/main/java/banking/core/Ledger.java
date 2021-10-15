package banking.core;

import banking.entity.Account;
import banking.entity.Card;
import banking.repository.AccountsRepository;
import banking.repository.CardsRepository;
import banking.repository.Repository;

class Ledger {
    protected static final AccountsRepository accounts = AccountsRepository.getInstance();
    protected static final CardsRepository cards = CardsRepository.getInstance();

    public Card createAccountAndCard() {
        Account account = accounts.createAccount();
        return cards.createCard(account);
    }

    public Card getCard(String number) {
        return cards.findCard(number);
    }
    public Card getCard(int id) {
        return cards.findCard(id);
    }

    public Account getAccount(String number) {
        return accounts.findAccount(number);
    }
    public Account getAccount(int id) {
        return accounts.findAccount(id);
    }

    public boolean registerIncome(String accountNumber, int amount) {
        Account account = accounts.findAccount(accountNumber);
        boolean isUpdated = false;

        if (account != null) {
            int balance = account.getBalance();
            balance += amount;
            account.setBalance(balance);

            Repository.beginTransaction();
            isUpdated = accounts.updateAccountBalance(account);

            if (!isUpdated) {
                Repository.rollback();
            }
            Repository.endTransaction();
        }
        return isUpdated;
    }

    public boolean registerTransfer(String debitAccountNumber, String creditAccountNumber, int amount) {
        Account debitAccount = accounts.findAccount(debitAccountNumber);
        Account creditAccount = accounts.findAccount(creditAccountNumber);
        boolean isUpdated = false;

        if (debitAccount != null && creditAccount != null) {
            int balance = debitAccount.getBalance();
            balance -= amount;
            debitAccount.setBalance(balance);
            balance = creditAccount.getBalance();
            balance += amount;
            creditAccount.setBalance(balance);

            Repository.beginTransaction();
            isUpdated = accounts.updateAccountBalance(debitAccount);
            isUpdated = isUpdated && accounts.updateAccountBalance(creditAccount);

            if (!isUpdated) {
                Repository.rollback();
            }
            Repository.endTransaction();
        }
        return isUpdated;
    }

    public boolean deleteAccountAndCard(String cardNumber) {
        Card card = cards.findCard(cardNumber);
        boolean isDeleted = false;

        if (card != null) {
            Repository.beginTransaction();
            isDeleted = cards.deleteCard(card.getId());
            isDeleted = isDeleted && accounts.deleteAccount(card.getAccount().getId());

            if (!isDeleted) {
                Repository.rollback();
            }
            Repository.endTransaction();
        }
        return isDeleted;
    }
}
