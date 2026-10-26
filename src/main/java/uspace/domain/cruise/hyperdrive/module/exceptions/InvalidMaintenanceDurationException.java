package uspace.domain.cruise.hyperdrive.module.exceptions;

public class InvalidMaintenanceDurationException extends RuntimeException {

    public InvalidMaintenanceDurationException() {
        super("Maintenance must have a duration greater or equal to the module power level");
    }
}