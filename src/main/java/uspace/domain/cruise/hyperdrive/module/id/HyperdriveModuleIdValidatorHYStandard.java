package uspace.domain.cruise.hyperdrive.module.id;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("regex")
public class HyperdriveModuleIdValidatorHYStandard extends HyperdriveModuleIdValidator {
    private static final String REGEX_FOR_HY_STANDARD = "^HY-(?:[1-9]|[1-9][0-9]|[1-9][0-9]{2})-[A-Z]$";

    @Override
    public boolean isValid(HyperdriveModuleId id) {
        return id != null && id.toString().matches(REGEX_FOR_HY_STANDARD);
    }
}
