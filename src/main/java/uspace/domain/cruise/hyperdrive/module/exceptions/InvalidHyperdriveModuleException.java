package uspace.domain.cruise.hyperdrive.module.exceptions;

public class InvalidHyperdriveModuleException extends RuntimeException {

    public InvalidHyperdriveModuleException() {
        super("Hyperdrive module invalid");
    }
}
