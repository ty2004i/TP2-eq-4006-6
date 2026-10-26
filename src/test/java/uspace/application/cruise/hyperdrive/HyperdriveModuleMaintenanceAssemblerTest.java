package uspace.application.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.application.cruise.hyperdrive.dtos.HyperdriveModuleDto;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.HyperdrivePowerLevel;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.hyperdrive.module.maintenance.Maintenance;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HyperdriveModuleMaintenanceAssemblerTest {

    private static final String MODULE_ID = "HY-77-V";

    private static final int MODULE_POWER = 85;

    private static final int FIRST_DURATION = MODULE_POWER;

    private static final int SECOND_DURATION = MODULE_POWER + 1;

    private static final List<String> FIRST_EMPLOYEE_IDS = List.of("ABC123");

    private static final List<String> SECOND_EMPLOYEE_IDS = List.of("DEF456", "GHI789");

    private static final CruiseDateTime ACTIVATION = new CruiseDateTime(
            LocalDateTime.of(2085, 1, 26, 1, 0));

    private static final CruiseDateTime DEACTIVATION = new CruiseDateTime(
            LocalDateTime.of(2085, 1, 28, 1, 0));

    private HyperdriveModuleAssembler assembler;

    private HyperdriveModule module;

    @BeforeEach
    void setUp() {
        assembler = new HyperdriveModuleAssembler();
        module = new HyperdriveModule(
                new HyperdriveModuleId(MODULE_ID),
                new HyperdrivePowerLevel(MODULE_POWER),
                ACTIVATION,
                DEACTIVATION);
    }

    @Test
    void givenModuleWithoutMaintenance_whenToDto_thenReturnEmptyMaintenances() {
        HyperdriveModuleDto dto = assembler.toDto(module);

        assertTrue(dto.maintenances.isEmpty());
    }

    @Test
    void givenMultipleMaintenances_whenToDto_thenIncludeTheirDurationsAndEmployees() {
        module.addMaintenance(new Maintenance(FIRST_DURATION, FIRST_EMPLOYEE_IDS));
        module.addMaintenance(new Maintenance(SECOND_DURATION, SECOND_EMPLOYEE_IDS));

        HyperdriveModuleDto dto = assembler.toDto(module);

        assertEquals(
                List.of(FIRST_DURATION, SECOND_DURATION),
                dto.maintenances.stream().map(maintenance -> maintenance.duration).toList());
        assertEquals(
                List.of(FIRST_EMPLOYEE_IDS, SECOND_EMPLOYEE_IDS),
                dto.maintenances.stream().map(maintenance -> maintenance.employeeIds).toList());
    }

    @Test
    void givenModuleWithMaintenance_whenToDto_thenPreserveModuleDetails() {
        module.addMaintenance(new Maintenance(FIRST_DURATION, FIRST_EMPLOYEE_IDS));

        HyperdriveModuleDto dto = assembler.toDto(module);

        assertEquals(MODULE_ID, dto.id);
        assertEquals(MODULE_POWER, dto.powerLevel);
        assertEquals(ACTIVATION.toString(), dto.activationDateTime);
        assertEquals(DEACTIVATION.toString(), dto.deactivationDateTime);
    }
}