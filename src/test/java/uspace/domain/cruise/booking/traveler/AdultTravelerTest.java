package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdultTravelerTest {
    private static final TravelerId ANY_ID = new TravelerId();

    private static final TravelerName ANY_NAME = new TravelerName("Bob");

    private static final List<Badge> ANY_BADGES = new ArrayList<>();

    private static final List<Traveler> ANY_TRAVELERS_IN_BOOKING = List.of();

    private AdultTraveler adultTraveler;

    @Mock
    private ZeroGravityExperience zeroGravityExperience;

    @BeforeEach
    void createAdultTraveler() {
        adultTraveler = new AdultTraveler(ANY_ID, ANY_NAME, ANY_BADGES);
    }

    @Test
    void whenCreate_thenHasTravelerCategoryAdult() {
        TravelerCategory expectedCategory = TravelerCategory.ADULT;

        TravelerCategory actualCategory = adultTraveler.getCategory();

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void whenBookZeroGravityExperience_thenZeroGravityExperienceIsBooked() {
        adultTraveler.bookZeroGravityExperience(zeroGravityExperience, ANY_TRAVELERS_IN_BOOKING);

        verify(zeroGravityExperience).book(ANY_ID);
    }

    @Test
    void givenNoZeroGBadge_whenBookZeroGravityExperience_thenEarnZeroGBadge() {
        List<Badge> noBadge = new ArrayList<>();
        AdultTraveler adultTravelerWithoutBadge = new AdultTraveler(ANY_ID, ANY_NAME, noBadge);

        adultTravelerWithoutBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_TRAVELERS_IN_BOOKING);

        assertTrue(adultTravelerWithoutBadge.getBadges().contains(Badge.ZERO_G));
    }

    @Test
    void givenZeroGBadge_whenBookZeroGravityExperience_thenDoNotEarnSecondZeroGBadge() {
        List<Badge> badgesWithZeroG = List.of(Badge.ZERO_G);
        AdultTraveler adultTravelerWithZeroGBadge = new AdultTraveler(ANY_ID, ANY_NAME, badgesWithZeroG);
        int expectedNumberOfZeroGBadges = 1;

        adultTravelerWithZeroGBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_TRAVELERS_IN_BOOKING);

        int numberOfZeroGBadges =
                (int) adultTravelerWithZeroGBadge.getBadges().stream().filter(Badge.ZERO_G::equals).count();
        assertEquals(expectedNumberOfZeroGBadges, numberOfZeroGBadges);
    }
}
