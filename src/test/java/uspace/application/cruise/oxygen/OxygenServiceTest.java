package uspace.application.cruise.oxygen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.cruise.oxygen.OxygenCalculationRequest;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.oxygen.Oxygen;
import uspace.domain.exceptions.InvalidDateFormatException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OxygenServiceTest {

    private static final String CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";

    private static final String CALCULATION_DATE_TIME_VALID = "2085-01-31T08:00";

    private static final int REQUIRED_OXYGEN = 3_141_600;

    private static final String INVALID_DATE = "2085-01-31";

    private static final String CRUISE_NOT_FOUND = "Unknow-cruise";

    @Mock
    private CruiseRepository cruiseRepository;

    @Mock
    private LocalDateTimeParser localDateTimeParser;

    @Mock
    private Cruise cruise;

    private OxygenService oxygenService;

    @BeforeEach
    void setUp() {
        oxygenService = new OxygenService(cruiseRepository, localDateTimeParser);
    }

    @Test
    void givenValidRequestAndExistingCruise_whenCalculateRemainingOxygen_thenReturnsCorrectDto() {

        OxygenCalculationRequest request = new OxygenCalculationRequest(CRUISE_ID, CALCULATION_DATE_TIME_VALID);
        LocalDateTime parsedDate = LocalDateTime.of(2085, 1, 31, 8, 0);
        CruiseDateTime calculationDateTime = new CruiseDateTime(parsedDate);
        Oxygen oxygenResult = Oxygen.from(REQUIRED_OXYGEN);

        when(localDateTimeParser.parse(CALCULATION_DATE_TIME_VALID)).thenReturn(parsedDate);
        when(cruiseRepository.findById(any(CruiseId.class))).thenReturn(cruise);
        when(cruise.calculateRemainingOxygen(calculationDateTime)).thenReturn(oxygenResult);

        // WHEN
        OxygenResponseDto responseDto = oxygenService.calculateRemainingOxygen(request);

        // THEN
        assertNotNull(responseDto);
        assertEquals(REQUIRED_OXYGEN, responseDto.requiredOxygen);
        verify(localDateTimeParser).parse(CALCULATION_DATE_TIME_VALID);
        verify(cruiseRepository).findById(any(CruiseId.class));
        verify(cruise).calculateRemainingOxygen(calculationDateTime);
    }

    @Test
    void givenInvalidDateFormat_whenCalculateRemainingOxygen_thenPropagatesInvalidDateFormatException() {


        OxygenCalculationRequest request = new OxygenCalculationRequest(CRUISE_ID, INVALID_DATE);

        doThrow(new InvalidDateFormatException()).when(localDateTimeParser).parse(INVALID_DATE);

        assertThrows(InvalidDateFormatException.class, () ->
                oxygenService.calculateRemainingOxygen(request)
        );
        verifyNoInteractions(cruiseRepository);
    }

    @Test
    void givenNonExistingCruise_whenCalculateRemainingOxygen_thenThrowsCruiseNotFoundException() {

        OxygenCalculationRequest request = new OxygenCalculationRequest(CRUISE_NOT_FOUND, CALCULATION_DATE_TIME_VALID);
        LocalDateTime parsedDate = LocalDateTime.of(2085, 1, 31, 8, 0);

        when(localDateTimeParser.parse(CALCULATION_DATE_TIME_VALID)).thenReturn(parsedDate);
        when(cruiseRepository.findById(any(CruiseId.class))).thenReturn(null);

        assertThrows(CruiseNotFoundException.class, () ->
                oxygenService.calculateRemainingOxygen(request)
        );
        verify(localDateTimeParser).parse(CALCULATION_DATE_TIME_VALID);
        verify(cruiseRepository).findById(any(CruiseId.class));
    }
}
