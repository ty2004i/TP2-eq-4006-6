package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleException;

@Provider
public class InvalidHyperdriveModuleExceptionMapper implements ExceptionMapper<InvalidHyperdriveModuleException> {

    @Override
    public Response toResponse(InvalidHyperdriveModuleException exception) {
        ErrorResponse error = new ErrorResponse("INVALID_HYPERDRIVE_MODULE", exception.getMessage());
        return Response.status(400).entity(error).build();
    }
}
