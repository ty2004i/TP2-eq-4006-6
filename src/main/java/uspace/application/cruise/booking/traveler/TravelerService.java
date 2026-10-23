package uspace.application.cruise.booking.traveler;

import jakarta.inject.Inject;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.time.LocalDateTime;

public class TravelerService {
    private final CruiseRepository cruiseRepository;

    private final LocalDateTimeParser localDateTimeParser;

    @Inject
    public TravelerService(CruiseRepository cruiseRepository, LocalDateTimeParser localDateTimeParser) {
        this.cruiseRepository = cruiseRepository;
        this.localDateTimeParser = localDateTimeParser;
    }

    public void bookZeroGravityExperience(String cruiseId, String bookingIdStr, String travelerIdStr,
            String experienceBookingDateTime) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        LocalDateTime experienceBookingLocalDateTime = localDateTimeParser.parse(experienceBookingDateTime);
        CruiseDateTime experienceCruiseBookingDateTime = new CruiseDateTime(experienceBookingLocalDateTime);

        BookingId bookingId = new BookingId(bookingIdStr);
        TravelerId travelerId = new TravelerId(travelerIdStr);

        cruise.bookZeroGravityExperience(bookingId, travelerId, experienceCruiseBookingDateTime);

        cruiseRepository.save(cruise);
    }
}
