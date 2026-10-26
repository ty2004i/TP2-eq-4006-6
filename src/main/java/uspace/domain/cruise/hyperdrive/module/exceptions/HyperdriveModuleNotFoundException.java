package uspace.domain.cruise.hyperdrive.module.exceptions;

public class HyperdriveModuleNotFoundException extends RuntimeException {

    public HyperdriveModuleNotFoundException() {
        super("Hyperdrive module not found in cruise");
    }
}