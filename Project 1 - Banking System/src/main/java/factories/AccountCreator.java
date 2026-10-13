package factories;

import model.Account;

sealed public interface AccountCreator permits SavingsAccountCreator, CurrentAccountCreator
{
    Account createAccount(long countAccountNumber, String password);
}
