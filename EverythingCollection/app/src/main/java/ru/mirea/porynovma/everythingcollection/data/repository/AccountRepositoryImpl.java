package ru.mirea.porynovma.everythingcollection.data.repository;

import ru.mirea.porynovma.everythingcollection.domain.models.Account;

public class AccountRepositoryImpl {
    public Account getAccountInfo(int id) {
        return new Account();
    }
    public Account validateAccount(String name, String password) {
        return new Account();
    }
    public void createAccount(String name, String password) {

    }
}
