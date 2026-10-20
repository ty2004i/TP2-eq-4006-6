package uspace.domain.cruise.hyperdrive.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.dateTime.CruiseDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class HyperdriveModuleFactoryTest {
    private static final LocalTime ANY_LOCAL_TIME = LocalTime.of(3,2,2);
    private static final String ANY_HYPERDRIVE_MODULE_ID = "ANY_HYPERDRIVE_MODULE_ID";
    private static final int ANY_POWER_LEVEL = 15;
    private static final int ANY_ACTIVATION_DAYS = 20;
    private static final LocalDate ANY_ACTIVATION_LOCAL_DATE = LocalDate.of(1999, 2, 3);
    private HyperdriveModuleFactory hyperdriveModuleFactory;

    @BeforeEach
    void createHyperdriveModuleFactory() {
        hyperdriveModuleFactory = new HyperdriveModuleFactory(ANY_LOCAL_TIME);
    }

    @Test
    void whenCreate_thenActivationDateTimeIsInitializedWithActivationTime() {
        CruiseDateTime expectedActivationDateTime = new CruiseDateTime(LocalDateTime.of(1999,2,3,3,2,2));

        HyperdriveModule hyperdriveModule = hyperdriveModuleFactory.create(ANY_HYPERDRIVE_MODULE_ID, ANY_POWER_LEVEL, ANY_ACTIVATION_DAYS, ANY_ACTIVATION_LOCAL_DATE);

        assertEquals(expectedActivationDateTime, hyperdriveModule.getActivationDateTime());
    }

    @Test
    void whenCreate_thenDeactivationDateTimeIsInitializedWithActivationTimeAndActivationDays() {
        CruiseDateTime expectedDeactivationDateTime = new CruiseDateTime(LocalDateTime.of(1999,2,23,3,2,2));

        HyperdriveModule hyperdriveModule = hyperdriveModuleFactory.create(ANY_HYPERDRIVE_MODULE_ID, ANY_POWER_LEVEL, ANY_ACTIVATION_DAYS, ANY_ACTIVATION_LOCAL_DATE);

        assertEquals(expectedDeactivationDateTime, hyperdriveModule.getDeactivationDateTime());
    }

}