package uspace.domain.cruise;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.TravelerName;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.crew.exceptions.CrewMemberAlreadyInCruiseException;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleActivationDateException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Cruise")
public class Cruise {
    @Id
    private CruiseId id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "localDateTime", column = @Column(name = "departureDateTime")),
    })
    private CruiseDateTime departureDateTime;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "localDateTime", column = @Column(name = "endDateTime")),
    })
    private CruiseDateTime endDateTime;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private CabinAvailabilities cabinAvailabilities;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private CruiseBookings cruiseBookings;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private ZeroGravityExperience zeroGravityExperience;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private HyperdriveInventory hyperdriveInventory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<String, CrewMember> crewMembers;

    public Cruise() {

    }

    public Cruise(CruiseId id, CruiseDateTime departureDateTime, CruiseDateTime endDateTime,
            CabinAvailabilities cabinAvailabilities,
            CruiseBookings cruiseBookings, ZeroGravityExperience zeroGravityExperience,
            HyperdriveInventory hyperdriveInventory) {
        this.id = id;
        this.departureDateTime = departureDateTime;
        this.endDateTime = endDateTime;
        this.cabinAvailabilities = cabinAvailabilities;
        this.cruiseBookings = cruiseBookings;
        this.zeroGravityExperience = zeroGravityExperience;
        this.hyperdriveInventory = hyperdriveInventory;
        this.crewMembers = new HashMap<>();
    }

    public CruiseId getId() {
        return id;
    }

    public CruiseDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public CruiseDateTime getEndDateTime() {
        return endDateTime;
    }

    public List<HyperdriveModule> getHyperdriveModules() {
        return hyperdriveInventory.getAllHyperdriveModules();
    }

    public void processBooking(Booking booking) {
        booking.validate(departureDateTime);
        cabinAvailabilities.bookCabin(booking.getCabinType());
        cruiseBookings.add(booking);
    }

    public Booking findBookingById(BookingId bookingId) {
        return cruiseBookings.findById(bookingId);
    }

    public void bookZeroGravityExperience(BookingId bookingId, TravelerId travelerId,
            CruiseDateTime experienceBookingDateTime) {
        if (experienceBookingDateTime.isAfter(departureDateTime)) {
            throw new ZeroGravityExperienceBookingTimeException();
        }

        Booking booking = cruiseBookings.findById(bookingId);
        if (booking == null) {
            throw new TravelerNotFoundException();
        }

        booking.bookZeroGravityExperience(travelerId, zeroGravityExperience);
    }

    public void addHyperdriveModule(HyperdriveModule hyperdriveModule) {
        if (!hyperdriveModule.isActivationBetween(departureDateTime, endDateTime)) {
            throw new InvalidHyperdriveModuleActivationDateException();
        }

        hyperdriveInventory.addHyperdriveModule(hyperdriveModule);
    }

    public void addCrewMember(CrewMember crewMember) {
        if (crewMembers.containsKey(crewMember.getEmployeeId())) {
            throw new CrewMemberAlreadyInCruiseException();
        }

        cabinAvailabilities.bookCabin(CabinType.STANDARD);
        crewMembers.put(crewMember.getEmployeeId(), crewMember);
    }

    public List<CrewMember> getCrewMembers() {
        return List.copyOf(crewMembers.values());
    }
}
