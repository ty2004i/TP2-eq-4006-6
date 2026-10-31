package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.oxygen.exceptions.OxygenCalculationDateException;

@Provider
public class OxygenCalculationDateExceptionMapper implements ExceptionMapper<OxygenCalculationDateException> {

    @Override
    public Response toResponse(OxygenCalculationDateException exception) {
        ErrorResponse error = new ErrorResponse("OXYGEN CALCULATION DATE OUTSIDE CRUISE", exception.getMessage());
        return Response.status(400).entity(error).build();
    }
}
