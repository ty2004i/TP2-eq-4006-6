package uspace.domain.cruise.hyperdrive.module.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class HyperdriveModuleIdValidatorId implements Serializable {

    private String id;

    public HyperdriveModuleIdValidatorId() {
        this.id = UUID.randomUUID().toString();
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
        HyperdriveModuleIdValidatorId other = (HyperdriveModuleIdValidatorId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
