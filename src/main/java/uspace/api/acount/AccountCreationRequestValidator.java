package uspace.api.acount;

import uspace.application.account.dtos.AccountCreationRequestDto;
import uspace.domain.exceptions.MissingParameterException;

public class AccountCreationRequestValidator {

    public void validate(AccountCreationRequestDto requestDto) {
        if (requestDto.username == null) {
            throw new MissingParameterException("username");
        }
        if (requestDto.homePlanetName == null) {
            throw new MissingParameterException("homePlanetName");
        }
    }
}
