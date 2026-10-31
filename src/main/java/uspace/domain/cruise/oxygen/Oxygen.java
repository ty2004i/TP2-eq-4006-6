package uspace.domain.cruise.oxygen;

import java.util.Objects;


public class Oxygen {

    private final int value;

    private Oxygen(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Oxygen value cannot be negative");
        }
        this.value = value;
    }

    public static Oxygen from(int value) {
        return new Oxygen(value);
    }

    public static Oxygen zero() {
        return new Oxygen(0);
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Oxygen oxygen)) return false;
        return value == oxygen.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
