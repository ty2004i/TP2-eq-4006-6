package uspace.domain.cruise.hyperdrive.module.maintenance;

import uspace.domain.cruise.hyperdrive.module.HyperdrivePowerLevel;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidMaintenanceDurationException;
import uspace.domain.exceptions.InvalidParameterException;

import java.util.List;

public class Maintenance {

    private final int duration;

    private final List<String> employeeIds;

    public Maintenance(int duration, List<String> employeeIds) {
        if (duration <= 0) {
            throw new InvalidParameterException("Maintenance duration must be greater than 0");
        }

        this.duration = duration;
        this.employeeIds = List.copyOf(employeeIds);
    }

    public void validateForPowerLevel(HyperdrivePowerLevel powerLevel) {
        if (duration < powerLevel.toInt()) {
            throw new InvalidMaintenanceDurationException();
        }
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getEmployeeIds() {
        return employeeIds;
    }
}