package uspace.application.account;

import jakarta.inject.Inject;
import uspace.application.account.dtos.AccountCreationRequestDto;
import uspace.application.account.dtos.AccountResponseDto;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;
import uspace.domain.account.HomePlanetName;
import uspace.domain.account.Username;
import uspace.domain.account.exceptions.AccountNotFoundException;
import uspace.domain.account.exceptions.UsernameAlreadyExistsException;

public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountAssembler accountAssembler;

    @Inject
    public AccountService(AccountRepository accountRepository, AccountAssembler accountAssembler) {
        this.accountRepository = accountRepository;
        this.accountAssembler = accountAssembler;
    }

    public void createAccount(AccountCreationRequestDto requestDto) {
        Username username = new Username(requestDto.username);
        HomePlanetName homePlanetName = new HomePlanetName(requestDto.homePlanetName);

        if (accountRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException();
        }

        Account account = new Account(username, homePlanetName);
        accountRepository.save(account);
    }

    public AccountResponseDto getAccountByUsername(String usernameStr) {
        Username username = new Username(usernameStr);

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new AccountNotFoundException();
        }

        return accountAssembler.toDto(account);

    }
}
