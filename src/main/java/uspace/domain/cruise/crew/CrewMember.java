package uspace.domain.cruise.crew;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.traveler.TravelerCategory;
import uspace.domain.cruise.booking.traveler.TravelerName;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;

@Entity
public class CrewMember extends Traveler {
    @Id
    protected String employeeId;

    public CrewMember() {
    }

    public CrewMember(String employeeId, TravelerName name) {
        super(null, name, TravelerCategory.CREW_MEMBER, null);
        this.employeeId = employeeId;
    }

    @Override
    public void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience,
                                          List<Traveler> travelersInBooking) {
        throw new UnsupportedOperationException("Crew members cannot book zero gravity experiences!!!");
    }

    @Override
    public boolean canAccompagnyChildForZeroGravityExperience(ZeroGravityExperience zeroGravityExperience) {
        return false;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}
