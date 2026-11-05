package uspace.application.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.account.dtos.AccountCreationRequestDto;
import uspace.application.account.dtos.AccountResponseDto;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;
import uspace.domain.account.HomePlanetName;
import uspace.domain.account.Username;
import uspace.domain.account.exceptions.AccountNotFoundException;
import uspace.domain.account.exceptions.UsernameAlreadyExistsException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @Mock
    private AccountAssembler accountAssembler;

    private final String USER_NAME = "johnDoe";

    private final String HOME_PLANET = "Earth";

    private final Username username = new Username(USER_NAME);

    private final HomePlanetName homePlanetName = new HomePlanetName(HOME_PLANET);

    @BeforeEach
    void setup() {
        accountService = new AccountService(accountRepository, accountAssembler);
    }

    @Test
    void givenNewAccount_whenCreateAccount_thenSavesToRepository() {
        AccountCreationRequestDto requestDto = new AccountCreationRequestDto(USER_NAME, HOME_PLANET);
        when(accountRepository.existsByUsername(username)).thenReturn(false);

        accountService.createAccount(requestDto);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertEquals(USER_NAME, captor.getValue().getUsername().getValue());
        assertEquals(HOME_PLANET, captor.getValue().getHomePlanetName().getValue());
    }

    @Test
    void givenExistingUsername_whenCreateAccount_thenThrowsException() {
        AccountCreationRequestDto requestDto = new AccountCreationRequestDto(USER_NAME, HOME_PLANET);

        when(accountRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> accountService.createAccount(requestDto));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void givenExistingAccount_whenGetByUsername_thenReturnDto() {
        Account account = new Account(username, homePlanetName);
        AccountResponseDto expectedDto = new AccountResponseDto(USER_NAME, HOME_PLANET, Collections.emptyList());

        when(accountRepository.findByUsername(username)).thenReturn(account);
        when(accountAssembler.toDto(account)).thenReturn(expectedDto);

        AccountResponseDto resultDto = accountService.getAccountByUsername(USER_NAME);

        assertEquals(expectedDto, resultDto);
    }

    @Test
    void givenUnknownUsername_whenGetByUsername_thenThrowsException() {
        when(accountRepository.findByUsername(username)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByUsername(USER_NAME));
    }

}
