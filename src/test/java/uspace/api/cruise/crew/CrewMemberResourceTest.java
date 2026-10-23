package uspace.api.cruise.crew;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.crew.CrewMemberService;
import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CrewMemberResourceTest {

    private static final String ANY_CRUISE_ID = "cruise-1";

    private static final String ANY_EMPLOYEE_NAME = "Bob";

    private static final String VALID_EMPLOYEE_ID = "ABC123";

    @Mock
    private CrewMemberService crewMemberService;

    @Mock
    private CrewMemberDtoValidator crewMemberDtoValidator;

    private CrewMemberResource resource;

    private CrewMemberDto crewMemberDto;

    @BeforeEach
    void setUp() {
        resource = new CrewMemberResource(
                crewMemberService,
                crewMemberDtoValidator);

        crewMemberDto = new CrewMemberDto();
        crewMemberDto.name = ANY_EMPLOYEE_NAME;
        crewMemberDto.employeeId = VALID_EMPLOYEE_ID;
    }

    @Test
    void whenAddCrewMember_thenValidateDto() {
        try (Response response = resource.addCrewMember(ANY_CRUISE_ID, crewMemberDto)) {
            verify(crewMemberDtoValidator).validate(crewMemberDto);
        }
    }

    @Test
    void givenValidDto_whenAddCrewMember_thenDelegateToService() {
        try (Response response = resource.addCrewMember(ANY_CRUISE_ID, crewMemberDto)) {
            verify(crewMemberService).addCrewMember(ANY_CRUISE_ID, crewMemberDto);
        }
    }

    @Test
    void givenSuccessfulAddition_whenAddCrewMember_thenReturnCreated() {
        try (Response response = resource.addCrewMember(ANY_CRUISE_ID, crewMemberDto)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        }
    }

    @Test
    void givenInvalidDto_whenAddCrewMember_thenThrowWithoutCallingService() {
        doThrow(new MissingParameterException("name"))
                .when(crewMemberDtoValidator).validate(crewMemberDto);

        assertThrows(
                MissingParameterException.class,
                () -> resource.addCrewMember(ANY_CRUISE_ID, crewMemberDto));

        verifyNoInteractions(crewMemberService);
    }
}