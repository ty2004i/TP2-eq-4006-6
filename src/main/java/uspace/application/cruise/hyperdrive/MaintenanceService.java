package uspace.application.cruise.hyperdrive;

import jakarta.inject.Inject;
import uspace.application.cruise.hyperdrive.dtos.NewMaintenanceDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.hyperdrive.module.maintenance.Maintenance;

public class MaintenanceService {

    private final CruiseRepository cruiseRepository;

    @Inject
    public MaintenanceService(CruiseRepository cruiseRepository) {
        this.cruiseRepository = cruiseRepository;
    }

    public void addMaintenance(String cruiseId, String moduleId, NewMaintenanceDto dto) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        Maintenance maintenance = new Maintenance(dto.duration, dto.employeeIds);

        cruise.addMaintenance(new HyperdriveModuleId(moduleId), maintenance);

        cruiseRepository.save(cruise);
    }
}