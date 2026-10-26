package uspace.api.cruise.hyperdrive;

import uspace.application.cruise.hyperdrive.dtos.NewMaintenanceDto;
import uspace.domain.exceptions.InvalidParameterException;
import uspace.domain.exceptions.MissingParameterException;

public class NewMaintenanceDtoValidator {

    public void validate(NewMaintenanceDto dto) {
        if (dto.duration == 0) {
            throw new MissingParameterException("duration");
        }

        if (dto.duration < 0) {
            throw new InvalidParameterException(
                    "Maintenance duration must be greater than 0");
        }

        if (dto.employeeIds == null || dto.employeeIds.isEmpty()) {
            throw new MissingParameterException("employeeIds");
        }
    }
}