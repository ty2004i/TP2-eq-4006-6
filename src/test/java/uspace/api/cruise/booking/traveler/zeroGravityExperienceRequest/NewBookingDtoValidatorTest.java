package uspace.api.cruise.booking.traveler.zeroGravityExperienceRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.api.cruise.booking.NewBookingDtoValidator;
import uspace.application.cruise.booking.dtos.newBooking.NewBookingDto;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NewBookingDtoValidatorTest {

    private NewBookingDtoValidator validator;

    private NewBookingDto newBookingDto;

    @BeforeEach
    void setup() {
        validator = new NewBookingDtoValidator();

        newBookingDto = new NewBookingDto();
    }

    @Test
    void givenMissingAccountUsername_whenValidate_thenThrowsMissingParameter() {
        newBookingDto.accountUsername = null;

        assertThrows(MissingParameterException.class, () ->
                validator.validate(newBookingDto));
    }
}
