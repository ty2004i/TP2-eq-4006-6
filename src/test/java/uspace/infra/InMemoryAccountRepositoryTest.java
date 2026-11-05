package uspace.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.account.Account;
import uspace.domain.account.HomePlanetName;
import uspace.domain.account.Username;
import uspace.domain.cruise.booking.BookingId;
import uspace.infra.persistence.inMemory.InMemoryAccountRepository;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryAccountRepositoryTest {

    private InMemoryAccountRepository repository;

    private final String USER_NAME_1 = "johnDoe";

    private final String USER_NAME_2 = "janeDoe";

    private final String HOME_PLANET_USER_1 = "Earth";

    private final String BOOKING_ID = "BOOKING-1";

    private final Username username1 = new Username(USER_NAME_1);

    private final Username username2 = new Username(USER_NAME_2);

    private final HomePlanetName homePlanetUser1 = new HomePlanetName(HOME_PLANET_USER_1);

    @BeforeEach
    void setup() {

        repository = new InMemoryAccountRepository();
    }

    @Test
    void givenAccount_whenSave_thenCanBeRetrievedByUsername() {
        Account account = new Account(username1, homePlanetUser1);

        repository.save(account);
        Account retrieved = repository.findByUsername(username1);

        assertNotNull(retrieved);
        assertEquals(username1, retrieved.getUsername());
        assertEquals(homePlanetUser1, retrieved.getHomePlanetName());
    }

    @Test
    void givenNonExistentAccount_whenFindByUsername_thenReturnNull() {
        Account retrieved = repository.findByUsername(username1);

        assertNull(retrieved);
    }

    @Test
    void givenSavedAccount_whenExistsByUsername_thenReturnTrue() {
        Account account = new Account(username1, homePlanetUser1);
        repository.save(account);

        assertTrue(repository.existsByUsername(username1));
        assertFalse(repository.existsByUsername(username2));
    }

    @Test
    void givenAccountWithBookings_whenSaved_thenRetainsBookings() {
        Account account = new Account(username1, homePlanetUser1);
        BookingId bookingId = new BookingId(BOOKING_ID);
        account.addBookingId(bookingId);

        repository.save(account);
        Account retrieved = repository.findByUsername(username1);

        assertEquals(1, retrieved.getBookingIds().size());
        assertTrue(retrieved.getBookingIds().contains(bookingId));
    }

}
