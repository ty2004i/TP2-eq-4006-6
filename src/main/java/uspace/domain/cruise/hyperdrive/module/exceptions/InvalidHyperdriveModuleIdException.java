package uspace.domain.cruise.hyperdrive.module.exceptions;

public class InvalidHyperdriveModuleIdException extends RuntimeException {

    public InvalidHyperdriveModuleIdException() {
        super("Invalid hyperdrive module id format");
    }
}
