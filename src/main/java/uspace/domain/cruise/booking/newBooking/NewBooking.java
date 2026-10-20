package uspace.domain.cruise.booking.newBooking;

import java.time.LocalDateTime;
import java.util.List;

public record NewBooking(String cabinType, List<NewBookingTraveler> travelers, LocalDateTime bookingDateTime) {
}
