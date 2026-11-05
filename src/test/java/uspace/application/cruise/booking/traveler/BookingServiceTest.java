package uspace.application.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.booking.BookingAssembler;
import uspace.application.cruise.booking.BookingService;
import uspace.application.cruise.booking.dtos.BookingIdDto;
import uspace.application.cruise.booking.dtos.newBooking.NewBookingDto;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;
import uspace.domain.account.HomePlanetName;
import uspace.domain.account.Username;
import uspace.domain.account.exceptions.AccountNotFoundException;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingFactory;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.newBooking.NewBooking;
import uspace.domain.exceptions.MissingParameterException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private CruiseRepository cruiseRepository;

    @Mock
    private BookingFactory bookingFactory;

    @Mock
    private BookingAssembler bookingAssembler;

    @Mock
    private LocalDateTimeParser localDateTimeParser;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Cruise cruise;

    @Mock
    private Booking booking;

    @Mock
    private NewBooking newBooking;

    private BookingService bookingService;

    private final String CRUISE_ID = "CRUISE-1";

    private final String USER_NAME = "johnDoe";

    private final String HOME_PLANET = "Earth";

    private final String BOOKING_DATE_TIME = "2084-04-08T12:30";

    private final String BOOKING_ID = "BOOKING-1";

    private final Username username = new Username(USER_NAME);

    private final HomePlanetName homePlanetName = new HomePlanetName(HOME_PLANET);

    private final CruiseId cruiseId = new CruiseId(CRUISE_ID);

    @BeforeEach
    void setup() {
        bookingService = new BookingService(cruiseRepository, bookingFactory, bookingAssembler,
                localDateTimeParser, accountRepository);
    }

    @Test
    void givenNonExistentAccount_whenCreateBooking_thenThrowsAccountNotFound() {
        NewBookingDto BookingDto = new NewBookingDto();
        BookingDto.accountUsername = USER_NAME;
        when(accountRepository.findByUsername(username)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> bookingService.createBooking(CRUISE_ID, BookingDto));
        verify(cruiseRepository, never()).save(any());
    }

    @Test
    void givenValidBookingAndAccount_whenCreateBooking_thenAddsBookingIdToAccountAndSavesBoth() {
        NewBookingDto BookingDto = new NewBookingDto();

        BookingDto.accountUsername = USER_NAME;
        BookingDto.bookingDateTime = BOOKING_DATE_TIME;

        Account account = new Account(username, homePlanetName);
        when(accountRepository.findByUsername(username)).thenReturn(account);


        when(cruiseRepository.findById(cruiseId)).thenReturn(cruise);


        LocalDateTime parsedDateTime = LocalDateTime.parse(BOOKING_DATE_TIME);
        when(localDateTimeParser.parse(BookingDto.bookingDateTime)).thenReturn(parsedDateTime);

        when(bookingAssembler.toNewBooking(BookingDto, parsedDateTime)).thenReturn(newBooking);

        BookingId expectedBookingId = new BookingId(BOOKING_ID);
        when(booking.getId()).thenReturn(expectedBookingId);
        when(bookingFactory.create(newBooking)).thenReturn(booking);

        BookingIdDto resultDto = bookingService.createBooking(CRUISE_ID, BookingDto);

        assertNotNull(resultDto);

        verify(cruiseRepository).save(cruise);
        verify(accountRepository).save(account);
        assertTrue(account.getBookingIds().stream()
                .anyMatch(b -> b.toString().equals(resultDto.id)));
    }

}
