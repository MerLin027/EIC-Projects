package factory;

import model.Account;

/**
 * Factory for accounts (Factory Method pattern). Each implementation creates one account type,
 * so callers choose a creator without depending on the concrete account class.
 */

sealed public interface AccountCreator permits SavingsAccountCreator, CurrentAccountCreator
{
    /**
     * Creates a new account. It is not stored anywhere; saving it is the caller's job.
     *
     * @param accountNumber the number the new account gets
     * @param password the account's login password
     * @return the new account
     */
    Account createAccount(long countAccountNumber, String password);
}
