package uspace.domain.cruise.dateTime;

import jakarta.persistence.Embeddable;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
public class CruiseDateTime {
    private LocalDateTime localDateTime;

    public CruiseDateTime() {

    }

    public CruiseDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public boolean isBefore(CruiseDateTime otherCruiseDateTime) {
        return localDateTime.isBefore(otherCruiseDateTime.localDateTime);
    }

    public boolean isAfter(CruiseDateTime otherCruiseDateTime) {
        return localDateTime.isAfter(otherCruiseDateTime.localDateTime);
    }

    public long durationInMinuteUntil(CruiseDateTime target) {
        return Duration.between(this.localDateTime, target.localDateTime).toMinutes();
    }

    @Override
    public String toString() {
        return localDateTime.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CruiseDateTime that = (CruiseDateTime) obj;
        return localDateTime.equals(that.localDateTime);
    }
}
