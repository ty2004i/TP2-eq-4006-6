package uspace.api.cruise.oxygen;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.oxygen.dto.OxygenResponseDto;
import uspace.application.cruise.oxygen.OxygenService;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OxygenResourceTest {

    private static final String CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";

    private static final String CALCULATION_DATE_TIME_VALID = "2085-01-31T08:00";

    private static final int REQUIRED_OXYGEN = 3_141_600;

    private static final String EMPTY_DATE = " ";

    @Mock
    private OxygenCalculationRequestValidator validator;

    @Mock
    private OxygenService oxygenService;

    private OxygenResource oxygenResource;

    @BeforeEach
    void setup() {
        oxygenResource = new OxygenResource(validator, oxygenService);
    }

    @Test
    void givenValidRequest_whenGetRemainingOxygen_thenReturnOkWithRequiredOxygen() {
        OxygenResponseDto expectedDto = new OxygenResponseDto(REQUIRED_OXYGEN);

        when(oxygenService.calculateRemainingOxygen(any(OxygenCalculationRequest.class))).thenReturn(expectedDto);

        Response response = oxygenResource.getRemainingOxygen(CRUISE_ID, CALCULATION_DATE_TIME_VALID);

        verify(validator).validate(any(OxygenCalculationRequest.class));
        verify(oxygenService).calculateRemainingOxygen(any(OxygenCalculationRequest.class));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertEquals(expectedDto, response.getEntity());
    }

    @Test
    void givenInvalidRequestFailingValidation_whenGetRemainingOxygen_thenPropagatesExceptionWithoutCallingService() {

        doThrow(new MissingParameterException("calculationDateTime"))
                .when(validator).validate(any(OxygenCalculationRequest.class));

        assertThrows(MissingParameterException.class, () ->
                oxygenResource.getRemainingOxygen(CRUISE_ID, EMPTY_DATE)
        );
        verifyNoInteractions(oxygenService);
    }

}
