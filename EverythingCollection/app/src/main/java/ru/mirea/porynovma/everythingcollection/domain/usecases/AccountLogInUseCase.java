package ru.mirea.porynovma.everythingcollection.domain.usecases;

import ru.mirea.porynovma.everythingcollection.domain.models.Account;
import ru.mirea.porynovma.everythingcollection.domain.repository.AccountRepository;

public class AccountLogInUseCase {
    private AccountRepository repo;
    public AccountLogInUseCase(AccountRepository repo) {
        this.repo = repo;
    }

    public Account execute(String name, String password) {
        return this.repo.validateAccount(name, password);
    }
}
