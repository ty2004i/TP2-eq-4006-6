package uspace.api.cruise.hyperdrive;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.hyperdrive.HyperdriveModuleService;
import uspace.application.cruise.hyperdrive.dtos.NewHyperdriveModuleDto;

@Path("/cruises/{cruiseId}/hyperdrive-modules")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HyperdriveModuleResource {

    private final HyperdriveModuleService hyperdriveModuleService;
    private final NewHyperdriveModuleValidator newHyperdriveModuleValidator;

    @Inject
    public HyperdriveModuleResource(HyperdriveModuleService hyperdriveModuleService, NewHyperdriveModuleValidator newHyperdriveModuleValidator) {
        this.hyperdriveModuleService = hyperdriveModuleService;
        this.newHyperdriveModuleValidator = newHyperdriveModuleValidator;
    }

    @POST
    public Response addHyperdriveModule(
            @PathParam("cruiseId") String cruiseId,
            NewHyperdriveModuleDto newHyperdriveModuleDto) {
        newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        hyperdriveModuleService.addHyperdriveModule(cruiseId, newHyperdriveModuleDto);

        return Response.status(Response.Status.CREATED).build();
    }
}
