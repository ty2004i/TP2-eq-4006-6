package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.Entity;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.booking.traveler.exceptions.ZeroGravityExperienceChildCriteriaException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;


@Entity
public class ChildTraveler extends Traveler {

    public ChildTraveler() {
    }

    public ChildTraveler(TravelerId id, TravelerName name, List<Badge> badges) {
        super(id, name, TravelerCategory.CHILD, badges);
    }

    @Override
    public void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience, List<Traveler> travelersInBooking) {

//        //Solution sans TDA acceptable pour le tp1
//        for (Traveler traveler : travelersInBooking) {
//            if ((traveler.getCategory() == TravelerCategory.ADULT || traveler.getCategory() == TravelerCategory.SENIOR)
//                && zeroGravityExperience.hasTravelerBooked(traveler.getId())) {
//                zeroGravityExperience.book(id);
//                earnOneTimeBadge(Badge.MINI_ZERO_G);
//                return;
//            }
//        }
//
//        throw new ZeroGravityExperienceChildCriteriaException();

        for (Traveler traveler : travelersInBooking) {
            if (traveler.canAccompagnyChildForZeroGravityExperience(zeroGravityExperience)) {
                zeroGravityExperience.book(id);
                earnOneTimeBadge(Badge.MINI_ZERO_G);
                return;
            }
        }

        throw new ZeroGravityExperienceChildCriteriaException();
    }

    @Override
    public boolean canAccompagnyChildForZeroGravityExperience(ZeroGravityExperience zeroGravityExperience) {
        return false;
    }
}
