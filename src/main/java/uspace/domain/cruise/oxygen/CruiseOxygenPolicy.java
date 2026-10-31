package uspace.domain.cruise.oxygen;

import java.util.Objects;

public class CruiseOxygenPolicy {

    private static final double DEFAULT_THRESHOLD_RATIO = 0.75;

    private static final double DEFAULT_PASSENGER_SURCHARGE = 0.05;

    private static final double DEFAULT_SAFETY_MARGIN = 0.10;

    private final double thresholdRatio;

    private final double passengerSurcharge;

    private final double safetyMargin;

    public CruiseOxygenPolicy(double thresholdRatio, double passengerSurcharge, double safetyMargin) {

        this.thresholdRatio = thresholdRatio;
        this.passengerSurcharge = passengerSurcharge;
        this.safetyMargin = safetyMargin;

        if (thresholdRatio <= 0 || thresholdRatio > 1.0) {
            throw new IllegalArgumentException("Threshold ratio must be between 0 and 1");
        }
        if (passengerSurcharge < 0 || safetyMargin < 0) {
            throw new IllegalArgumentException("Rates must be positive or zero");
        }
    }

    public static CruiseOxygenPolicy defaultPolicy() {

        return new CruiseOxygenPolicy(DEFAULT_THRESHOLD_RATIO, DEFAULT_PASSENGER_SURCHARGE, DEFAULT_SAFETY_MARGIN);
    }

    public double getThresholdRatio() {

        return thresholdRatio;
    }

    public double getPassengerSurcharge() {

        return passengerSurcharge;
    }

    public double getSafetyMargin() {

        return safetyMargin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CruiseOxygenPolicy that)) return false;
        return Double.compare(that.thresholdRatio, thresholdRatio) == 0 &&
                Double.compare(that.passengerSurcharge, passengerSurcharge) == 0 &&
                Double.compare(that.safetyMargin, safetyMargin) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(thresholdRatio, passengerSurcharge, safetyMargin);
    }
}

