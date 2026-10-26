package uspace.domain.cruise.crew.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException() {
        super("One or more employees are not part of the cruise");
    }
}