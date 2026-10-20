package uspace.domain.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleActivationDateException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CruiseTest {

    private static final CruiseId ANY_CRUISE_ID = new CruiseId("ANY_CRUISE_ID");
    private static final LocalDateTime ANY_LOCAL_DEPARTURE_DATE_TIME = LocalDateTime.now();
    private static final CruiseDateTime ANY_DEPARTURE_DATE_TIME = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME);
    private static final CruiseDateTime ANY_END_DATE_TIME = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.plusDays(1));
    private static final CruiseDateTime ANY_DATE_TIME_BEFORE_DEPARTURE = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.minusHours(1));
    private static final CruiseDateTime ANY_DATE_TIME_AFTER_DEPARTURE = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.plusHours(1));
    private static final BookingId ANY_BOOKING_ID = new BookingId("ANY_BOOKING_ID");
    private static final TravelerId ANY_TRAVELER_ID = new TravelerId();
    private Cruise cruise;
    @Mock
    private CruiseBookings cruiseBookingsMock;
    @Mock
    private CabinAvailabilities cabinAvailabilitiesMock;
    @Mock
    private ZeroGravityExperience zeroGravityExperienceMock;
    @Mock
    private Booking bookingMock;
    @Mock
    private HyperdriveInventory hyperdriveInventoryMock;
    @Mock
    private HyperdriveModule hyperdriveModuleMock;

    @BeforeEach
    void createCruise() {
        cruise = new Cruise(ANY_CRUISE_ID, ANY_DEPARTURE_DATE_TIME, ANY_END_DATE_TIME, cabinAvailabilitiesMock,
                            cruiseBookingsMock, zeroGravityExperienceMock, hyperdriveInventoryMock);
    }

    @Test
    void givenExperienceBookingDateTimeAfterDeparture_whenBookZeroGravityExperience_thenThrowZeroGravityExperienceBookingTimeException() {
        Executable bookZeroGravityExperience = () ->
                cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_DATE_TIME_AFTER_DEPARTURE);

        assertThrows(ZeroGravityExperienceBookingTimeException.class, bookZeroGravityExperience);
    }

    @Test
    void givenExperienceBookingDateTimeBeforeDepartureAndNonExistingBookingId_whenBookZeroGravityExperience_thenThrowTravelerNotFoundException() {
        Executable bookZeroGravityExperience = () ->
                cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_DATE_TIME_BEFORE_DEPARTURE);

        assertThrows(TravelerNotFoundException.class, bookZeroGravityExperience);
    }

    @Test
    void givenExperienceBookingDateTimeBeforeDepartureAndExistingBookingId_whenBookZeroGravityExperience_thenBookingBookZeroGravityExperience() {
        when(cruiseBookingsMock.findById(ANY_BOOKING_ID)).thenReturn(bookingMock);

        cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_DATE_TIME_BEFORE_DEPARTURE);

        verify(bookingMock).bookZeroGravityExperience(ANY_TRAVELER_ID, zeroGravityExperienceMock);
    }

    @Test
    void givenHyperdriveModuleNotActivatedDuringCruiseTime_whenAddHyperdriveModule_thenThrowInvalidHyperdriveModuleActivationDateException() {
        when(hyperdriveModuleMock.isActivationBetween(ANY_DEPARTURE_DATE_TIME, ANY_END_DATE_TIME)).thenReturn(false);

        Executable addHyperdriveModule = () -> cruise.addHyperdriveModule(hyperdriveModuleMock);

        assertThrows(InvalidHyperdriveModuleActivationDateException.class, addHyperdriveModule);
    }

    @Test
    void givenHyperdriveModuleActivatedDuringCruiseTime_whenAddHyperdriveModule_thenAddToInventory() {
        when(hyperdriveModuleMock.isActivationBetween(ANY_DEPARTURE_DATE_TIME, ANY_END_DATE_TIME)).thenReturn(true);

        cruise.addHyperdriveModule(hyperdriveModuleMock);

        verify(hyperdriveInventoryMock).addHyperdriveModule(hyperdriveModuleMock);
    }
}