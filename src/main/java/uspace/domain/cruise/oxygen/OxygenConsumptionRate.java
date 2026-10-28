package uspace.domain.cruise.oxygen;

import uspace.domain.cruise.booking.traveler.TravelerCategory;

public enum OxygenConsumptionRate {
    CHILD(150),
    ADULT(250),
    SENIOR(300),
    CREW_MEMBER(250);

    private final double ratePerMinute;

    OxygenConsumptionRate(double ratePerMinute) {
        this.ratePerMinute = ratePerMinute;
    }

    public double getRatePerMinute() {
        return ratePerMinute;
    }

    public static OxygenConsumptionRate fromTravelerCategory(TravelerCategory category) {
        return switch (category) {
            case CHILD -> CHILD;
            case ADULT -> ADULT;
            case SENIOR -> SENIOR;
        };
    }
}
