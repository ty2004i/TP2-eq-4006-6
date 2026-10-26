package uspace.application.cruise.hyperdrive.dtos;

import java.util.List;

public class MaintenanceDto {

    public final int duration;

    public final List<String> employeeIds;

    public MaintenanceDto(int duration, List<String> employeeIds) {
        this.duration = duration;
        this.employeeIds = List.copyOf(employeeIds);
    }
}