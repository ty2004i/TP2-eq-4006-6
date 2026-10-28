package uspace.domain.cruise.booking;

import jakarta.persistence.*;
import uspace.domain.cruise.oxygen.OxygenConsumptionRate;

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

    public double calculatePassengersConsumptionPerMinute(double surchargeMultiplier) {
        if (this.bookings == null || this.bookings.isEmpty()) {
            return 0.0;
        }

        return this.bookings.values().stream()
                .mapToDouble(booking -> booking.calculatePassengerConsumptionPerMinute(surchargeMultiplier))
                .sum();
    }
}
