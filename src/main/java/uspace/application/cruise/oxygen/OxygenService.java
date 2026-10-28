package uspace.application.cruise.oxygen;

import jakarta.inject.Inject;
import uspace.api.cruise.oxygen.OxygenCalculationRequest;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.oxygen.Oxygen;

import java.time.LocalDateTime;

public class OxygenService {

    private final CruiseRepository cruiseRepository;

    private final LocalDateTimeParser localDateTimeParser;

    @Inject
    public OxygenService(CruiseRepository cruiseRepository, LocalDateTimeParser localDateTimeParser) {
        this.cruiseRepository = cruiseRepository;
        this.localDateTimeParser = localDateTimeParser;
    }

    public OxygenResponseDto calculateRemainingOxygen(OxygenCalculationRequest request) {

        LocalDateTime calculationLocalDateTime = localDateTimeParser.parse(request.calculationDateTime);
        CruiseDateTime calculationCruiseDateTime = new CruiseDateTime(calculationLocalDateTime);

        Cruise cruise = findCruiseById(request.cruiseId);
        Oxygen requiredOxygen = cruise.calculateRemainingOxygen(calculationCruiseDateTime);

        return new OxygenResponseDto(requiredOxygen.value());
    }


    private Cruise findCruiseById(String cruiseId) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }
        return cruise;
    }
}
