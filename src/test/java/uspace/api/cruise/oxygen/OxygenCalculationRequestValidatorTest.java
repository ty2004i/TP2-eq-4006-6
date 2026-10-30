package uspace.api.cruise.oxygen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OxygenCalculationRequestValidatorTest {

    private OxygenCalculationRequestValidator validator;

    private static final String INVALID_CRUISE_ID = null;

    private static final String CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";

    private static final String CALCULATION_DATE_TIME = "2085-01-31T08:00";

    private static final String INVALID_CALCULATION_DATE_TIME = null;

    @BeforeEach
    void setUp() {
        validator = new OxygenCalculationRequestValidator();
    }

    @Test
    void givenValidRequest_whenValidate_thenDoNotThrowException() {
        OxygenCalculationRequest request = new OxygenCalculationRequest(
                CRUISE_ID,
                CALCULATION_DATE_TIME
        );

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void givenMissingCruiseId_whenValidate_thenThrowMissingParameterException() {

        OxygenCalculationRequest request = new OxygenCalculationRequest(
               INVALID_CRUISE_ID, CALCULATION_DATE_TIME
        );

        assertThrows(MissingParameterException.class, () -> validator.validate(request));
    }

    @Test
    void givenMissingCalculationDateTime_whenValidate_thenThrowMissingParameterException() {

        OxygenCalculationRequest request = new OxygenCalculationRequest(
                CRUISE_ID,
                INVALID_CALCULATION_DATE_TIME
        );

        assertThrows(MissingParameterException.class, () -> validator.validate(request));
    }
}
