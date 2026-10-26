package uspace.domain.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleNotFoundException;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidMaintenanceDurationException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleIdValidator;
import uspace.domain.cruise.hyperdrive.module.maintenance.Maintenance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HyperdriveInventoryMaintenanceTest {

    private static final HyperdriveModuleId EXISTING_MODULE_ID = new HyperdriveModuleId("HY-77-V");

    private static final HyperdriveModuleId UNKNOWN_MODULE_ID = new HyperdriveModuleId("UNKNOWN");

    @Mock
    private HyperdriveModule module;

    @Mock
    private HyperdriveModuleIdValidator moduleIdValidator;

    @Mock
    private Maintenance maintenance;

    private HyperdriveInventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new HyperdriveInventory(
                new HyperdriveInventoryId(),
                List.of(module),
                moduleIdValidator);

        when(module.getId()).thenReturn(EXISTING_MODULE_ID);
    }

    @Test
    void givenExistingModule_whenAddMaintenance_thenDelegateToModule() {
        inventory.addMaintenance(EXISTING_MODULE_ID, maintenance);

        verify(module).addMaintenance(maintenance);
    }

    @Test
    void givenUnknownModule_whenAddMaintenance_thenThrowWithoutAddingMaintenance() {
        assertThrows(
                HyperdriveModuleNotFoundException.class,
                () -> inventory.addMaintenance(UNKNOWN_MODULE_ID, maintenance));

        verify(module, never()).addMaintenance(maintenance);
    }

    @Test
    void givenModuleRejectsMaintenance_whenAddMaintenance_thenPropagateException() {
        doThrow(new InvalidMaintenanceDurationException())
                .when(module)
                .addMaintenance(maintenance);

        assertThrows(
                InvalidMaintenanceDurationException.class,
                () -> inventory.addMaintenance(EXISTING_MODULE_ID, maintenance));
    }
}