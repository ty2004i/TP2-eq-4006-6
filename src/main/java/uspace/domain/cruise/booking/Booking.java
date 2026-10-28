package uspace.domain.cruise.booking;

import jakarta.persistence.*;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.exceptions.InvalidBookingDateException;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.NoTravelerToBookException;
import uspace.domain.cruise.oxygen.OxygenConsumptionRate;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;

@Entity
public class Booking {

    @Id
    private BookingId id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Traveler> travelers;

    @Enumerated(EnumType.STRING)
    private CabinType cabinType;

    @Embedded
    private CruiseDateTime bookingDateTime;

    public Booking() {

    }

    public Booking(BookingId id, List<Traveler> travelers, CabinType cabinType, CruiseDateTime bookingDateTime) {
        this.id = id;
        this.travelers = travelers;
        this.cabinType = cabinType;
        this.bookingDateTime = bookingDateTime;
    }

    public BookingId getId() {
        return id;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public List<Traveler> getTravelers() {
        return travelers;
    }

    public CruiseDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void validate(CruiseDateTime cruiseDepartureDateTime) {
        if (travelers.isEmpty()) {
            throw new NoTravelerToBookException();
        }

        if (!bookingDateIsBeforeCruiseDeparture(cruiseDepartureDateTime)) {
            throw new InvalidBookingDateException();
        }
    }

    public int getTravelersNumber() {
        return travelers.size();
    }

    public void bookZeroGravityExperience(TravelerId travelerId, ZeroGravityExperience zeroGravityExperience) {
        Traveler traveler = findTravelerById(travelerId);
        if (traveler == null) {
            throw new TravelerNotFoundException();
        }

        traveler.bookZeroGravityExperience(zeroGravityExperience, travelers);
    }

    private boolean bookingDateIsBeforeCruiseDeparture(CruiseDateTime cruiseDepartureDateTime) {
        return bookingDateTime.isBefore(cruiseDepartureDateTime);
    }

    private Traveler findTravelerById(TravelerId travelerId) {
        return travelers.stream()
                        .filter(traveler -> traveler.getId().equals(travelerId))
                        .findFirst()
                        .orElse(null);
    }

    public double calculatePassengerConsumptionPerMinute(double surchargeMultiplier) {
        return this.travelers.stream()
                .mapToDouble(traveler -> {
                    OxygenConsumptionRate rate = OxygenConsumptionRate.fromTravelerCategory(traveler.getCategory());
                    return rate.getRatePerMinute() * surchargeMultiplier;
                })
                .sum();
    }
}
