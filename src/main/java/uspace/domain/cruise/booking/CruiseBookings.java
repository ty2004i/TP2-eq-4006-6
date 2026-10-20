package uspace.domain.cruise.booking;

import jakarta.persistence.*;

import java.util.Map;

@Entity
public class CruiseBookings {
    @Id
    private CruiseBookingsId id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKey(name = "id")
    private Map<BookingId, Booking> bookings;

    public CruiseBookings() {
    }

    public CruiseBookings(CruiseBookingsId id, Map<BookingId, Booking> bookings) {
        this.id = id;
        this.bookings = bookings;
    }

    public void add(Booking booking) {
        bookings.put(booking.getId(), booking);
    }

    public Booking findById(BookingId bookingId) {
        return bookings.get(bookingId);
    }
}
