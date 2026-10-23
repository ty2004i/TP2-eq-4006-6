package uspace.domain.cruise.crew;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import uspace.domain.cruise.booking.traveler.TravelerName;

@Entity
public class CrewMember {

    @EmbeddedId
    private EmployeeId employeeId;

    @Embedded
    private TravelerName name;

    protected CrewMember() {
    }

    public CrewMember(String employeeId, TravelerName name) {
        this.employeeId = new EmployeeId(employeeId);
        this.name = name;
    }

    public String getEmployeeId() {
        return employeeId.toString();
    }

    public TravelerName getName() {
        return name;
    }
}