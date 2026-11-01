package uspace.infra.persistence.inMemory;

import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;
import uspace.domain.account.Username;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {

    private static final Map<Username, Account> accounts = new HashMap<>();

    @Override
    public Account findByUsername(Username username) { return accounts.get(username); }

    @Override
    public void save(Account account) { accounts.put(account.getUsername(), account); }

    @Override
    public boolean existsByUsername(Username username) {
        return accounts.containsKey(username);
    }
}
