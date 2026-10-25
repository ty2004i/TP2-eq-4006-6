package uspace.domain.cruise.hyperdrive.module.id;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HyperdriveModuleIdValidatorHYStandardTest {
    private HyperdriveModuleIdValidatorHYStandard hyperdriveModuleIdValidatorHYStandard;

    @BeforeEach
    void createHyperdriveModuleIdValidatorHYStandard() {
        hyperdriveModuleIdValidatorHYStandard = new HyperdriveModuleIdValidatorHYStandard();
    }

    @Test
    void givenIdNotBeginningWithHY_whenValidate_thenReturnFalse() {
        HyperdriveModuleId anyIdNotBeginningWithHY = new HyperdriveModuleId("BA-300-V");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyIdNotBeginningWithHY);

        assertFalse(isValid);
    }

    @Test
    void givenIdBeginningWithHYLowerCase_whenValidate_thenReturnFalse() {
        HyperdriveModuleId anyIdBeginningWithHYLowerCase = new HyperdriveModuleId("hy-77-V");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyIdBeginningWithHYLowerCase);

        assertFalse(isValid);
    }

    @Test
    void givenIdWithoutNumbers_whenValidate_thenReturnFalse() {
        HyperdriveModuleId anyIdWithoutNumbers = new HyperdriveModuleId("HY-V");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyIdWithoutNumbers);

        assertFalse(isValid);
    }

    @Test
    void givenIdNumberBiggerThan999_whenValidate_thenReturnFalse() {
        HyperdriveModuleId anyIdNumberBiggerThan999 = new HyperdriveModuleId("HY-1000-V");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyIdNumberBiggerThan999);

        assertFalse(isValid);
    }

    @Test
    void givenIdFinishingWithMoreThanOneUppercaseLetter_whenValidate_thenReturnFalse() {
        HyperdriveModuleId anyIdFinishingWithMoreThanOneUppercaseLetter = new HyperdriveModuleId("HY-500-DD");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyIdFinishingWithMoreThanOneUppercaseLetter);

        assertFalse(isValid);
    }

    @Test
    void givenIdNotFinishingWithALetter_whenValidate_thenReturnFalse() {
        HyperdriveModuleId anyIdNotFinishingWithALetter = new HyperdriveModuleId("HY-122");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyIdNotFinishingWithALetter);

        assertFalse(isValid);
    }

    @Test
    void givenIdFinishingWithALowerCaseLetter_whenValidate_thenReturnFalse() {
        HyperdriveModuleId anyIdFinishingWithALowerCaseLetter = new HyperdriveModuleId("HY-500-f");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyIdFinishingWithALowerCaseLetter);

        assertFalse(isValid);
    }

    @Test
    void givenIdWithoutHyphen_whenValidate_thenReturnFalse() {
        HyperdriveModuleId anyIdWithoutHyphen = new HyperdriveModuleId("HY77V");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyIdWithoutHyphen);

        assertFalse(isValid);
    }

    @Test
    void givenValidFormat_whenValidate_thenReturnTrue() {
        HyperdriveModuleId anyValidFormatId = new HyperdriveModuleId("HY-77-V");

        boolean isValid = hyperdriveModuleIdValidatorHYStandard.isValid(anyValidFormatId);

        assertTrue(isValid);
    }

}
