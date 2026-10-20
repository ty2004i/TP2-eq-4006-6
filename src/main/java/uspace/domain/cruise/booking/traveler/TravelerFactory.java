package uspace.domain.cruise.booking.traveler;

import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.booking.traveler.exceptions.InvalidTravelerCategoryException;

import java.util.ArrayList;
import java.util.List;

public class TravelerFactory {
    public Traveler create(String travelerNameStr, TravelerCategory travelerCategory) {
        TravelerId travelerId = new TravelerId();
        TravelerName travelerName = new TravelerName(travelerNameStr);
        List<Badge> noBadgesInitial = new ArrayList<>();

        return switch (travelerCategory) {
            case ADULT -> new AdultTraveler(travelerId, travelerName, noBadgesInitial);
            case CHILD -> new ChildTraveler(travelerId, travelerName, noBadgesInitial);
            case SENIOR -> new SeniorTraveler(travelerId, travelerName, noBadgesInitial);
            default -> throw new InvalidTravelerCategoryException();
        };
    }
}
