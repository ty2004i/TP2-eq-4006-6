package uspace.domain.account;

public interface AccountRepository {
    Account findByUsername(Username username);

    void save(Account account);

    boolean existsByUsername(Username username);
}
