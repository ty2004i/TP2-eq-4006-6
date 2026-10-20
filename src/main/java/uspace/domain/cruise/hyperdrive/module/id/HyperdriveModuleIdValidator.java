package uspace.domain.cruise.hyperdrive.module.id;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "hypermodul_id_validator_type", discriminatorType = DiscriminatorType.STRING)
public abstract class HyperdriveModuleIdValidator {

    @Id
    protected HyperdriveModuleIdValidatorId id;

    protected HyperdriveModuleIdValidator() {
        id = new HyperdriveModuleIdValidatorId();
    }

    public abstract boolean isValid(HyperdriveModuleId id);
}
