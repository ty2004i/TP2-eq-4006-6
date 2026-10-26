package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.crew.exceptions.EmployeeNotFoundException;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleNotFoundException;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidMaintenanceDurationException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaintenanceExceptionMappersTest {

    @Test
    void givenEmployeeNotFound_whenMap_thenReturnExpectedNotFoundResponse() {
        EmployeeNotFoundExceptionMapper mapper = new EmployeeNotFoundExceptionMapper();

        try (Response response = mapper.toResponse(new EmployeeNotFoundException())) {
            ErrorResponse error = (ErrorResponse) response.getEntity();

            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
            assertEquals("EMPLOYEE_NOT_FOUND", error.error);
            assertEquals("One or more employees are not part of the cruise", error.description);
        }
    }

    @Test
    void givenModuleNotFound_whenMap_thenReturnExpectedNotFoundResponse() {
        HyperdriveModuleNotFoundExceptionMapper mapper = new HyperdriveModuleNotFoundExceptionMapper();

        try (Response response = mapper.toResponse(new HyperdriveModuleNotFoundException())) {
            ErrorResponse error = (ErrorResponse) response.getEntity();

            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
            assertEquals("HYPERDRIVE_MODULE_NOT_FOUND", error.error);
            assertEquals("Hyperdrive module not found in cruise", error.description);
        }
    }

    @Test
    void givenInvalidDuration_whenMap_thenReturnExpectedBadRequestResponse() {
        InvalidMaintenanceDurationExceptionMapper mapper = new InvalidMaintenanceDurationExceptionMapper();

        try (Response response = mapper.toResponse(new InvalidMaintenanceDurationException())) {
            ErrorResponse error = (ErrorResponse) response.getEntity();

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
            assertEquals("INVALID_MAINTENANCE_DURATION", error.error);
            assertEquals(
                    "Maintenance must have a duration greater or equal to the module power level",
                    error.description);
        }
    }
}