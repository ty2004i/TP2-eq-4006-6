package uspace.domain.account;

import jakarta.persistence.Embeddable;
import uspace.domain.exceptions.MissingParameterException;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Username implements Serializable {

    private String value;

    protected Username() {

    }

    public Username(String value) {
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Username username)) return false;
        return Objects.equals(value, username.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
