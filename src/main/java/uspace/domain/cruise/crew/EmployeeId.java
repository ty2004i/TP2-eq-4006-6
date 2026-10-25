package uspace.domain.cruise.crew;

import jakarta.persistence.Embeddable;
import uspace.domain.cruise.crew.exceptions.InvalidEmployeeIdFormatException;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeeId implements Serializable {

    private static final String VALID_FORMAT = "^[A-Z]{3}[0-9]{3}$";

    private String value;

    protected EmployeeId() {
    }

    public EmployeeId(String value) {
        if (value == null || !value.matches(VALID_FORMAT)) {
            throw new InvalidEmployeeIdFormatException();
        }

        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof EmployeeId)) {
            return false;
        }

        EmployeeId otherId = (EmployeeId) other;
        return Objects.equals(value, otherId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}