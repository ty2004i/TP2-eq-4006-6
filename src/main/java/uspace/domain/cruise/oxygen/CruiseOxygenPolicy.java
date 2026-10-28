package uspace.domain.cruise.oxygen;

public record CruiseOxygenPolicy(
        double thresholdRatio,
        double passengerSurchargeRate,
        double safetyMarginRate
) {
    public CruiseOxygenPolicy {
        if (thresholdRatio <= 0 || thresholdRatio > 1.0) {
            throw new IllegalArgumentException("Threshold ratio must be between 0 and 1");
        }
        if (passengerSurchargeRate < 0 || safetyMarginRate < 0) {
            throw new IllegalArgumentException("Rates must be positive or zero");
        }
    }

    public static CruiseOxygenPolicy createDefaultJupiterPolicy() {
        return new CruiseOxygenPolicy(0.75, 0.05, 0.10);
    }
}
