package uspace.api.cruise.hyperdrive;

import uspace.application.cruise.hyperdrive.dtos.NewHyperdriveModuleDto;
import uspace.domain.exceptions.InvalidParameterException;
import uspace.domain.exceptions.MissingParameterException;

public class NewHyperdriveModuleValidator {
    public void validate(NewHyperdriveModuleDto newHyperdriveModuleDto) {
        if (newHyperdriveModuleDto.id == null) {
            throw new MissingParameterException("id");
        }
        if (newHyperdriveModuleDto.powerLevel == 0) {
            throw new MissingParameterException("powerLevel");
        }
        if (newHyperdriveModuleDto.powerLevel < 0) {
            throw new InvalidParameterException("Power level must be a positive number");
        }
        if (newHyperdriveModuleDto.activationDays == 0) {
            throw new MissingParameterException("activationDays");
        }
        if (newHyperdriveModuleDto.activationDays < 0) {
            throw new InvalidParameterException("Activation days must be a positive number");
        }
        if (newHyperdriveModuleDto.activationDate == null) {
            throw new MissingParameterException("activationDate");
        }
    }
}
