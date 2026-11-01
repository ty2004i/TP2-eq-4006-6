package uspace.api.acount;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.account.AccountService;
import uspace.application.account.dtos.AccountCreationRequestDto;
import uspace.application.account.dtos.AccountResponseDto;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    private final AccountService accountService;
    private final AccountCreationRequestValidator validator;

    @Inject
    public AccountResource(AccountService accountService, AccountCreationRequestValidator validator) {
        this.accountService = accountService;
        this.validator = validator;
    }

    @POST
    public Response createAccount(AccountCreationRequestDto request) {
        validator.validate(request);
        accountService.createAccount(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{username}")
    public Response getAccount(@PathParam("username") String username) {
        AccountResponseDto accountDto = accountService.getAccountByUsername(username);
        return Response.ok(accountDto).build();
    }
}
