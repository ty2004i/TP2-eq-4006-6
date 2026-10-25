package uspace.api.cruise.crew;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CrewMemberDtoValidatorTest {

    private static final String ANY_EMPLOYEE_NAME = "Bob";

    private static final String VALID_EMPLOYEE_ID = "ABC123";

    private CrewMemberDtoValidator validator;

    private CrewMemberDto crewMemberDto;

    @BeforeEach
    void setUp() {
        validator = new CrewMemberDtoValidator();

        crewMemberDto = new CrewMemberDto();
        crewMemberDto.name = ANY_EMPLOYEE_NAME;
        crewMemberDto.employeeId = VALID_EMPLOYEE_ID;
    }

    @Test
    void givenAllRequiredFields_whenValidate_thenDoNotThrow() {
        assertDoesNotThrow(() -> validator.validate(crewMemberDto));
    }

    @Test
    void givenMissingName_whenValidate_thenThrowMissingParameterException() {
        crewMemberDto.name = null;

        MissingParameterException exception = assertThrows(
                MissingParameterException.class,
                () -> validator.validate(crewMemberDto));

        assertEquals("Missing parameter: name", exception.getMessage());
    }

    @Test
    void givenMissingEmployeeId_whenValidate_thenThrowMissingParameterException() {
        crewMemberDto.employeeId = null;

        MissingParameterException exception = assertThrows(
                MissingParameterException.class,
                () -> validator.validate(crewMemberDto));

        assertEquals("Missing parameter: employeeId", exception.getMessage());
    }
}