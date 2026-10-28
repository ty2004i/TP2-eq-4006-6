package uspace.domain.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.booking.CruiseBookingsId;
import uspace.domain.cruise.booking.traveler.*;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.cabin.CabinAvailabilitiesId;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.oxygen.Oxygen;
import uspace.domain.cruise.oxygen.exceptions.OxygenCalculationDateException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CruiseOxygenTest {

    private static final String CALCULATION_DATE_TIME_UNDER_75 = "2085-01-02T00:00";

    private static final String CALCULATION_DATE_TIME_OVER_75 = "2085-01-01T04:00";

    private static final String CALCULATION_DATE_TIME_BEFORE_DEPARTURE = "2084-12-31T23:59";

    private static final String CALCULATION_DATE_TIME_AFTER_END = "2085-01-03T04:01";

    private static final String DEPARTURE_DATE_TIME = "2085-01-01T00:00";

    private static final String END_DATE_TIME = "2085-01-03T04:00";

    private static final String BOOKING_ID = "Booking-1";

    private static final String CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";


    private final TravelerFactory travelerFactory = new TravelerFactory();

    private Cruise cruise;

    private final CruiseDateTime departureDateTime = new CruiseDateTime(LocalDateTime.parse(DEPARTURE_DATE_TIME));

    private final CruiseDateTime endDateTime = new CruiseDateTime(LocalDateTime.parse(END_DATE_TIME));

    public CruiseOxygenTest() {
    }

    @BeforeEach
    void setup() {
        CruiseBookings cruiseBookings = new CruiseBookings(new CruiseBookingsId(), new HashMap<>());
        ZeroGravityExperience zeroGravityExperience = new ZeroGravityExperience();
        HyperdriveInventory hyperdriveInventory = new HyperdriveInventory();

        Map<CabinType, Integer> cabinStock = new HashMap<>();
        cabinStock.put(CabinType.STANDARD, 10);
        cabinStock.put(CabinType.DELUXE, 5);
        cabinStock.put(CabinType.SUITE, 2);

        CabinAvailabilities cabinAvailabilities = new CabinAvailabilities(new CabinAvailabilitiesId(), cabinStock);

        cruise = new Cruise(
                new CruiseId(CRUISE_ID),
                departureDateTime,
                endDateTime,
                cabinAvailabilities,
                cruiseBookings,
                zeroGravityExperience,
                hyperdriveInventory
        );

        List<Traveler> travelers = List.of(
                travelerFactory.create("Adult One", TravelerCategory.ADULT),
                travelerFactory.create("Adult Two", TravelerCategory.ADULT),
                travelerFactory.create("Child One", TravelerCategory.CHILD),
                travelerFactory.create("Senior One", TravelerCategory.SENIOR)
        );

        Booking booking = new Booking(
                new BookingId(BOOKING_ID),
                travelers,
                CabinType.STANDARD,
                departureDateTime
        );
        cruiseBookings.add(booking);

        cruise.addCrewMember(new CrewMember("MBR101", new TravelerName("Alice")));
        cruise.addCrewMember(new CrewMember("MBR102", new TravelerName("Bob")));
        cruise.addCrewMember(new CrewMember("MBR103", new TravelerName("Charlie")));
    }

    @Test
    void givenRemainingTimeUnder75Percent_whenCalculateRemainingOxygen_thenReturnsStandardCalculatedOxygen() {

        CruiseDateTime calculationDateTime = new CruiseDateTime(LocalDateTime.parse(CALCULATION_DATE_TIME_UNDER_75));

        Oxygen result = cruise.calculateRemainingOxygen(calculationDateTime);

        // 1680 min * 1700 O2/min = 2 856 000
        assertEquals(2_856_000L, result.value());
    }

    @Test
    void givenRemainingTimeAtLeast75Percent_whenCalculateRemainingOxygen_thenReturnsOverconsumptionCalculatedOxygen() {

        CruiseDateTime calculationDateTime = new CruiseDateTime(LocalDateTime.parse(CALCULATION_DATE_TIME_OVER_75));

        Oxygen result = cruise.calculateRemainingOxygen(calculationDateTime);

        // (2880 min * 1747.5 O2/min) * 1.10 = 5 536 080
        assertEquals(5_536_080L, result.value());
    }

    @Test
    void givenCalculationDateEqualToEndDateTime_whenCalculateRemainingOxygen_thenReturnsZeroOxygen() {

        CruiseDateTime calculationDateTime = endDateTime;

        Oxygen result = cruise.calculateRemainingOxygen(calculationDateTime);

        assertEquals(0L, result.value());
    }

    @Test
    void givenCalculationDateBeforeDeparture_whenCalculateRemainingOxygen_thenThrowsOxygenException() {

        CruiseDateTime beforeDeparture =
                new CruiseDateTime(LocalDateTime.parse(CALCULATION_DATE_TIME_BEFORE_DEPARTURE));

        assertThrows(OxygenCalculationDateException.class, () ->
                cruise.calculateRemainingOxygen(beforeDeparture)
        );
    }

    @Test
    void givenCalculationDateAfterEnd_whenCalculateRemainingOxygen_thenThrowsOxygenException() {

        CruiseDateTime afterEnd = new CruiseDateTime(LocalDateTime.parse(CALCULATION_DATE_TIME_AFTER_END));

        assertThrows(OxygenCalculationDateException.class, () ->
                cruise.calculateRemainingOxygen(afterEnd)
        );
    }
}
