package uspace.application.account;

import uspace.domain.account.Account;
import uspace.application.account.dtos.AccountResponseDto;
import uspace.domain.cruise.booking.BookingId;

import java.util.List;

public class AccountAssembler {

    public AccountResponseDto toDto(Account account) {
        List<String> bookingIdList = getBookingIds(account);

        return new AccountResponseDto(
                account.getUsername().getValue(),
                account.getHomePlanetName().getValue(),
                bookingIdList
        );
    }

    private List<String> getBookingIds(Account account) {
        return account.getBookingIds().stream().map(BookingId::toString).toList();
    }
}
