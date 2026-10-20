package uspace.application.utils.dateTimeParser;

import jakarta.inject.Inject;
import uspace.domain.exceptions.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateParser {

    private final DateTimeFormatter dateTimeFormatter;

    @Inject
    public LocalDateParser(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public LocalDate parse(String dateTimeStr) {
        LocalDate dateTime;
        try {
            dateTime = LocalDate.parse(dateTimeStr, dateTimeFormatter);
        } catch (Exception e) {
            throw new InvalidDateFormatException();
        }

        return dateTime;
    }
}
