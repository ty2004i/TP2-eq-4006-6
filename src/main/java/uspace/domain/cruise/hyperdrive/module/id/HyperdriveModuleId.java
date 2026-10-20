package uspace.domain.cruise.hyperdrive.module.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class HyperdriveModuleId implements Serializable {

    private String id;

    public HyperdriveModuleId() {

    }

    public HyperdriveModuleId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        HyperdriveModuleId other = (HyperdriveModuleId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
