package uspace.domain.cruise.hyperdrive.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HyperdriveModuleTest {
    private static final HyperdriveModuleId ANY_ID = new HyperdriveModuleId("AAA");
    private static final HyperdrivePowerLevel ANY_POWER_LEVEL = new HyperdrivePowerLevel(4);
    private static final LocalDateTime ANY_ACTIVATION_LOCAL_DATE_TIME = LocalDateTime.of(2003, 6, 6, 1, 0, 0);
    private static final LocalDateTime ANY_DEACTIVATION_LOCAL_DATE_TIME = LocalDateTime.of(2003, 6, 10, 1, 0, 0);
    private static final CruiseDateTime ANY_DATE_TIME_DURING_ACTIVATION_PERIOD = new CruiseDateTime(LocalDateTime.of(2003, 6, 8, 1, 0, 0));
    private static final CruiseDateTime ANY_ACTIVATION_DATE_TIME = new CruiseDateTime(ANY_ACTIVATION_LOCAL_DATE_TIME);
    private static final CruiseDateTime ANY_DEACTIVATION_DATE_TIME = new CruiseDateTime(ANY_DEACTIVATION_LOCAL_DATE_TIME);
    private static final CruiseDateTime ANY_DATE_TIME_BEFORE_ACTIVATION = new CruiseDateTime(ANY_ACTIVATION_LOCAL_DATE_TIME.minusDays(1));
    private static final CruiseDateTime ANY_DATE_TIME_AFTER_DEACTIVATION = new CruiseDateTime(ANY_DEACTIVATION_LOCAL_DATE_TIME.plusDays(1));
    private HyperdriveModule hyperdriveModule;

    @BeforeEach
    void createHyperdriveModule() {
        hyperdriveModule = new HyperdriveModule(ANY_ID, ANY_POWER_LEVEL, ANY_ACTIVATION_DATE_TIME, ANY_DEACTIVATION_DATE_TIME);
    }

    @Test
    void givenHyperdriveModuleWithDifferentId_whenValidateSameId_thenReturnFalse() {
        HyperdriveModuleId anyDifferentId = new HyperdriveModuleId("123");
        HyperdriveModule anOtherHyperdriveModuleWithDifferentId = new HyperdriveModule(anyDifferentId, ANY_POWER_LEVEL, ANY_ACTIVATION_DATE_TIME, ANY_DEACTIVATION_DATE_TIME);

        boolean hasSameId = hyperdriveModule.hasSameIdThan(anOtherHyperdriveModuleWithDifferentId);

        assertFalse(hasSameId);
    }

    @Test
    void givenHyperdriveModuleWithSameId_whenValidateSameId_thenReturnTrue() {
        HyperdriveModuleId anySameId = new HyperdriveModuleId("AAA");
        HyperdriveModule anOtherHyperdriveModuleWithSameId = new HyperdriveModule(anySameId, ANY_POWER_LEVEL, ANY_ACTIVATION_DATE_TIME, ANY_DEACTIVATION_DATE_TIME);

        boolean hasSameId = hyperdriveModule.hasSameIdThan(anOtherHyperdriveModuleWithSameId);

        assertTrue(hasSameId);
    }

    @Test
    void givenStartDateTimeAfterActivation_whenValidateActivationIsBetweenStartAndEnd_thenReturnFalse() {
        CruiseDateTime anyDateTimeAfterActivation = new CruiseDateTime(ANY_ACTIVATION_LOCAL_DATE_TIME.plusHours(1));

        boolean isActivationBetween = hyperdriveModule.isActivationBetween(anyDateTimeAfterActivation, ANY_DATE_TIME_AFTER_DEACTIVATION);

        assertFalse(isActivationBetween);
    }

    @Test
    void givenEndDateTimeBeforeDeactivation_whenValidateActivationIsBetweenStartAndEnd_thenReturnFalse() {
        CruiseDateTime anyDateTimeBeforeDeactivation = new CruiseDateTime(ANY_DEACTIVATION_LOCAL_DATE_TIME.minusHours(1));

        boolean isActivationBetween = hyperdriveModule.isActivationBetween(ANY_DATE_TIME_BEFORE_ACTIVATION, anyDateTimeBeforeDeactivation);

        assertFalse(isActivationBetween);
    }

    @Test
    void givenActivationBetweenStartAndEnd_whenValidateActivationIsBetweenStartAndEnd_thenReturnFalse() {
        boolean isActivationBetween = hyperdriveModule.isActivationBetween(ANY_DATE_TIME_BEFORE_ACTIVATION, ANY_DATE_TIME_AFTER_DEACTIVATION);

        assertTrue(isActivationBetween);
    }

    @Test
    void givenHyperdriveModuleActivationDuringActivationPeriod_whenValidateSameActivationPeriod_thenReturnTrue() {
        HyperdriveModule anOtherHyperdriveModuleWithActivationDuringActivationPeriod = new HyperdriveModule(ANY_ID, ANY_POWER_LEVEL, ANY_DATE_TIME_DURING_ACTIVATION_PERIOD, ANY_DATE_TIME_AFTER_DEACTIVATION);

        boolean hasSameActivationPeriod = hyperdriveModule.hasSameActivationPeriodThan(anOtherHyperdriveModuleWithActivationDuringActivationPeriod);

        assertTrue(hasSameActivationPeriod);
    }

    @Test
    void givenHyperdriveModuleDeactivationDuringActivationPeriod_whenValidateSameActivationPeriod_thenReturnTrue() {
        HyperdriveModule anOtherHyperdriveModuleWithDeactivationDuringActivationPeriod = new HyperdriveModule(ANY_ID, ANY_POWER_LEVEL, ANY_DATE_TIME_BEFORE_ACTIVATION, ANY_DATE_TIME_DURING_ACTIVATION_PERIOD);

        boolean hasSameActivationPeriod = hyperdriveModule.hasSameActivationPeriodThan(anOtherHyperdriveModuleWithDeactivationDuringActivationPeriod);

        assertTrue(hasSameActivationPeriod);
    }

    @Test
    void givenHyperdriveModuleWithDifferentActivationPeriod_whenValidateSameActivationPeriod_thenReturnFalse() {
        HyperdriveModule anOtherHyperdriveModuleWithDifferentActivationPeriod = new HyperdriveModule(ANY_ID, ANY_POWER_LEVEL, ANY_DATE_TIME_AFTER_DEACTIVATION, ANY_DATE_TIME_AFTER_DEACTIVATION);

        boolean hasSameActivationPeriod = hyperdriveModule.hasSameActivationPeriodThan(anOtherHyperdriveModuleWithDifferentActivationPeriod);

        assertFalse(hasSameActivationPeriod);
    }

}