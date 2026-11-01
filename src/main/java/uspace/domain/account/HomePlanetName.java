package uspace.domain.account;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HomePlanetName implements Serializable {

    private String value;

    protected HomePlanetName() {

    }

    public HomePlanetName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Home planet name cannot be null or empty");
        }
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
        if (!(o instanceof HomePlanetName that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
