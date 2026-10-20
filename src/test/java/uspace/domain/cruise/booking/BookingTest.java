package uspace.domain.cruise.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.exceptions.InvalidBookingDateException;
import uspace.domain.cruise.booking.traveler.*;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.NoTravelerToBookException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingTest {
    private static final BookingId ANY_BOOKING_ID = new BookingId("id-123");
    private static final CabinType ANY_CABIN_TYPE = CabinType.DELUXE;
    private static final LocalDateTime ANY_LOCAL_DEPARTURE_DATE_TIME = LocalDateTime.of(2012, 3, 3, 0, 0);
    private static final CruiseDateTime ANY_DEPARTURE_DATE_TIME = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME);
    private static final CruiseDateTime ANY_DATE_TIME_BEFORE_DEPARTURE = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.minusHours(1));
    @Mock
    private Traveler aTravelerMock;
    @Mock
    private Traveler anotherTravelerMock;
    @Test
    void givenBookingWithoutTravelerAndDateTimeBeforeCruiseDeparture_whenValidate_thenThrowNoTravelerToBookException(){
        List<Traveler> noTraveler = new ArrayList<>();
        Booking bookingWithoutTraveler = new Booking(ANY_BOOKING_ID, noTraveler, ANY_CABIN_TYPE, ANY_DATE_TIME_BEFORE_DEPARTURE);

        Executable validate = () -> bookingWithoutTraveler.validate(ANY_DEPARTURE_DATE_TIME);

        assertThrows(NoTravelerToBookException.class, validate);
    }

    @Test
    void givenAtLeastOneTravelerAndBookingDateTimeAfterCruiseDeparture_whenValidate_thenThrowInvalidBookingDateException(){
        List<Traveler> anyTravelers = new ArrayList<>(List.of(aTravelerMock, anotherTravelerMock));
        CruiseDateTime anyBookingDateTimeAfterCruiseDeparture = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.plusHours(1));
        Booking bookingWithBookingDateTimeAfterCruiseDeparture = new Booking(ANY_BOOKING_ID, anyTravelers, ANY_CABIN_TYPE, anyBookingDateTimeAfterCruiseDeparture);

        Executable validate = () -> bookingWithBookingDateTimeAfterCruiseDeparture.validate(ANY_DEPARTURE_DATE_TIME);

        assertThrows(InvalidBookingDateException.class, validate);
    }

    @Test
    void givenAtLeastOneTravelerAndDateTimeBeforeCruiseDeparture_whenValidate_thenDoNotThrow(){
        List<Traveler> anyTravelers = new ArrayList<>(List.of(aTravelerMock, anotherTravelerMock));
        Booking validBooking = new Booking(ANY_BOOKING_ID, anyTravelers, ANY_CABIN_TYPE, ANY_DATE_TIME_BEFORE_DEPARTURE);

        Executable validate = () -> validBooking.validate(ANY_DEPARTURE_DATE_TIME);

        assertDoesNotThrow(validate);
    }

}