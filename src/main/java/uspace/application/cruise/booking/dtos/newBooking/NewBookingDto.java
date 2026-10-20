package uspace.application.cruise.booking.dtos.newBooking;

import java.util.List;

public class NewBookingDto {
    public String cabinType;
    public List<NewBookingTravelerDto> travelers;
    public String bookingDateTime;

    public NewBookingDto() {
    }

    public NewBookingDto(String cabinType, List<NewBookingTravelerDto> travelers, String bookingDateTime) {
        this.cabinType = cabinType;
        this.travelers = travelers;
        this.bookingDateTime = bookingDateTime;
    }
}
