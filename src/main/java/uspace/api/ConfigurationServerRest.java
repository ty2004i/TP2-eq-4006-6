package uspace.api;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import standardHyperdriveStabilitySystem.StandardHyperdriveStabilitySystem;
import uspace.api.cruise.booking.NewBookingDtoValidator;
import uspace.api.cruise.hyperdrive.NewHyperdriveModuleValidator;
import uspace.application.cruise.hyperdrive.HyperdriveModuleAssembler;
import uspace.api.cruise.booking.traveler.zeroGravityExperienceRequest.ZeroGravityExperienceRequestValidator;
import uspace.application.cruise.hyperdrive.HyperdriveModuleService;
import uspace.application.utils.dateTimeParser.LocalDateParser;
import uspace.api.cruise.crew.CrewMemberDtoValidator;
import uspace.application.cruise.crew.CrewMemberService;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.application.cruise.booking.BookingAssembler;
import uspace.application.cruise.booking.BookingService;
import uspace.application.cruise.CruiseAssembler;
import uspace.application.cruise.CruiseService;
import uspace.application.cruise.booking.BookingTravelerAssembler;
import uspace.application.cruise.booking.traveler.TravelerService;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingFactory;
import uspace.domain.cruise.booking.traveler.TravelerFactory;
import uspace.domain.cruise.hyperdrive.HyperdriveStabilityValidator;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModuleFactory;
import uspace.infra.hyperdrive.StandardHyperdriveStabilityValidator;
import uspace.infra.persistence.inMemory.InMemoryCruiseRepository;
import uspace.api.cruise.hyperdrive.NewMaintenanceDtoValidator;
import uspace.application.cruise.hyperdrive.MaintenanceService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ConfigurationServerRest extends AbstractBinder {
    private final static String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    private final static LocalTime HYPERDRIVE_ACTIVATION_TIME = LocalTime.of(1, 0, 0);

    @Override
    protected void configure() {
        bind(new StandardHyperdriveStabilityValidator(new StandardHyperdriveStabilitySystem()))
                .to(HyperdriveStabilityValidator.class);

        bindAsContract(TravelerFactory.class);
        bindAsContract(BookingFactory.class);
        bind(new HyperdriveModuleFactory(HYPERDRIVE_ACTIVATION_TIME)).to(HyperdriveModuleFactory.class);

        bindAsContract(BookingTravelerAssembler.class);
        bindAsContract(BookingAssembler.class);
        bindAsContract(HyperdriveModuleAssembler.class);
        bindAsContract(CruiseAssembler.class);

        bind(DateTimeFormatter.ISO_LOCAL_DATE_TIME).to(DateTimeFormatter.class);
        bindAsContract(LocalDateTimeParser.class);
        bind(new LocalDateParser(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT))).to(LocalDateParser.class);

        bind(InMemoryCruiseRepository.class).to(CruiseRepository.class);

        bindAsContract(CrewMemberDtoValidator.class);
        bindAsContract(ZeroGravityExperienceRequestValidator.class);
        bindAsContract(NewBookingDtoValidator.class);
        bindAsContract(NewHyperdriveModuleValidator.class);

        bindAsContract(CrewMemberService.class);
        bindAsContract(TravelerService.class);
        bindAsContract(HyperdriveModuleService.class);
        bindAsContract(BookingService.class);
        bindAsContract(CruiseService.class);
        bindAsContract(NewMaintenanceDtoValidator.class);
        bindAsContract(MaintenanceService.class);
    }
}
