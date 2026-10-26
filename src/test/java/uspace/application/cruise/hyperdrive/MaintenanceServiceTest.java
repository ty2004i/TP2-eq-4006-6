package uspace.application.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.hyperdrive.dtos.NewMaintenanceDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.crew.exceptions.EmployeeNotFoundException;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleNotFoundException;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidMaintenanceDurationException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.hyperdrive.module.maintenance.Maintenance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceTest {

    private static final String ANY_CRUISE_ID = "cruise-1";

    private static final String ANY_MODULE_ID = "SS-99-T";

    private static final int ANY_VALID_DURATION = 85;

    private static final List<String> ANY_EMPLOYEE_IDS = List.of("ABC123");

    @Mock
    private CruiseRepository cruiseRepository;

    @Mock
    private Cruise cruise;

    private MaintenanceService service;

    private NewMaintenanceDto dto;

    @BeforeEach
    void setUp() {
        service = new MaintenanceService(cruiseRepository);

        dto = new NewMaintenanceDto();
        dto.duration = ANY_VALID_DURATION;
        dto.employeeIds = ANY_EMPLOYEE_IDS;
    }

    @Test
    void givenExistingCruise_whenAddMaintenance_thenPassCorrectDataToCruise() {
        when(cruiseRepository.findById(new CruiseId(ANY_CRUISE_ID))).thenReturn(cruise);

        service.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto);

        ArgumentCaptor<Maintenance> captor = ArgumentCaptor.forClass(Maintenance.class);
        verify(cruise).addMaintenance(
                eq(new HyperdriveModuleId(ANY_MODULE_ID)),
                captor.capture());

        Maintenance maintenance = captor.getValue();
        assertEquals(ANY_VALID_DURATION, maintenance.getDuration());
        assertEquals(ANY_EMPLOYEE_IDS, maintenance.getEmployeeIds());
    }

    @Test
    void givenSuccessfulAddition_whenAddMaintenance_thenSaveCruise() {
        when(cruiseRepository.findById(new CruiseId(ANY_CRUISE_ID))).thenReturn(cruise);

        service.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto);

        verify(cruiseRepository).save(cruise);
    }

    @Test
    void givenUnknownCruise_whenAddMaintenance_thenThrowWithoutSaving() {
        when(cruiseRepository.findById(new CruiseId(ANY_CRUISE_ID))).thenReturn(null);

        assertThrows(
                CruiseNotFoundException.class,
                () -> service.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto));

        verify(cruiseRepository, never()).save(any(Cruise.class));
    }

    @Test
    void givenUnknownEmployee_whenAddMaintenance_thenThrowWithoutSaving() {
        when(cruiseRepository.findById(new CruiseId(ANY_CRUISE_ID))).thenReturn(cruise);
        doThrow(new EmployeeNotFoundException())
                .when(cruise)
                .addMaintenance(any(HyperdriveModuleId.class), any(Maintenance.class));

        assertThrows(
                EmployeeNotFoundException.class,
                () -> service.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto));

        verify(cruiseRepository, never()).save(any(Cruise.class));
    }

    @Test
    void givenUnknownModule_whenAddMaintenance_thenThrowWithoutSaving() {
        when(cruiseRepository.findById(new CruiseId(ANY_CRUISE_ID))).thenReturn(cruise);
        doThrow(new HyperdriveModuleNotFoundException())
                .when(cruise)
                .addMaintenance(any(HyperdriveModuleId.class), any(Maintenance.class));

        assertThrows(
                HyperdriveModuleNotFoundException.class,
                () -> service.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto));

        verify(cruiseRepository, never()).save(any(Cruise.class));
    }

    @Test
    void givenInsufficientDuration_whenAddMaintenance_thenThrowWithoutSaving() {
        when(cruiseRepository.findById(new CruiseId(ANY_CRUISE_ID))).thenReturn(cruise);
        doThrow(new InvalidMaintenanceDurationException())
                .when(cruise)
                .addMaintenance(any(HyperdriveModuleId.class), any(Maintenance.class));

        assertThrows(
                InvalidMaintenanceDurationException.class,
                () -> service.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto));

        verify(cruiseRepository, never()).save(any(Cruise.class));
    }
}