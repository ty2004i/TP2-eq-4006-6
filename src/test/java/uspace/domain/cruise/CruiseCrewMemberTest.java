package uspace.domain.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.cabin.CabinAvailabilitiesId;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.cabin.exceptions.CabinTypeNotAvailableException;
import uspace.domain.cruise.crew.exceptions.CrewMemberAlreadyInCruiseException;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.booking.traveler.TravelerName;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CruiseCrewMemberTest {

    private static final CruiseId ANY_CRUISE_ID = new CruiseId("cruise-1");

    private static final String VALID_EMPLOYEE_ID = "ABC123";

    private static final String ANY_EMPLOYEE_NAME = "Bob";

    private static final int INITIAL_STANDARD_CABINS = 2;

    private static final int ONE_RESERVED_CABIN = 1;

    private static final int NO_AVAILABLE_CABINS = 0;

    private static final int ONE_CREW_MEMBER = 1;

    private static final CruiseDateTime DEPARTURE = new CruiseDateTime(
            LocalDateTime.of(2085, 1, 25, 12, 0));

    private static final CruiseDateTime END = new CruiseDateTime(
            LocalDateTime.of(2085, 2, 1, 12, 0));

    @Mock
    private CruiseBookings cruiseBookings;

    @Mock
    private ZeroGravityExperience zeroGravityExperience;

    @Mock
    private HyperdriveInventory hyperdriveInventory;

    private CabinAvailabilities cabinAvailabilities;

    private Cruise cruise;

    private CrewMember crewMember;

    @BeforeEach
    void setUp() {
        createCruiseWithStandardCabins(INITIAL_STANDARD_CABINS);

        crewMember = new CrewMember(
                VALID_EMPLOYEE_ID,
                new TravelerName(ANY_EMPLOYEE_NAME));
    }

    @Test
    void givenValidCrewMember_whenAddCrewMember_thenAddMemberToCruise() {
        cruise.addCrewMember(crewMember);

        assertEquals(ONE_CREW_MEMBER, cruise.getCrewMembers().size());
        assertEquals(VALID_EMPLOYEE_ID, cruise.getCrewMembers().get(0).getEmployeeId());
        assertEquals(ANY_EMPLOYEE_NAME, cruise.getCrewMembers().get(0).getName().toString());
    }

    @Test
    void givenAvailableStandardCabin_whenAddCrewMember_thenReserveOneStandardCabin() {
        cruise.addCrewMember(crewMember);

        assertEquals(
                INITIAL_STANDARD_CABINS - ONE_RESERVED_CABIN,
                cabinAvailabilities.getAvailabilitiesByCabinType(CabinType.STANDARD));
    }

    @Test
    void givenExistingEmployeeId_whenAddCrewMember_thenRejectWithoutReservingAnotherCabin() {
        cruise.addCrewMember(crewMember);

        assertThrows(
                CrewMemberAlreadyInCruiseException.class,
                () -> cruise.addCrewMember(crewMember));

        assertEquals(ONE_CREW_MEMBER, cruise.getCrewMembers().size());
        assertEquals(
                INITIAL_STANDARD_CABINS - ONE_RESERVED_CABIN,
                cabinAvailabilities.getAvailabilitiesByCabinType(CabinType.STANDARD));
    }

    @Test
    void givenNoStandardCabin_whenAddCrewMember_thenThrowWithoutAddingMember() {
        createCruiseWithStandardCabins(NO_AVAILABLE_CABINS);

        assertThrows(
                CabinTypeNotAvailableException.class,
                () -> cruise.addCrewMember(crewMember));

        assertTrue(cruise.getCrewMembers().isEmpty());
        assertEquals(
                NO_AVAILABLE_CABINS,
                cabinAvailabilities.getAvailabilitiesByCabinType(CabinType.STANDARD));
    }

    private void createCruiseWithStandardCabins(int availableCabins) {
        cabinAvailabilities = new CabinAvailabilities(
                new CabinAvailabilitiesId(),
                new HashMap<>(Map.of(CabinType.STANDARD, availableCabins)));

        cruise = new Cruise(
                ANY_CRUISE_ID,
                DEPARTURE,
                END,
                cabinAvailabilities,
                cruiseBookings,
                zeroGravityExperience,
                hyperdriveInventory);
    }
}