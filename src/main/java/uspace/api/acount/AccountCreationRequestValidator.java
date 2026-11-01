package uspace.api.acount;

import uspace.application.account.dtos.AccountCreationRequestDto;
import uspace.domain.exceptions.MissingParameterException;

public class AccountCreationRequestValidator {

    public void validate(AccountCreationRequestDto requestDto) {
        if (requestDto == null) {
            throw new MissingParameterException("username");
        }
        if (requestDto.username == null || requestDto.username.trim().isEmpty()) {
            throw new MissingParameterException("username");
        }
        if (requestDto.homePlanetName == null || requestDto.homePlanetName.trim().isEmpty()) {
            throw new MissingParameterException("homePlanetName");
        }
    }
}
