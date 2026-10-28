package uspace.domain.cruise.oxygen.exceptions;

public class OxygenCalculationDateException extends RuntimeException {
    public OxygenCalculationDateException() {
        super("Calculation date must be within the cruise timeframe");
    }
}
