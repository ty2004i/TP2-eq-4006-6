package uspace.api.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.api.acount.AccountCreationRequestValidator;
import uspace.application.account.dtos.AccountCreationRequestDto;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountCreationRequestValidatorTest {

    private AccountCreationRequestValidator validator;

    private AccountCreationRequestDto requestDto;

    private final String USER_NAME = "johnDoe";

    private final String HOME_PLANET = "Earth";


    @BeforeEach
    void setup() {
        validator = new AccountCreationRequestValidator();

        requestDto = new AccountCreationRequestDto(USER_NAME, HOME_PLANET);
    }

    @Test
    void givenValidRequest_whenValidate_thenNoExceptionThrown() {
        assertDoesNotThrow(() -> validator.validate(requestDto));
    }

    @Test
    void givenMissingUsername_whenValidate_thenThrowsMissingParameterException() {
        requestDto.username = null;

        assertThrows(MissingParameterException.class, () -> validator.validate(requestDto));
    }

    @Test
    void givenMissingHomePlanetName_whenValidate_thenThrowsMissingParameterException() {
        requestDto.homePlanetName = null;

        assertThrows(MissingParameterException.class, () -> validator.validate(requestDto));
    }

}
