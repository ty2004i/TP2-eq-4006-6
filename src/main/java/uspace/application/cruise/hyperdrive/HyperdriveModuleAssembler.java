package uspace.application.cruise.hyperdrive;

import uspace.application.cruise.hyperdrive.dtos.HyperdriveModuleDto;
import uspace.application.cruise.hyperdrive.dtos.MaintenanceDto;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;

import java.util.List;

public class HyperdriveModuleAssembler {

    public HyperdriveModuleDto toDto(HyperdriveModule module) {
        List<MaintenanceDto> maintenances = module.getMaintenances().stream()
                .map(maintenance -> new MaintenanceDto(
                        maintenance.getDuration(),
                        maintenance.getEmployeeIds()))
                .toList();

        return new HyperdriveModuleDto(
                module.getId().toString(),
                module.getPowerLevel().toInt(),
                module.getActivationDateTime().toString(),
                module.getDeactivationDateTime().toString(),
                maintenances);
    }
}