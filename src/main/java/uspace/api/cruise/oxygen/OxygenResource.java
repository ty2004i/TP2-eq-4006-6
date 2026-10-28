package uspace.api.cruise.oxygen;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.oxygen.OxygenResponseDto;
import uspace.application.cruise.oxygen.OxygenService;

@Path("/cruise/{cruiseId}/oxygen")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OxygenResource {

    private final OxygenCalculationRequestValidator validator;

    private final OxygenService oxygenService;

    @Inject
    public OxygenResource(OxygenCalculationRequestValidator validator, OxygenService oxygenService) {
        this.validator = validator;
        this.oxygenService = oxygenService;
    }

    @GET
    public Response getRemainingOxygen(
            @PathParam("cruiseId") String cruiseId,
            @QueryParam("calculationDateTime") String calculationDateTime
    ) {
        OxygenCalculationRequest oxygenCalculationRequest =
                new OxygenCalculationRequest(cruiseId, calculationDateTime);
        validator.validate(oxygenCalculationRequest);

        OxygenResponseDto oxygenResponseDto = oxygenService.calculateRemainingOxygen(oxygenCalculationRequest);

        return Response.ok(oxygenResponseDto).build();
    }
}
