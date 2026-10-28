package uspace.api.cruise.oxygen;

public class OxygenCalculationRequest {
    public String cruiseId;

    public String calculationDateTime;

    public OxygenCalculationRequest(String cruiseId, String calculationDateTime) {
        this.cruiseId = cruiseId;
        this.calculationDateTime = calculationDateTime;
    }
}
