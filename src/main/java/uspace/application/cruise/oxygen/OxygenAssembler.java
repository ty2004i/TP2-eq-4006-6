package uspace.application.cruise.oxygen;

import uspace.application.cruise.oxygen.dto.OxygenResponseDto;
import uspace.domain.cruise.oxygen.Oxygen;

public class OxygenAssembler {

    public OxygenResponseDto toDto(Oxygen oxygen) {
        return new OxygenResponseDto(oxygen.value());
    }
}
