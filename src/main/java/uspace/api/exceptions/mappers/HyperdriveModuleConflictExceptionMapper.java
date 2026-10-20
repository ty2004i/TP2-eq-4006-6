package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleConflictException;

@Provider
public class HyperdriveModuleConflictExceptionMapper implements ExceptionMapper<HyperdriveModuleConflictException> {

    @Override
    public Response toResponse(HyperdriveModuleConflictException exception) {
        ErrorResponse error = new ErrorResponse("MODULE_CONFLICT", exception.getMessage());
        return Response.status(400).entity(error).build();
    }
}
