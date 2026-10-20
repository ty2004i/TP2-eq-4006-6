package uspace.domain.cruise.hyperdrive.module.exceptions;

public class HyperdriveModuleAlreadyExistsException extends RuntimeException {

    public HyperdriveModuleAlreadyExistsException() {
        super("Hyperdrive module already exists in the cruise");
    }
}
