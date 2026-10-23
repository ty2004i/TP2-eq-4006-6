package uspace.application.cruise.crew;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.cabin.exceptions.CabinTypeNotAvailableException;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.crew.exceptions.CrewMemberAlreadyInCruiseException;
import uspace.domain.cruise.crew.exceptions.InvalidEmployeeIdFormatException;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrewMemberServiceTest {

    private static final String ANY_CRUISE_ID = "cruise-1";

    private static final String VALID_EMPLOYEE_ID = "ABC123";

    private static final String INVALID_EMPLOYEE_ID = "abc123";

    private static final String ANY_EMPLOYEE_NAME = "Bob";

    @Mock
    private CruiseRepository cruiseRepository;

    @Mock
    private Cruise cruise;

    private CrewMemberService service;

    private CrewMemberDto crewMemberDto;

    @BeforeEach
    void setUp() {
        service = new CrewMemberService(cruiseRepository);

        crewMemberDto = new CrewMemberDto();
        crewMemberDto.employeeId = VALID_EMPLOYEE_ID;
        crewMemberDto.name = ANY_EMPLOYEE_NAME;
    }

    @Test
    void givenValidMember_whenAddCrewMember_thenProvideMemberToCruise() {
        givenExistingCruise();

        service.addCrewMember(ANY_CRUISE_ID, crewMemberDto);

        ArgumentCaptor<CrewMember> memberCaptor = ArgumentCaptor.forClass(CrewMember.class);
        verify(cruise).addCrewMember(memberCaptor.capture());

        CrewMember addedMember = memberCaptor.getValue();
        assertEquals(VALID_EMPLOYEE_ID, addedMember.getEmployeeId());
        assertEquals(ANY_EMPLOYEE_NAME, addedMember.getName().toString());
    }

    @Test
    void givenSuccessfulAddition_whenAddCrewMember_thenSaveCruise() {
        givenExistingCruise();

        service.addCrewMember(ANY_CRUISE_ID, crewMemberDto);

        verify(cruiseRepository).save(cruise);
    }

    @Test
    void givenUnknownCruise_whenAddCrewMember_thenThrowWithoutSaving() {
        when(cruiseRepository.findById(new CruiseId(ANY_CRUISE_ID)))
                .thenReturn(null);

        assertThrows(
                CruiseNotFoundException.class,
                () -> service.addCrewMember(ANY_CRUISE_ID, crewMemberDto));

        verify(cruiseRepository, never()).save(any(Cruise.class));
        verifyNoInteractions(cruise);
    }

    @Test
    void givenInvalidEmployeeId_whenAddCrewMember_thenThrowWithoutAddingOrSaving() {
        givenExistingCruise();
        crewMemberDto.employeeId = INVALID_EMPLOYEE_ID;

        assertThrows(
                InvalidEmployeeIdFormatException.class,
                () -> service.addCrewMember(ANY_CRUISE_ID, crewMemberDto));

        verifyNoInteractions(cruise);
        verify(cruiseRepository, never()).save(any(Cruise.class));
    }

    @Test
    void givenExistingEmployeeId_whenAddCrewMember_thenPropagateExceptionWithoutSaving() {
        givenExistingCruise();
        doThrow(new CrewMemberAlreadyInCruiseException())
                .when(cruise).addCrewMember(any(CrewMember.class));

        assertThrows(
                CrewMemberAlreadyInCruiseException.class,
                () -> service.addCrewMember(ANY_CRUISE_ID, crewMemberDto));

        verify(cruiseRepository, never()).save(any(Cruise.class));
    }

    @Test
    void givenNoAvailableCabin_whenAddCrewMember_thenPropagateExceptionWithoutSaving() {
        givenExistingCruise();
        doThrow(new CabinTypeNotAvailableException())
                .when(cruise).addCrewMember(any(CrewMember.class));

        assertThrows(
                CabinTypeNotAvailableException.class,
                () -> service.addCrewMember(ANY_CRUISE_ID, crewMemberDto));

        verify(cruiseRepository, never()).save(any(Cruise.class));
    }

    private void givenExistingCruise() {
        when(cruiseRepository.findById(new CruiseId(ANY_CRUISE_ID)))
                .thenReturn(cruise);
    }
}