package uspace.domain.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.booking.BookingId;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private final String USER_NAME = "johnDoe";

    private final String HOME_PLANET_EARTH = "Earth";

    private final String HOME_PLANET_MARS = "Mars";

    private final String BOOKING_ID = "BOOKING-1";

    private final Username username = new Username(USER_NAME);

    private final HomePlanetName homePlanetNameEarth = new HomePlanetName(HOME_PLANET_EARTH);

    private final HomePlanetName homePlanetNameMars = new HomePlanetName(HOME_PLANET_MARS);

    @Test
    void whenAccountCreated_thenBookingListIsEmpty() {
        Account account = new Account(username, homePlanetNameEarth);

        assertNotNull(account.getBookingIds());
        assertTrue(account.getBookingIds().isEmpty());
    }

    @Test
    void givenBookingId_whenAddBookingId_thenBookingIsAddedWithoutDuplicates() {
        Account account = new Account(username, homePlanetNameEarth);
        BookingId bookingId = new BookingId(BOOKING_ID);

        account.addBookingId(bookingId);
        account.addBookingId(bookingId); // duplication

        assertEquals(1, account.getBookingIds().size());
        assertTrue(account.getBookingIds().contains(bookingId));
    }

    @Test
    void givenTwoAccountsWithSameUsername_thenAreEqual() {
        Account account1 = new Account(username, homePlanetNameEarth);
        Account account2 = new Account(username, homePlanetNameMars);

        assertEquals(account1, account2);
        assertEquals(account1.hashCode(), account2.hashCode());
    }

}
