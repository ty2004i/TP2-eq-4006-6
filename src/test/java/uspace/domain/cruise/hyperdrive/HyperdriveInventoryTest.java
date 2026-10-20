package uspace.domain.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.HyperdrivePowerLevel;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleAlreadyExistsException;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleConflictException;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleIdException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleIdValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HyperdriveInventoryTest {
    private static final HyperdriveInventoryId ANY_INVENTORY_ID = new HyperdriveInventoryId();
    private static final HyperdriveModuleId ANY_ID = new HyperdriveModuleId("AAA");
    private static final HyperdrivePowerLevel ANY_POWER_LEVEL = new HyperdrivePowerLevel(4);
    private static final CruiseDateTime ANY_ACTIVATION_DATE_TIME = new CruiseDateTime(LocalDateTime.of(2003, 6, 6, 1, 0, 0));
    private static final CruiseDateTime ANY_DEACTIVATION_DATE_TIME = new CruiseDateTime(LocalDateTime.of(2003, 6, 10, 1, 0, 0));
    private static final HyperdriveModule ANY_HYPERDRIVE_MODULE = new HyperdriveModule(ANY_ID, ANY_POWER_LEVEL, ANY_ACTIVATION_DATE_TIME, ANY_DEACTIVATION_DATE_TIME);
    private HyperdriveInventory hyperdriveInventory;
    @Mock
    private HyperdriveModuleIdValidator hyperdriveModuleIdValidatorMock;
    @Mock
    private HyperdriveModule hyperdriveModuleMock;

    @BeforeEach
    void createHyperdriveInventory() {
        List<HyperdriveModule> hyperdriveModules = new ArrayList<>(List.of(hyperdriveModuleMock));
        hyperdriveInventory = new HyperdriveInventory(ANY_INVENTORY_ID, hyperdriveModules, hyperdriveModuleIdValidatorMock);
    }

    @Test
    void givenInvalidId_whenAddHyperdriveModule_thenThrowInvalidHyperdriveModuleIdException() {
        when(hyperdriveModuleIdValidatorMock.isValid(ANY_ID)).thenReturn(false);

        Executable addHyperdriveModule = () -> hyperdriveInventory.addHyperdriveModule(ANY_HYPERDRIVE_MODULE);

        assertThrows(InvalidHyperdriveModuleIdException.class, addHyperdriveModule);
    }

    @Test
    void givenValidIdAndExistingHyperdriveModuleWithSameId_whenAddHyperdriveModule_thenThrowHyperdriveModuleAlreadyExistsException() {
        when(hyperdriveModuleIdValidatorMock.isValid(ANY_ID)).thenReturn(true);
        when(hyperdriveModuleMock.hasSameIdThan(ANY_HYPERDRIVE_MODULE)).thenReturn(true);

        Executable addHyperdriveModule = () -> hyperdriveInventory.addHyperdriveModule(ANY_HYPERDRIVE_MODULE);

        assertThrows(HyperdriveModuleAlreadyExistsException.class, addHyperdriveModule);
    }

    @Test
    void givenValidUniqueIdAndExistingHyperdriveModuleWithSameActivationPeriod_whenAddHyperdriveModule_thenThrowHyperdriveModuleConflictException() {
        when(hyperdriveModuleIdValidatorMock.isValid(ANY_ID)).thenReturn(true);
        when(hyperdriveModuleMock.hasSameIdThan(ANY_HYPERDRIVE_MODULE)).thenReturn(false);
        when(hyperdriveModuleMock.hasSameActivationPeriodThan(ANY_HYPERDRIVE_MODULE)).thenReturn(true);

        Executable addHyperdriveModule = () -> hyperdriveInventory.addHyperdriveModule(ANY_HYPERDRIVE_MODULE);

        assertThrows(HyperdriveModuleConflictException.class, addHyperdriveModule);
    }

    @Test
    void givenValidUniqueIdAndNoExistingHyperdriveModuleWithSameActivationPeriod_whenAddHyperdriveModule_thenAddHyperdriveModuleToInventory() {
        when(hyperdriveModuleIdValidatorMock.isValid(ANY_ID)).thenReturn(true);
        when(hyperdriveModuleMock.hasSameIdThan(ANY_HYPERDRIVE_MODULE)).thenReturn(false);
        when(hyperdriveModuleMock.hasSameActivationPeriodThan(ANY_HYPERDRIVE_MODULE)).thenReturn(false);

        hyperdriveInventory.addHyperdriveModule(ANY_HYPERDRIVE_MODULE);

        List<HyperdriveModule> hyperdriveModulesInInventory = hyperdriveInventory.getAllHyperdriveModules();
        assertTrue(hyperdriveModulesInInventory.contains(ANY_HYPERDRIVE_MODULE));
    }

}