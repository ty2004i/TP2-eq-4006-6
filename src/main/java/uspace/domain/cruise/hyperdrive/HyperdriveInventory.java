package uspace.domain.cruise.hyperdrive;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleAlreadyExistsException;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleConflictException;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleIdException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleIdValidator;

import java.util.List;

@Entity
public class HyperdriveInventory {
    @Id
    private HyperdriveInventoryId id;

    @OneToMany(fetch = FetchType.EAGER, cascade = jakarta.persistence.CascadeType.ALL)
    private List<HyperdriveModule> hyperdriveModules;

    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    private HyperdriveModuleIdValidator hyperdriveModuleIdValidator;

    public HyperdriveInventory() { }

    public HyperdriveInventory(HyperdriveInventoryId id,
                               List<HyperdriveModule> hyperdriveModules,
                               HyperdriveModuleIdValidator hyperdriveModuleIdValidator) {
        this.id = id;
        this.hyperdriveModules = hyperdriveModules;
        this.hyperdriveModuleIdValidator = hyperdriveModuleIdValidator;
    }

    public List<HyperdriveModule> getAllHyperdriveModules() {
        return hyperdriveModules;
    }

    public void addHyperdriveModule(HyperdriveModule hyperdriveModule) {
        if (!hyperdriveModuleIdValidator.isValid(hyperdriveModule.getId())) {
            throw new InvalidHyperdriveModuleIdException();
        }

        if (containsHyperdriveModuleId(hyperdriveModule)) {
            throw new HyperdriveModuleAlreadyExistsException();
        }

        if (containsHyperDriveModuleWithSameActivationPeriod(hyperdriveModule)) {
            throw new HyperdriveModuleConflictException();
        }

        hyperdriveModules.add(hyperdriveModule);
    }

    private boolean containsHyperdriveModuleId(HyperdriveModule newHyperdriveModule) {
        return hyperdriveModules.stream().anyMatch(module -> module.hasSameIdThan(newHyperdriveModule));
    }

    private boolean containsHyperDriveModuleWithSameActivationPeriod(HyperdriveModule newHyperdriveModule) {
        return hyperdriveModules.stream().anyMatch(module -> module.hasSameActivationPeriodThan(newHyperdriveModule));
    }
}
