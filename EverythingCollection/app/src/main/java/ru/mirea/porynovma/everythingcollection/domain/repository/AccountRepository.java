package ru.mirea.porynovma.everythingcollection.domain.repository;

import ru.mirea.porynovma.everythingcollection.domain.models.Account;

public interface AccountRepository {
    public Account getAccountInfo(int id);
    public Account validateAccount(String name, String password);
    public Account createAccount(String name, String password);
}
