package ru.mirea.porynovma.everythingcollection.domain.usecases;

import ru.mirea.porynovma.everythingcollection.domain.models.Account;
import ru.mirea.porynovma.everythingcollection.domain.repository.AccountRepository;

public class AccountRegisterUseCase {
    private AccountRepository repo;
    public AccountRegisterUseCase(AccountRepository repo) { this.repo = repo; }
    public Account execute(String name, String password) {
        return this.repo.createAccount(name, password);
    }
}
