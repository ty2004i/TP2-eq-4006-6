package uspace.api.cruise.hyperdrive;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.hyperdrive.MaintenanceService;
import uspace.application.cruise.hyperdrive.dtos.NewMaintenanceDto;
import uspace.domain.exceptions.MissingParameterException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class MaintenanceResourceTest {

    private static final String ANY_CRUISE_ID = "cruise-1";

    private static final String ANY_MODULE_ID = "AA-99-T";

    private static final int ANY_VALID_DURATION = 85;

    private static final List<String> ANY_EMPLOYEE_IDS = List.of("ABC123");

    @Mock
    private MaintenanceService maintenanceService;

    @Mock
    private NewMaintenanceDtoValidator validator;

    private MaintenanceResource resource;

    private NewMaintenanceDto dto;

    @BeforeEach
    void setUp() {
        resource = new MaintenanceResource(maintenanceService, validator);

        dto = new NewMaintenanceDto();
        dto.duration = ANY_VALID_DURATION;
        dto.employeeIds = ANY_EMPLOYEE_IDS;
    }

    @Test
    void whenAddMaintenance_thenValidateDto() {
        try (Response response = resource.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto)) {
            verify(validator).validate(dto);
        }
    }

    @Test
    void givenValidRequest_whenAddMaintenance_thenDelegateToService() {
        try (Response response = resource.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto)) {
            verify(maintenanceService).addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto);
        }
    }

    @Test
    void givenSuccessfulAddition_whenAddMaintenance_thenReturnCreated() {
        try (Response response = resource.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        }
    }

    @Test
    void givenInvalidRequest_whenAddMaintenance_thenThrowWithoutCallingService() {
        doThrow(new MissingParameterException("duration"))
                .when(validator)
                .validate(dto);

        assertThrows(
                MissingParameterException.class,
                () -> resource.addMaintenance(ANY_CRUISE_ID, ANY_MODULE_ID, dto));

        verifyNoInteractions(maintenanceService);
    }
}