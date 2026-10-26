package uspace.domain.cruise.hyperdrive.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidMaintenanceDurationException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.hyperdrive.module.maintenance.Maintenance;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HyperdriveModuleMaintenanceTest {

    private static final HyperdriveModuleId ANY_MODULE_ID = new HyperdriveModuleId("HY-77-V");

    private static final int MODULE_POWER = 85;

    private static final int SUFFICIENT_DURATION = MODULE_POWER;

    private static final int INSUFFICIENT_DURATION = MODULE_POWER - 1;

    private static final List<String> ANY_EMPLOYEE_IDS = List.of("ABC123");

    private static final CruiseDateTime ACTIVATION = new CruiseDateTime(
            LocalDateTime.of(2085, 1, 26, 1, 0));

    private static final CruiseDateTime DEACTIVATION = new CruiseDateTime(
            LocalDateTime.of(2085, 1, 28, 1, 0));

    private HyperdriveModule module;

    @BeforeEach
    void setUp() {
        module = new HyperdriveModule(
                ANY_MODULE_ID,
                new HyperdrivePowerLevel(MODULE_POWER),
                ACTIVATION,
                DEACTIVATION);
    }

    @Test
    void givenNewModule_whenGetMaintenances_thenReturnEmptyList() {
        assertTrue(module.getMaintenances().isEmpty());
    }

    @Test
    void givenSufficientDuration_whenAddMaintenance_thenStoreMaintenance() {
        Maintenance maintenance = new Maintenance(SUFFICIENT_DURATION, ANY_EMPLOYEE_IDS);

        module.addMaintenance(maintenance);

        assertEquals(List.of(maintenance), module.getMaintenances());
    }

    @Test
    void givenInsufficientDuration_whenAddMaintenance_thenThrowWithoutStoringMaintenance() {
        Maintenance maintenance = new Maintenance(INSUFFICIENT_DURATION, ANY_EMPLOYEE_IDS);

        assertThrows(
                InvalidMaintenanceDurationException.class,
                () -> module.addMaintenance(maintenance));

        assertTrue(module.getMaintenances().isEmpty());
    }

    @Test
    void givenExistingMaintenance_whenAddAnotherMaintenance_thenKeepBoth() {
        Maintenance firstMaintenance = new Maintenance(SUFFICIENT_DURATION, ANY_EMPLOYEE_IDS);
        Maintenance secondMaintenance = new Maintenance(SUFFICIENT_DURATION, ANY_EMPLOYEE_IDS);
        module.addMaintenance(firstMaintenance);

        module.addMaintenance(secondMaintenance);

        assertEquals(
                List.of(firstMaintenance, secondMaintenance),
                module.getMaintenances());
    }
}