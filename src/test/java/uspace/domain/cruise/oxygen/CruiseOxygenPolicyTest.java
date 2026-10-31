package uspace.domain.cruise.oxygen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CruiseOxygenPolicyTest {

    private static final double INVALID_THRESHOLD = 1.5;

    private static final double NEGATIVE_SURCHARGE = -0.05;

    private static final double NEGATIVE_MARGIN = -0.10;

    private static final double DEFAULT_THRESHOLD_RATIO = 0.75;

    private static final double DEFAULT_PASSENGER_SURCHARGE = 0.05;

    private static final double DEFAULT_SAFETY_MARGIN = 0.10;

    @Test
    void givenDefaultPolicy_whenInstantiated_thenValuesAreCorrect() {
        CruiseOxygenPolicy policy = CruiseOxygenPolicy.defaultPolicy();

        assertEquals(DEFAULT_THRESHOLD_RATIO, policy.getThresholdRatio());
        assertEquals(DEFAULT_PASSENGER_SURCHARGE, policy.getPassengerSurcharge());
        assertEquals(DEFAULT_SAFETY_MARGIN, policy.getSafetyMargin());
    }

    @Test
    void givenBoundaryValidValues_whenInstantiate_thenSuccess() {
        CruiseOxygenPolicy policy = new CruiseOxygenPolicy(1.0, 0.0, 0.0);

        assertEquals(1.0, policy.getThresholdRatio());
        assertEquals(0.0, policy.getPassengerSurcharge());
        assertEquals(0.0, policy.getSafetyMargin());
    }

    @Test
    void givenInvalidThresholdRatio_whenInstantiate_thenThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CruiseOxygenPolicy(INVALID_THRESHOLD, DEFAULT_PASSENGER_SURCHARGE, DEFAULT_SAFETY_MARGIN)
        );

        assertEquals("Threshold ratio must be between 0 and 1", exception.getMessage());
    }

    @Test
    void givenNegativePassengerSurcharge_whenInstantiate_thenThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CruiseOxygenPolicy(DEFAULT_THRESHOLD_RATIO, NEGATIVE_SURCHARGE, DEFAULT_SAFETY_MARGIN)
        );

        assertEquals("Rates must be positive or zero", exception.getMessage());
    }

    @Test
    void givenNegativeSafetyMargin_whenInstantiate_thenThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CruiseOxygenPolicy(DEFAULT_THRESHOLD_RATIO, DEFAULT_PASSENGER_SURCHARGE, NEGATIVE_MARGIN)
        );

        assertEquals("Rates must be positive or zero", exception.getMessage());
    }

    @Test
    void givenIdenticalPolicies_whenEquals_thenTrue() {
        CruiseOxygenPolicy policyA = new CruiseOxygenPolicy(0.80, DEFAULT_PASSENGER_SURCHARGE, DEFAULT_SAFETY_MARGIN);
        CruiseOxygenPolicy policyB = new CruiseOxygenPolicy(0.80, DEFAULT_PASSENGER_SURCHARGE, DEFAULT_SAFETY_MARGIN);

        assertEquals(policyA, policyB);
        assertEquals(policyA.hashCode(), policyB.hashCode());
    }
}
