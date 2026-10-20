package uspace.application.cruise.crew;

import jakarta.inject.Inject;
import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

public class CrewMemberService {

    private final CruiseRepository cruiseRepository;

    @Inject
    public CrewMemberService(CruiseRepository cruiseRepository) {
        this.cruiseRepository = cruiseRepository;
    }

    public void addCrewMember(String cruiseId, CrewMemberDto crewMemberDto) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        cruise.addCrewMember(crewMemberDto);
        cruiseRepository.save(cruise);
    }
}
