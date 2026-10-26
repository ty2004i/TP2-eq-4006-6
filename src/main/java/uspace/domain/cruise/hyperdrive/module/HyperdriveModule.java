package uspace.domain.cruise.hyperdrive.module;

import jakarta.persistence.*;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.hyperdrive.module.maintenance.Maintenance;

import java.util.ArrayList;
import java.util.List;

@Entity
public class HyperdriveModule {
    @Id
    private HyperdriveModuleId id;

    @Embedded
    private HyperdrivePowerLevel powerLevel;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "localDateTime", column = @Column(name = "activationDateTime")),
    })
    private CruiseDateTime activationDateTime;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "localDateTime", column = @Column(name = "deactivationDateTime")),
    })
    private CruiseDateTime deactivationDateTime;

    private List<Maintenance> maintenances = new ArrayList<>();

    public HyperdriveModule() {

    }

    public HyperdriveModule(HyperdriveModuleId id, HyperdrivePowerLevel powerLevel, CruiseDateTime activationDateTime,
            CruiseDateTime deactivationDateTime) {
        this.id = id;
        this.powerLevel = powerLevel;
        this.activationDateTime = activationDateTime;
        this.deactivationDateTime = deactivationDateTime;
    }

    public HyperdriveModuleId getId() {
        return id;
    }

    public HyperdrivePowerLevel getPowerLevel() {
        return powerLevel;
    }

    public CruiseDateTime getActivationDateTime() {
        return activationDateTime;
    }

    public CruiseDateTime getDeactivationDateTime() {
        return deactivationDateTime;
    }

    public boolean hasSameIdThan(HyperdriveModule hyperdriveModule) {
        return id.equals(hyperdriveModule.id);
    }

    public boolean isActivationBetween(CruiseDateTime startDateTime, CruiseDateTime endDateTime) {
        return activationDateTime.isAfter(startDateTime) && deactivationDateTime.isBefore(endDateTime);
    }

    public boolean hasSameActivationPeriodThan(HyperdriveModule hyperdriveModule) {
        return isDuringActivation(hyperdriveModule.activationDateTime) ||
                isDuringActivation(hyperdriveModule.deactivationDateTime);
    }

    private boolean isDuringActivation(CruiseDateTime dateTime) {
        return dateTime.isAfter(activationDateTime) && dateTime.isBefore(deactivationDateTime);
    }

    public void addMaintenance(Maintenance maintenance) {
        maintenance.validateForPowerLevel(powerLevel);

        maintenances.add(maintenance);
    }

    public List<Maintenance> getMaintenances() {
        return List.copyOf(maintenances);
    }
}
