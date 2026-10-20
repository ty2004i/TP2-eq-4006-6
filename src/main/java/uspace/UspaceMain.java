package uspace;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uspace.api.ConfigurationServerRest;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.booking.CruiseBookingsId;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.cabin.CabinAvailabilitiesId;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.hyperdrive.HyperdriveInventoryId;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleIdValidatorHYStandard;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperienceId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UspaceMain implements Runnable {
    private static final int PORT = 8181;
    private static final String DEFAULT_CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";
    private static final LocalDateTime DEFAULT_CRUISE_DEPARTURE_DATE = LocalDateTime.of(2085, 1, 25, 12, 0);
    private static final LocalDateTime DEFAULT_CRUISE_END_DATE = LocalDateTime.of(2085, 2, 1, 12, 0);
    private static final int DEFAULT_STANDARD_CABINS = 10;
    private static final int DEFAULT_DELUXE_CABINS = 150;
    private static final int DEFAULT_SUITE_CABINS = 3;
    private static final int DEFAULT_ZERO_GRAVITY_EXPERIENCE_CAPACITY = 10;

    private final Logger logger = LoggerFactory.getLogger(UspaceMain.class);

    public static void main(String[] args) {
        new UspaceMain().run();
    }

    public void run() {
        Server server = new Server(PORT);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        ResourceConfig packageConfig = new ResourceConfig()
                .packages("uspace")
                .register(JacksonFeature.withoutExceptionMappers())
                .register(ConfigurationServerRest.class);
        ServletContainer container = new ServletContainer(packageConfig);
        ServletHolder servletHolder = new ServletHolder(container);

        contextHandler.addServlet(servletHolder, "/*");

        try {
            server.start();
            logger.info("Server started on port " + PORT);

            CruiseRepository cruiseRepository = getCruiseRepositoryFromContainer(container);
            initializeCruiseRepositoryWithDefaultCruise(cruiseRepository);


            server.join();
        } catch (Exception e) {
            logger.error("Error starting server", e);
        } finally {
            if (server.isRunning()) {
                server.destroy();
            }
        }
    }

    private CruiseRepository getCruiseRepositoryFromContainer(ServletContainer container) {
        return container.getApplicationHandler().getInjectionManager().getInstance(CruiseRepository.class);
    }

    private void initializeCruiseRepositoryWithDefaultCruise(CruiseRepository cruiseRepository) {
        Cruise cruise = new Cruise(new CruiseId(DEFAULT_CRUISE_ID),
                                   new CruiseDateTime(DEFAULT_CRUISE_DEPARTURE_DATE),
                                   new CruiseDateTime(DEFAULT_CRUISE_END_DATE),
                                   new CabinAvailabilities(new CabinAvailabilitiesId(),
                                                           new HashMap < > (
                                           Map.of(CabinType.STANDARD, DEFAULT_STANDARD_CABINS,
                                                  CabinType.DELUXE, DEFAULT_DELUXE_CABINS,
                                                  CabinType.SUITE, DEFAULT_SUITE_CABINS))),
                                   new CruiseBookings(new CruiseBookingsId(), new HashMap<>()),
                                   new ZeroGravityExperience(new ZeroGravityExperienceId(), DEFAULT_ZERO_GRAVITY_EXPERIENCE_CAPACITY),
                                   new HyperdriveInventory(new HyperdriveInventoryId(), new ArrayList < > (), new HyperdriveModuleIdValidatorHYStandard()));

        cruiseRepository.save(cruise);
    }
}