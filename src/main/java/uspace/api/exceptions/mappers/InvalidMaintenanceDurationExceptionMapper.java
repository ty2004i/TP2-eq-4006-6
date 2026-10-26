package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidMaintenanceDurationException;

@Provider
public class InvalidMaintenanceDurationExceptionMapper
        implements ExceptionMapper<InvalidMaintenanceDurationException> {

    @Override
    public Response toResponse(InvalidMaintenanceDurationException exception) {
        ErrorResponse error = new ErrorResponse(
                "INVALID_MAINTENANCE_DURATION",
                exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}