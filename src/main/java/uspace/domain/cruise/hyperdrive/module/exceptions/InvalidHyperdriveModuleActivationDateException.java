package uspace.domain.cruise.hyperdrive.module.exceptions;

public class InvalidHyperdriveModuleActivationDateException extends RuntimeException {

    public InvalidHyperdriveModuleActivationDateException() {
        super("Activation date or deactivation date is outside the cruise timeframe");
    }
}
