package uspace.application.cruise.hyperdrive.dtos;

public class HyperdriveModuleDto {

    public String id;
    public int powerLevel;
    public String activationDateTime;
    public String deactivationDateTime;


    public HyperdriveModuleDto(String id, int powerLevel,
                               String activationDateTime, String deactivationDateTime) {
        this.id = id;
        this.powerLevel = powerLevel;
        this.activationDateTime = activationDateTime;
        this.deactivationDateTime = deactivationDateTime;
    }
}
