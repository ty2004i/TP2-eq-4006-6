package uspace.application.cruise;

import jakarta.inject.Inject;
import uspace.application.cruise.dtos.CruiseDto;
import uspace.application.cruise.hyperdrive.HyperdriveModuleAssembler;
import uspace.application.cruise.hyperdrive.dtos.HyperdriveModuleDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.crew.CrewMember;

import java.util.List;

public class CruiseAssembler {
    private final HyperdriveModuleAssembler hyperdriveModuleAssembler;

    @Inject
    public CruiseAssembler(HyperdriveModuleAssembler hyperdriveModuleAssembler) {
        this.hyperdriveModuleAssembler = hyperdriveModuleAssembler;
    }

    public CruiseDto toDto(Cruise cruise) {
        List<HyperdriveModuleDto> hyperdriveModuleDtos = createHyperdriveModuleDtos(cruise.getHyperdriveModules());
        List<String> crewMembersIds = getCrewMembersIds(cruise);

        return new CruiseDto(
            cruise.getId().toString(),
            cruise.getDepartureDateTime().toString(),
            cruise.getEndDateTime().toString(),
            hyperdriveModuleDtos,
            crewMembersIds
        );
    }

    private List<HyperdriveModuleDto> createHyperdriveModuleDtos(List<HyperdriveModule> hyperdriveModules) {
        return hyperdriveModules.stream().map(hyperdriveModuleAssembler::toDto).toList();
    }

    private List<String> getCrewMembersIds(Cruise cruise) {
        return cruise.getCrewMembers().stream().map(CrewMember::getEmployeeId).toList();
    }
}
