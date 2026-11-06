package uspace.domain.cruise.hyperdrive.module.maintenance;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import uspace.domain.cruise.hyperdrive.module.HyperdrivePowerLevel;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidMaintenanceDurationException;
import uspace.domain.exceptions.InvalidParameterException;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int duration;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "maintenance_employees", joinColumns = @JoinColumn(name = "maintenance_id"))
    @Column(name = "employee_id", nullable = false)
    @OrderColumn(name = "employee_position")
    private List<String> employeeIds = new ArrayList<>();

    protected Maintenance() {
    }

    public Maintenance(int duration, List<String> employeeIds) {
        if (duration <= 0) {
            throw new InvalidParameterException("Maintenance duration must be greater than 0");
        }

        this.duration = duration;
        this.employeeIds.addAll(List.copyOf(employeeIds));
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
        return List.copyOf(employeeIds);
    }
}