package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleAlreadyExistsException;

@Provider
public class HyperdriveModuleAlreadyExistsExceptionMapper
    implements ExceptionMapper<HyperdriveModuleAlreadyExistsException> {

    @Override
    public Response toResponse(HyperdriveModuleAlreadyExistsException exception) {
        ErrorResponse error = new ErrorResponse("MODULE_ALREADY_EXISTS", exception.getMessage());
        return Response.status(400).entity(error).build();
    }
}
