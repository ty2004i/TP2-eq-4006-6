package uspace.application.cruise.hyperdrive.dtos;

public class NewHyperdriveModuleDto {

    public String id;
    public int powerLevel;
    public int activationDays;
    public String activationDate;

    public NewHyperdriveModuleDto() {
    }

    public NewHyperdriveModuleDto(String id, int powerLevel,
                                  int activationDays, String activationDate) {
        this.id = id;
        this.powerLevel = powerLevel;
        this.activationDays = activationDays;
        this.activationDate = activationDate;
    }
}
