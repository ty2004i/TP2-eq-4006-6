package uspace.application.account.dtos;

import java.util.List;

public class AccountResponseDto {

    public String username;

    public String homePlanetName;

    public List<String> bookingIds;


    public AccountResponseDto(String username, String homePlanetName, List<String> bookingIds) {
        this.username = username;
        this.homePlanetName = homePlanetName;
        this.bookingIds = bookingIds;
    }
}
