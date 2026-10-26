package uspace.domain.cruise.hyperdrive.module.maintenance;

import org.junit.jupiter.api.Test;
import uspace.domain.cruise.hyperdrive.module.HyperdrivePowerLevel;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidMaintenanceDurationException;
import uspace.domain.exceptions.InvalidParameterException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaintenanceTest {

    private static final int MODULE_POWER = 85;

    private static final int DURATION_BELOW_POWER = MODULE_POWER - 1;

    private static final int DURATION_ABOVE_POWER = MODULE_POWER + 1;

    private static final int ZERO_DURATION = 0;

    private static final int NEGATIVE_DURATION = -1;

    private static final List<String> ANY_EMPLOYEE_IDS = List.of("ABC123");

    private static final HyperdrivePowerLevel POWER_LEVEL = new HyperdrivePowerLevel(MODULE_POWER);

    @Test
    void givenDurationBelowPower_whenValidate_thenThrowInvalidMaintenanceDurationException() {
        Maintenance maintenance = new Maintenance(DURATION_BELOW_POWER, ANY_EMPLOYEE_IDS);

        assertThrows(
                InvalidMaintenanceDurationException.class,
                () -> maintenance.validateForPowerLevel(POWER_LEVEL));
    }

    @Test
    void givenDurationEqualToPower_whenValidate_thenDoNotThrow() {
        Maintenance maintenance = new Maintenance(MODULE_POWER, ANY_EMPLOYEE_IDS);

        assertDoesNotThrow(() -> maintenance.validateForPowerLevel(POWER_LEVEL));
    }

    @Test
    void givenDurationAbovePower_whenValidate_thenDoNotThrow() {
        Maintenance maintenance = new Maintenance(DURATION_ABOVE_POWER, ANY_EMPLOYEE_IDS);

        assertDoesNotThrow(() -> maintenance.validateForPowerLevel(POWER_LEVEL));
    }

    @Test
    void givenZeroDuration_whenCreateMaintenance_thenThrowInvalidParameterException() {
        assertThrows(
                InvalidParameterException.class,
                () -> new Maintenance(ZERO_DURATION, ANY_EMPLOYEE_IDS));
    }

    @Test
    void givenNegativeDuration_whenCreateMaintenance_thenThrowInvalidParameterException() {
        assertThrows(
                InvalidParameterException.class,
                () -> new Maintenance(NEGATIVE_DURATION, ANY_EMPLOYEE_IDS));
    }
}