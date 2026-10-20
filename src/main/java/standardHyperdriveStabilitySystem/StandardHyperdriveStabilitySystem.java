package standardHyperdriveStabilitySystem;

import java.util.List;

public class StandardHyperdriveStabilitySystem {
    private static final List<String> UNSTABLE_MODULES = List.of("HY-222-Z");

    public boolean isHyperdriveModuleStable(String id) {
        return !UNSTABLE_MODULES.contains(id);
    }
}
