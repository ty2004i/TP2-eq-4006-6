package uspace.application.cruise.crew;

import jakarta.inject.Inject;
import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.traveler.TravelerName;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.booking.traveler.TravelerName;

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

        CrewMember crewMember = new CrewMember(
                crewMemberDto.employeeId,
                new TravelerName(crewMemberDto.name));

        cruise.addCrewMember(crewMember);
        cruiseRepository.save(cruise);
    }
}
