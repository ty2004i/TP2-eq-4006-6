package uspace.domain.account;

import jakarta.persistence.*;
import uspace.domain.cruise.booking.BookingId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Account")
public class Account {
    @EmbeddedId
    private Username username;

    @Embedded
    private HomePlanetName homePlanetName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Account_Bookings", joinColumns = @JoinColumn(name = "username"))
    @AttributeOverride(name = "id", column = @Column(name = "booking_id"))
    private List<BookingId> bookingIds;

    protected Account() {
        this.bookingIds = new ArrayList<>();
    }

    public Account(Username username, HomePlanetName homePlanetName) {
        this.username = username;
        this.homePlanetName = homePlanetName;
        this.bookingIds = new ArrayList<>();
    }

    public Account(Username username, HomePlanetName homePlanetName, List<BookingId> bookingIds) {
        this.username = username;
        this.homePlanetName = homePlanetName;
        this.bookingIds = bookingIds != null ? new ArrayList<>(bookingIds) : new ArrayList<>();
    }

    public Username getUsername() {
        return username;
    }

    public HomePlanetName getHomePlanetName() {
        return homePlanetName;
    }

    public List<BookingId> getBookingIds() {
        return Collections.unmodifiableList(bookingIds);
    }

    public void addBookingId(BookingId bookingId) {
        if (bookingId != null && !this.bookingIds.contains(bookingId)) {
            this.bookingIds.add(bookingId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(username, account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
