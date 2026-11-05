package uspace.api.account;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.acount.AccountCreationRequestValidator;
import uspace.api.acount.AccountResource;
import uspace.application.account.AccountService;
import uspace.application.account.dtos.AccountCreationRequestDto;
import uspace.application.account.dtos.AccountResponseDto;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountResourceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private AccountCreationRequestValidator validator;

    private AccountResource accountResource;

    private final String USER_NAME = "johnDoe";

    private final String HOME_PLANET = "Earth";

    @BeforeEach
    void setup() {
        accountResource = new AccountResource(accountService, validator);
    }

    @Test
    void whenCreateAccount_thenReturnCreated() {
        AccountCreationRequestDto requestDto = new AccountCreationRequestDto(USER_NAME, HOME_PLANET);

        Response response = accountResource.createAccount(requestDto);

        verify(validator).validate(requestDto);
        verify(accountService).createAccount(requestDto);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    void whenGetAccount_thenReturnOkWithAccountPayload() {
        AccountResponseDto responseDto = new AccountResponseDto(USER_NAME, HOME_PLANET, Collections.emptyList());
        when(accountService.getAccountByUsername(USER_NAME)).thenReturn(responseDto);

        Response response = accountResource.getAccount(USER_NAME);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseDto, response.getEntity());
    }
}
