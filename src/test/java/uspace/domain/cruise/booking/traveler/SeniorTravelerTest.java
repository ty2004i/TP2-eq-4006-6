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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SeniorTravelerTest {
    private static final TravelerId ANY_ID = new TravelerId();
    private static final TravelerName ANY_NAME = new TravelerName("Bob");
    private static final List<Badge> ANY_BADGES = new ArrayList<>();
    private static final List<Badge> NO_BADGE = new ArrayList<>();
    private static final List<Traveler> ANY_TRAVELERS_IN_BOOKING = List.of();
    private SeniorTraveler seniorTraveler;
    @Mock
    private ZeroGravityExperience zeroGravityExperience;

    @BeforeEach
    void createSeniorTraveler() {
        seniorTraveler = new SeniorTraveler(ANY_ID, ANY_NAME, ANY_BADGES);
    }

    @Test
    void whenCreate_thenHasTravelerCategorySenior() {
        TravelerCategory expectedCategory = TravelerCategory.SENIOR;

        TravelerCategory actualCategory = seniorTraveler.getCategory();

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void whenBookZeroGravityExperience_thenZeroGravityExperienceIsBooked() {
        seniorTraveler.bookZeroGravityExperience(zeroGravityExperience, ANY_TRAVELERS_IN_BOOKING);

        verify(zeroGravityExperience).book(ANY_ID);
    }

    @Test
    void givenNoZeroGBadge_whenBookZeroGravityExperience_thenEarnZeroGBadge() {
        SeniorTraveler seniorTravelerWithoutBadge = new SeniorTraveler(ANY_ID, ANY_NAME, NO_BADGE);

        seniorTravelerWithoutBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_TRAVELERS_IN_BOOKING);

        assertTrue(seniorTravelerWithoutBadge.getBadges().contains(Badge.ZERO_G));
    }

    @Test
    void givenZeroGBadge_whenBookZeroGravityExperience_thenDoNotEarnSecondZeroGBadge() {
        List<Badge> badgesWithZeroG = new ArrayList<>(List.of(Badge.ZERO_G));
        SeniorTraveler seniorTravelerWithZeroGBadge = new SeniorTraveler(ANY_ID, ANY_NAME, badgesWithZeroG);
        int expectedNumberOfZeroGBadges = 1;

        seniorTravelerWithZeroGBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_TRAVELERS_IN_BOOKING);

        int numberOfZeroGBadges = (int) seniorTravelerWithZeroGBadge.getBadges().stream().filter(Badge.ZERO_G::equals).count();
        assertEquals(expectedNumberOfZeroGBadges, numberOfZeroGBadges);
    }

    @Test
    void givenNoStillGotItBadge_whenBookZeroGravityExperience_thenEarnStillGotItBadge() {
        SeniorTraveler seniorTravelerWithoutBadge = new SeniorTraveler(ANY_ID, ANY_NAME, NO_BADGE);

        seniorTravelerWithoutBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_TRAVELERS_IN_BOOKING);

        assertTrue(seniorTravelerWithoutBadge.getBadges().contains(Badge.STILL_GOT_IT));
    }

    @Test
    void givenStillGotItBadge_whenBookZeroGravityExperience_thenDoNotEarnSecondStillGotItBadge() {
        List<Badge> badgesWithStillGotIt = new ArrayList<>(List.of(Badge.STILL_GOT_IT));
        SeniorTraveler seniorTravelerWithStillGotItBadge = new SeniorTraveler(ANY_ID, ANY_NAME, badgesWithStillGotIt);
        int expectedNumberOfStillGotItBadges = 1;

        seniorTravelerWithStillGotItBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_TRAVELERS_IN_BOOKING);

        int numberOfStillGotItBadges = (int) seniorTravelerWithStillGotItBadge.getBadges().stream().filter(Badge.STILL_GOT_IT::equals).count();
        assertEquals(expectedNumberOfStillGotItBadges, numberOfStillGotItBadges);
    }
}