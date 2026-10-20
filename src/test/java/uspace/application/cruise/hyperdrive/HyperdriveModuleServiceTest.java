package uspace.application.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.hyperdrive.dtos.NewHyperdriveModuleDto;
import uspace.application.utils.dateTimeParser.LocalDateParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.hyperdrive.HyperdriveStabilityValidator;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModuleFactory;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HyperdriveModuleServiceTest {
    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);
    private static final String ANY_HYPERDRIVE_MODULE_ID = "ANY_HYPERDRIVE_MODULE_ID";
    private static final int ANY_POWER_LEVEL = 15;
    private static final int ANY_ACTIVATION_DAYS = 40;
    private static final String ANY_ACTIVATION_DATE = "1999-02-03";
    private static final LocalDate ANY_ACTIVATION_LOCAL_DATE = LocalDate.of(1999,2,3);
    private static final NewHyperdriveModuleDto ANY_NEW_HYPERDRIVE_MODULE_DTO = new NewHyperdriveModuleDto(ANY_HYPERDRIVE_MODULE_ID, ANY_POWER_LEVEL, ANY_ACTIVATION_DAYS, ANY_ACTIVATION_DATE);
    private HyperdriveModuleService hyperdriveModuleService;
    @Mock
    private CruiseRepository cruiseRepositoryMock;
    @Mock
    private HyperdriveStabilityValidator hyperdriveStabilityValidatorMock;
    @Mock
    private HyperdriveModuleFactory hyperdriveModuleFactoryMock;
    @Mock
    private LocalDateParser localDateParserMock;
    @Mock
    private Cruise cruiseMock;
    @Mock
    private HyperdriveModule hyperdriveModuleMock;

    @BeforeEach
    void createHyperdriveModuleService() {
        hyperdriveModuleService = new HyperdriveModuleService(cruiseRepositoryMock, hyperdriveStabilityValidatorMock, hyperdriveModuleFactoryMock, localDateParserMock);
    }

    @Test
    void givenNonExistingCruise_whenAddHyperdriveModule_thenThrowCruiseNotFoundException(){
        givenNonExistingCruise();

        Executable addHyperdriveModule = () -> hyperdriveModuleService.addHyperdriveModule(ANY_CRUISE_ID_STR, ANY_NEW_HYPERDRIVE_MODULE_DTO);

        assertThrows(CruiseNotFoundException.class, addHyperdriveModule);
    }

    @Test
    void givenExistingCruiseAndHyperdriveModuleNotStable_whenAddHyperdriveModule_thenThrowInvalidHyperdriveModuleException(){
        givenExistingCruise();
        givenInvalidHyperdriveModule();

        Executable addHyperdriveModule = () -> hyperdriveModuleService.addHyperdriveModule(ANY_CRUISE_ID_STR, ANY_NEW_HYPERDRIVE_MODULE_DTO);

        assertThrows(InvalidHyperdriveModuleException.class, addHyperdriveModule);
    }

    @Test
    void givenExistingCruiseAndValidHyperdriveModuleStable_whenAddHyperdriveModule_thenAddToCruise(){
        givenExistingCruise();
        givenValidHyperdriveModule();

        hyperdriveModuleService.addHyperdriveModule(ANY_CRUISE_ID_STR, ANY_NEW_HYPERDRIVE_MODULE_DTO);

        verify(cruiseMock).addHyperdriveModule(hyperdriveModuleMock);
    }

    @Test
    void givenExistingCruiseAndValidHyperdriveModuleStable_whenAddHyperdriveModule_thenSaveCruise(){
        givenExistingCruise();
        givenValidHyperdriveModule();

        hyperdriveModuleService.addHyperdriveModule(ANY_CRUISE_ID_STR, ANY_NEW_HYPERDRIVE_MODULE_DTO);

        verify(cruiseRepositoryMock).save(cruiseMock);
    }

    private void givenNonExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);
    }

    private void givenExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
    }

    private void givenInvalidHyperdriveModule() {
        when(hyperdriveStabilityValidatorMock.isHyperdriveModuleStable(ANY_HYPERDRIVE_MODULE_ID)).thenReturn(false);
    }

    private void givenValidHyperdriveModule() {
        when(hyperdriveStabilityValidatorMock.isHyperdriveModuleStable(ANY_HYPERDRIVE_MODULE_ID)).thenReturn(true);
        when(localDateParserMock.parse(ANY_ACTIVATION_DATE)).thenReturn(ANY_ACTIVATION_LOCAL_DATE);
        when(hyperdriveModuleFactoryMock.create(ANY_HYPERDRIVE_MODULE_ID, ANY_POWER_LEVEL, ANY_ACTIVATION_DAYS, ANY_ACTIVATION_LOCAL_DATE)).thenReturn(hyperdriveModuleMock);
    }

}