package uspace.domain.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.booking.traveler.TravelerName;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.crew.exceptions.EmployeeNotFoundException;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.hyperdrive.module.maintenance.Maintenance;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CruiseMaintenanceTest {

    private static final CruiseId ANY_CRUISE_ID = new CruiseId("cruise-1");

    private static final HyperdriveModuleId ANY_MODULE_ID = new HyperdriveModuleId("HY-77-V");

    private static final String FIRST_EMPLOYEE_ID = "ABC123";

    private static final String SECOND_EMPLOYEE_ID = "DEF456";

    private static final String UNKNOWN_EMPLOYEE_ID = "XYZ789";

    private static final int ANY_VALID_DURATION = 85;

    private static final CruiseDateTime DEPARTURE = new CruiseDateTime(
            LocalDateTime.of(2085, 1, 25, 12, 0));

    private static final CruiseDateTime END = new CruiseDateTime(
            LocalDateTime.of(2085, 2, 1, 12, 0));

    @Mock
    private CabinAvailabilities cabinAvailabilities;

    @Mock
    private CruiseBookings cruiseBookings;

    @Mock
    private ZeroGravityExperience zeroGravityExperience;

    @Mock
    private HyperdriveInventory hyperdriveInventory;

    private Cruise cruise;

    @BeforeEach
    void setUp() {
        cruise = new Cruise(
                ANY_CRUISE_ID,
                DEPARTURE,
                END,
                cabinAvailabilities,
                cruiseBookings,
                zeroGravityExperience,
                hyperdriveInventory);
    }

    @Test
    void givenEmployeeInCruise_whenAddMaintenance_thenDelegateToInventory() {
        cruise.addCrewMember(new CrewMember(
                FIRST_EMPLOYEE_ID,
                new TravelerName("Bob")));
        Maintenance maintenance = new Maintenance(
                ANY_VALID_DURATION,
                List.of(FIRST_EMPLOYEE_ID));

        cruise.addMaintenance(ANY_MODULE_ID, maintenance);

        verify(hyperdriveInventory).addMaintenance(ANY_MODULE_ID, maintenance);
    }

    @Test
    void givenAllEmployeesInCruise_whenAddMaintenance_thenDelegateToInventory() {
        cruise.addCrewMember(new CrewMember(
                FIRST_EMPLOYEE_ID,
                new TravelerName("Bob")));
        cruise.addCrewMember(new CrewMember(
                SECOND_EMPLOYEE_ID,
                new TravelerName("Alice")));
        Maintenance maintenance = new Maintenance(
                ANY_VALID_DURATION,
                List.of(FIRST_EMPLOYEE_ID, SECOND_EMPLOYEE_ID));

        cruise.addMaintenance(ANY_MODULE_ID, maintenance);

        verify(hyperdriveInventory).addMaintenance(ANY_MODULE_ID, maintenance);
    }

    @Test
    void givenUnknownEmployee_whenAddMaintenance_thenThrowWithoutCallingInventory() {
        Maintenance maintenance = new Maintenance(
                ANY_VALID_DURATION,
                List.of(UNKNOWN_EMPLOYEE_ID));

        assertThrows(
                EmployeeNotFoundException.class,
                () -> cruise.addMaintenance(ANY_MODULE_ID, maintenance));

        verifyNoInteractions(hyperdriveInventory);
    }

    @Test
    void givenOneUnknownEmployeeAmongMembers_whenAddMaintenance_thenThrowWithoutCallingInventory() {
        cruise.addCrewMember(new CrewMember(
                FIRST_EMPLOYEE_ID,
                new TravelerName("Bob")));
        Maintenance maintenance = new Maintenance(
                ANY_VALID_DURATION,
                List.of(FIRST_EMPLOYEE_ID, UNKNOWN_EMPLOYEE_ID));

        assertThrows(
                EmployeeNotFoundException.class,
                () -> cruise.addMaintenance(ANY_MODULE_ID, maintenance));

        verifyNoInteractions(hyperdriveInventory);
    }
}