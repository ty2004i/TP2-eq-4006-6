package uspace.api.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.application.cruise.hyperdrive.dtos.NewMaintenanceDto;
import uspace.domain.exceptions.InvalidParameterException;
import uspace.domain.exceptions.MissingParameterException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NewMaintenanceDtoValidatorTest {

    private static final int VALID_DURATION = 10;

    private static final int MISSING_DURATION = 0;

    private static final int NEGATIVE_DURATION = -1;

    private static final String ANY_EMPLOYEE_ID = "ABC123";

    private NewMaintenanceDtoValidator validator;

    private NewMaintenanceDto dto;

    @BeforeEach
    void setUp() {
        validator = new NewMaintenanceDtoValidator();

        dto = new NewMaintenanceDto();
        dto.duration = VALID_DURATION;
        dto.employeeIds = List.of(ANY_EMPLOYEE_ID);
    }

    @Test
    void givenValidFields_whenValidate_thenDoNotThrow() {
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void givenZeroDuration_whenValidate_thenThrowMissingParameterException() {
        dto.duration = MISSING_DURATION;

        MissingParameterException exception = assertThrows(
                MissingParameterException.class,
                () -> validator.validate(dto));

        assertEquals("Missing parameter: duration", exception.getMessage());
    }

    @Test
    void givenNegativeDuration_whenValidate_thenThrowInvalidParameterException() {
        dto.duration = NEGATIVE_DURATION;

        InvalidParameterException exception = assertThrows(
                InvalidParameterException.class,
                () -> validator.validate(dto));

        assertEquals(
                "Maintenance duration must be greater than 0",
                exception.getMessage());
    }

    @Test
    void givenMissingEmployeeIds_whenValidate_thenThrowMissingParameterException() {
        dto.employeeIds = null;

        MissingParameterException exception = assertThrows(
                MissingParameterException.class,
                () -> validator.validate(dto));

        assertEquals("Missing parameter: employeeIds", exception.getMessage());
    }

    @Test
    void givenEmptyEmployeeIds_whenValidate_thenThrowMissingParameterException() {
        dto.employeeIds = List.of();

        MissingParameterException exception = assertThrows(
                MissingParameterException.class,
                () -> validator.validate(dto));

        assertEquals("Missing parameter: employeeIds", exception.getMessage());
    }
}