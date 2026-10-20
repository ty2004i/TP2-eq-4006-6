package uspace.api.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.application.cruise.hyperdrive.dtos.NewHyperdriveModuleDto;
import uspace.domain.exceptions.InvalidParameterException;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NewHyperdriveModuleValidatorTest {
    private static final String ANY_ID = "AAA";
    private static final int ANY_POSITIVE_POWER_LEVEL = 4;
    private static final int ANY_POSITIVE_ACTIVATION_DAYS = 80;
    private static final String ANY_ACTIVATION_DATE = "2084-04-08";
    private NewHyperdriveModuleValidator newHyperdriveModuleValidator;

    @BeforeEach
    void createHyperdriveModuleValidator() {
        newHyperdriveModuleValidator = new NewHyperdriveModuleValidator();
    }

    @Test
    void givenNewHyperdriveModuleWithoutId_whenValidate_thenThrowMissingParameterException() {
        NewHyperdriveModuleDto newHyperdriveModuleDto = new NewHyperdriveModuleDto(null, ANY_POSITIVE_POWER_LEVEL, ANY_POSITIVE_ACTIVATION_DAYS, ANY_ACTIVATION_DATE);

        Executable validate = () -> newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenNewHyperdriveModuleWithoutPowerLevel_whenValidate_thenThrowMissingParameterException() {
        NewHyperdriveModuleDto newHyperdriveModuleDto = new NewHyperdriveModuleDto(ANY_ID, 0, ANY_POSITIVE_ACTIVATION_DAYS, ANY_ACTIVATION_DATE);

        Executable validate = () -> newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenNewHyperdriveModuleWithNegativePowerLevel_whenValidate_thenThrowInvalidParameterException() {
        int anyNegativePowerLevel = -1;
        NewHyperdriveModuleDto newHyperdriveModuleDto = new NewHyperdriveModuleDto(ANY_ID, anyNegativePowerLevel, ANY_POSITIVE_ACTIVATION_DAYS, ANY_ACTIVATION_DATE);

        Executable validate = () -> newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        assertThrows(InvalidParameterException.class, validate);
    }

    @Test
    void givenNewHyperdriveModuleWithoutActivationDays_whenValidate_thenThrowMissingParameterException() {
        NewHyperdriveModuleDto newHyperdriveModuleDto = new NewHyperdriveModuleDto(ANY_ID, ANY_POSITIVE_POWER_LEVEL, 0, ANY_ACTIVATION_DATE);

        Executable validate = () -> newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenNewHyperdriveModuleWithoutActivationDays_whenValidate_thenThrowInvalidParameterException() {
        int anyNegativeActivationDays = -8;
        NewHyperdriveModuleDto newHyperdriveModuleDto = new NewHyperdriveModuleDto(ANY_ID, ANY_POSITIVE_POWER_LEVEL, anyNegativeActivationDays, ANY_ACTIVATION_DATE);

        Executable validate = () -> newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        assertThrows(InvalidParameterException.class, validate);
    }

    @Test
    void givenNewHyperdriveModuleWithoutActivationDate_whenValidate_thenThrowMissingParameterException() {
        NewHyperdriveModuleDto newHyperdriveModuleDto = new NewHyperdriveModuleDto(ANY_ID, ANY_POSITIVE_POWER_LEVEL, ANY_POSITIVE_ACTIVATION_DAYS, null);

        Executable validate = () -> newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenNewHyperdriveModuleWithAllParametersPresentAndValid_whenValidate_thenDoNothing() {
        NewHyperdriveModuleDto newHyperdriveModuleDto = new NewHyperdriveModuleDto(ANY_ID, ANY_POSITIVE_POWER_LEVEL, ANY_POSITIVE_ACTIVATION_DAYS, ANY_ACTIVATION_DATE);

        Executable validate = () -> newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        assertDoesNotThrow(validate);
    }
}