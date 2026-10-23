package uspace.domain.cruise.crew;

import org.junit.jupiter.api.Test;
import uspace.domain.cruise.crew.exceptions.InvalidEmployeeIdFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeIdTest {

    private static final String VALID_ID = "ABC123";

    private static final String ID_WITH_LOWERCASE_LETTERS = "abc123";

    private static final String ID_WITH_TOO_FEW_DIGITS = "ABC12";

    private static final String ID_WITH_TOO_MANY_LETTERS = "ABCD123";

    @Test
    void givenValidId_whenCreate_thenKeepValue() {
        EmployeeId employeeId = new EmployeeId(VALID_ID);

        assertEquals(VALID_ID, employeeId.toString());
    }

    @Test
    void givenLowercaseLetters_whenCreate_thenThrowInvalidEmployeeIdFormatException() {
        assertThrows(
                InvalidEmployeeIdFormatException.class,
                () -> new EmployeeId(ID_WITH_LOWERCASE_LETTERS));
    }

    @Test
    void givenTooFewDigits_whenCreate_thenThrowInvalidEmployeeIdFormatException() {
        assertThrows(
                InvalidEmployeeIdFormatException.class,
                () -> new EmployeeId(ID_WITH_TOO_FEW_DIGITS));
    }

    @Test
    void givenTooManyLetters_whenCreate_thenThrowInvalidEmployeeIdFormatException() {
        assertThrows(
                InvalidEmployeeIdFormatException.class,
                () -> new EmployeeId(ID_WITH_TOO_MANY_LETTERS));
    }

    @Test
    void givenNullId_whenCreate_thenThrowInvalidEmployeeIdFormatException() {
        assertThrows(
                InvalidEmployeeIdFormatException.class,
                () -> new EmployeeId(null));
    }

    @Test
    void givenSameValue_whenCompare_thenEmployeeIdsAreEqual() {
        EmployeeId firstId = new EmployeeId(VALID_ID);
        EmployeeId secondId = new EmployeeId(VALID_ID);

        assertEquals(firstId, secondId);
        assertEquals(firstId.hashCode(), secondId.hashCode());
    }
}