package uspace.api.cruise.oxygen;

import uspace.domain.exceptions.MissingParameterException;

public class OxygenCalculationRequestValidator {
    public void validate(OxygenCalculationRequest oxygenCalculationRequest) {
        if (oxygenCalculationRequest.cruiseId == null) {
            throw new MissingParameterException("cruiseId");
        }

        if (oxygenCalculationRequest.calculationDateTime == null) {
            throw new MissingParameterException("calculationDateTime");
        }
    }
}
