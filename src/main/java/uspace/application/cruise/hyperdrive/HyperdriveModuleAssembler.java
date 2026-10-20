package uspace.application.cruise.hyperdrive;

import uspace.application.cruise.hyperdrive.dtos.HyperdriveModuleDto;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;

public class HyperdriveModuleAssembler {
    public HyperdriveModuleDto toDto(HyperdriveModule module) {
        return new HyperdriveModuleDto(module.getId().toString(),
                                       module.getPowerLevel().toInt(),
                                       module.getActivationDateTime().toString(),
                                       module.getDeactivationDateTime().toString());
    }
}
