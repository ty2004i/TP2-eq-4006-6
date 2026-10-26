package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleNotFoundException;

@Provider
public class HyperdriveModuleNotFoundExceptionMapper
        implements ExceptionMapper<HyperdriveModuleNotFoundException> {

    @Override
    public Response toResponse(HyperdriveModuleNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
                "HYPERDRIVE_MODULE_NOT_FOUND",
                exception.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}