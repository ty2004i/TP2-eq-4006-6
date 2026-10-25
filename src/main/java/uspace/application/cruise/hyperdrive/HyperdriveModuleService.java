package uspace.application.cruise.hyperdrive;

import jakarta.inject.Inject;
import uspace.application.cruise.hyperdrive.dtos.NewHyperdriveModuleDto;
import uspace.application.utils.dateTimeParser.LocalDateParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModuleFactory;
import uspace.domain.cruise.hyperdrive.HyperdriveStabilityValidator;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleException;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;

import java.time.LocalDate;

public class HyperdriveModuleService {
    private final CruiseRepository cruiseRepository;

    private final HyperdriveStabilityValidator hyperdriveStabilityValidator;

    private final HyperdriveModuleFactory hyperdriveModuleFactory;

    private final LocalDateParser localDateParser;

    @Inject
    public HyperdriveModuleService(CruiseRepository cruiseRepository,
                                   HyperdriveStabilityValidator hyperdriveStabilityValidator,
                                   HyperdriveModuleFactory hyperdriveModuleFactory, LocalDateParser localDateParser) {
        this.cruiseRepository = cruiseRepository;
        this.hyperdriveStabilityValidator = hyperdriveStabilityValidator;
        this.hyperdriveModuleFactory = hyperdriveModuleFactory;
        this.localDateParser = localDateParser;
    }


    public void addHyperdriveModule(String cruiseId, NewHyperdriveModuleDto newHyperdriveModuleDto) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        if (!hyperdriveStabilityValidator.isHyperdriveModuleStable(newHyperdriveModuleDto.id)) {
            throw new InvalidHyperdriveModuleException();
        }

        LocalDate activationDate = localDateParser.parse(newHyperdriveModuleDto.activationDate);
        HyperdriveModule hyperdriveModule = hyperdriveModuleFactory.create(newHyperdriveModuleDto.id,
                newHyperdriveModuleDto.powerLevel, newHyperdriveModuleDto.activationDays, activationDate);

        cruise.addHyperdriveModule(hyperdriveModule);
        cruiseRepository.save(cruise);
    }
}
