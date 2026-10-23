package uspace.application.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelerServiceTest {

    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";

    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);

    private static final String ANY_BOOKING_ID_STR = "ANY_BOOKING_ID";

    private static final BookingId ANY_BOOKING_ID = new BookingId(ANY_BOOKING_ID_STR);

    private static final String ANY_TRAVELER_ID_STR = "ANY_TRAVELER_ID";

    private static final TravelerId ANY_TRAVELER_ID = new TravelerId(ANY_TRAVELER_ID_STR);

    private static final String ANY_BOOKING_DATE_TIME_STR = "2084-04-08T12:30";

    private static final LocalDateTime ANY_EXPERIENCE_BOOKING_LOCAL_DATE_TIME = LocalDateTime.of(2084, 4, 8, 12, 30);

    private static final CruiseDateTime ANY_EXPERIENCE_BOOKING_DATE_TIME =
            new CruiseDateTime(ANY_EXPERIENCE_BOOKING_LOCAL_DATE_TIME);

    private TravelerService travelerService;

    @Mock
    private CruiseRepository cruiseRepositoryMock;

    @Mock
    private LocalDateTimeParser localDateTimeParserMock;

    @Mock
    private Cruise cruiseMock;

    @BeforeEach
    void createTravelerService() {
        travelerService = new TravelerService(cruiseRepositoryMock, localDateTimeParserMock);
    }

    @Test
    void givenNonExistingCruise_whenBookZeroGravityExperience_thenThrowCruiseNotFoundException() {
        givenNonExistingCruise();

        Executable bookZeroGravityExperience = () ->
                travelerService.bookZeroGravityExperience(ANY_CRUISE_ID_STR, ANY_BOOKING_ID_STR,
                        ANY_TRAVELER_ID_STR, ANY_BOOKING_DATE_TIME_STR);

        assertThrows(CruiseNotFoundException.class, bookZeroGravityExperience);
    }

    @Test
    void givenExistingCruise_whenBookZeroGravityExperience_thenBookZeroGravityExperience() {
        givenExistingCruise();
        when(localDateTimeParserMock.parse(ANY_BOOKING_DATE_TIME_STR))
                .thenReturn(ANY_EXPERIENCE_BOOKING_LOCAL_DATE_TIME);

        travelerService.bookZeroGravityExperience(ANY_CRUISE_ID_STR, ANY_BOOKING_ID_STR,
                ANY_TRAVELER_ID_STR, ANY_BOOKING_DATE_TIME_STR);

        verify(cruiseMock).bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_EXPERIENCE_BOOKING_DATE_TIME);
    }

    @Test
    void givenExistingCruise_whenBookZeroGravityExperience_thenSaveCruise() {
        givenExistingCruise();
        when(localDateTimeParserMock.parse(ANY_BOOKING_DATE_TIME_STR))
                .thenReturn(ANY_EXPERIENCE_BOOKING_LOCAL_DATE_TIME);

        travelerService.bookZeroGravityExperience(ANY_CRUISE_ID_STR, ANY_BOOKING_ID_STR,
                ANY_TRAVELER_ID_STR, ANY_BOOKING_DATE_TIME_STR);

        verify(cruiseRepositoryMock).save(cruiseMock);
    }

    private void givenNonExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);
    }

    private void givenExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
    }
}
