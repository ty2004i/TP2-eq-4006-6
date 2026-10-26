package uspace.api.cruise.hyperdrive;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.hyperdrive.MaintenanceService;
import uspace.application.cruise.hyperdrive.dtos.NewMaintenanceDto;

@Path("/cruises/{cruiseId}/hyperdrive-modules/{moduleId}/maintenances")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MaintenanceResource {

    private final MaintenanceService maintenanceService;

    private final NewMaintenanceDtoValidator validator;

    @Inject
    public MaintenanceResource(
            MaintenanceService maintenanceService,
            NewMaintenanceDtoValidator validator) {
        this.maintenanceService = maintenanceService;
        this.validator = validator;
    }

    @POST
    public Response addMaintenance(
            @PathParam("cruiseId") String cruiseId,
            @PathParam("moduleId") String moduleId,
            NewMaintenanceDto dto) {
        validator.validate(dto);

        maintenanceService.addMaintenance(cruiseId, moduleId, dto);

        return Response.status(Response.Status.CREATED).build();
    }
}