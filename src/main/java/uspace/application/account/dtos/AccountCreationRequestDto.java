package uspace.application.account.dtos;

public class AccountCreationRequestDto {
    public String username;

    public String homePlanetName;

    public AccountCreationRequestDto(String username, String homePlanetName) {
        this.username = username;
        this.homePlanetName = homePlanetName;
    }
}
