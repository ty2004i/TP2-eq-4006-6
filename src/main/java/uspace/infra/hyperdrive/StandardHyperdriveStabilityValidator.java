package uspace.infra.hyperdrive;

import standardHyperdriveStabilitySystem.StandardHyperdriveStabilitySystem;
import uspace.domain.cruise.hyperdrive.HyperdriveStabilityValidator;

public class StandardHyperdriveStabilityValidator implements HyperdriveStabilityValidator {

    private final StandardHyperdriveStabilitySystem standardHyperdriveStabilitySystem;

    public StandardHyperdriveStabilityValidator(StandardHyperdriveStabilitySystem standardHyperdriveStabilitySystem) {
        this.standardHyperdriveStabilitySystem = standardHyperdriveStabilitySystem;
    }

    @Override
    public boolean isHyperdriveModuleStable(String id) {
        return standardHyperdriveStabilitySystem.isHyperdriveModuleStable(id);
    }
}
