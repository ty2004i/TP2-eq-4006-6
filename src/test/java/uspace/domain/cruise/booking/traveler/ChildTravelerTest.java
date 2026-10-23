package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.booking.traveler.exceptions.ZeroGravityExperienceChildCriteriaException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChildTravelerTest {
    private static final TravelerId ANY_ID = new TravelerId();

    private static final TravelerId ANY_ADULT_ID = new TravelerId();

    private static final TravelerId ANY_SENIOR_ID = new TravelerId();

    private static final TravelerName ANY_NAME = new TravelerName("Bob");

    private static final List<Badge> ANY_BADGES = new ArrayList<>();

    private static final List<Badge> NO_BADGE = new ArrayList<>();

    private static final List<Badge> BADGES_WITH_MINI_ZERO_G = List.of(Badge.MINI_ZERO_G);

    private static final AdultTraveler ANY_ADULT_TRAVELER = new AdultTraveler(ANY_ADULT_ID, ANY_NAME, ANY_BADGES);

    private static final SeniorTraveler ANY_SENIOR_TRAVELER = new SeniorTraveler(ANY_SENIOR_ID, ANY_NAME, ANY_BADGES);

    private static final List<Traveler> ANY_ADULTS_AND_SENIORS_IN_BOOKING = List.of(ANY_ADULT_TRAVELER,
            ANY_SENIOR_TRAVELER);

    private ChildTraveler childTraveler;

    @Mock
    private ZeroGravityExperience zeroGravityExperience;

    @BeforeEach
    void createChildTraveler() {
        childTraveler = new ChildTraveler(ANY_ID, ANY_NAME, ANY_BADGES);
    }

    @Test
    void whenCreate_thenHasTravelerCategoryChild() {
        TravelerCategory expectedCategory = TravelerCategory.CHILD;

        TravelerCategory actualCategory = childTraveler.getCategory();

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void givenNoAdultOrSenior_whenBooking_thenThrowsCriteriaException() {
        List<Traveler> noAdultAndNoSenior = List.of();

        Executable bookZeroGravityExperience =
                () -> childTraveler.bookZeroGravityExperience(zeroGravityExperience, noAdultAndNoSenior);

        assertThrows(ZeroGravityExperienceChildCriteriaException.class, bookZeroGravityExperience);
    }

    @Test
    void givenNoEligibleTraveler_whenBooking_thenThrowsCriteriaException() {
        givenTravelerWhoDidNotBookZeroGravityExperience(ANY_ADULT_ID);
        givenTravelerWhoDidNotBookZeroGravityExperience(ANY_SENIOR_ID);

        Executable bookZeroGravityExperience =
                () -> childTraveler.bookZeroGravityExperience(zeroGravityExperience, ANY_ADULTS_AND_SENIORS_IN_BOOKING);

        assertThrows(ZeroGravityExperienceChildCriteriaException.class, bookZeroGravityExperience);
    }

    @Test
    void givenAdultAlreadyBooked_whenBooking_thenExperienceBooked() {
        givenAdultWhoBookedZeroGravityExperience();

        childTraveler.bookZeroGravityExperience(zeroGravityExperience, ANY_ADULTS_AND_SENIORS_IN_BOOKING);

        verify(zeroGravityExperience).book(ANY_ID);
    }

    @Test
    void givenAdultWithoutMiniBadge_whenBooking_thenBadgeEarned() {
        givenAdultWhoBookedZeroGravityExperience();
        ChildTraveler childTravelerWithoutBadge = new ChildTraveler(ANY_ID, ANY_NAME, NO_BADGE);

        childTravelerWithoutBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_ADULTS_AND_SENIORS_IN_BOOKING);

        assertTrue(childTravelerWithoutBadge.getBadges().contains(Badge.MINI_ZERO_G));
    }

    @Test
    void givenAdultWithMiniBadge_whenBooking_thenBadgeNotEarnedAgain() {
        givenAdultWhoBookedZeroGravityExperience();
        ChildTraveler childTravelerWithZeroGBadge = new ChildTraveler(ANY_ID, ANY_NAME, BADGES_WITH_MINI_ZERO_G);
        int expectedNumberOfMiniZeroGBadges = 1;

        childTravelerWithZeroGBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_ADULTS_AND_SENIORS_IN_BOOKING);

        int numberOfMiniZeroGBadges =
                (int) childTravelerWithZeroGBadge.getBadges().stream().filter(Badge.MINI_ZERO_G::equals).count();
        assertEquals(expectedNumberOfMiniZeroGBadges, numberOfMiniZeroGBadges);
    }

    @Test
    void givenSeniorAlreadyBooked_whenBooking_thenExperienceBooked() {
        givenSeniorWhoBookedZeroGravityExperience();

        childTraveler.bookZeroGravityExperience(zeroGravityExperience, ANY_ADULTS_AND_SENIORS_IN_BOOKING);

        verify(zeroGravityExperience).book(ANY_ID);
    }

    @Test
    void givenSeniorWithoutMiniBadge_whenBooking_thenBadgeEarned() {
        givenSeniorWhoBookedZeroGravityExperience();
        ChildTraveler childTravelerWithoutBadge = new ChildTraveler(ANY_ID, ANY_NAME, NO_BADGE);

        childTravelerWithoutBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_ADULTS_AND_SENIORS_IN_BOOKING);

        assertTrue(childTravelerWithoutBadge.getBadges().contains(Badge.MINI_ZERO_G));
    }

    @Test
    void givenSeniorWithMiniBadge_whenBooking_thenBadgeNotEarnedAgain() {
        givenSeniorWhoBookedZeroGravityExperience();
        ChildTraveler childTravelerWithZeroGBadge = new ChildTraveler(ANY_ID, ANY_NAME, BADGES_WITH_MINI_ZERO_G);
        int expectedNumberOfMiniZeroGBadges = 1;

        childTravelerWithZeroGBadge.bookZeroGravityExperience(zeroGravityExperience, ANY_ADULTS_AND_SENIORS_IN_BOOKING);

        int numberOfMiniZeroGBadges =
                (int) childTravelerWithZeroGBadge.getBadges().stream().filter(Badge.MINI_ZERO_G::equals).count();
        assertEquals(expectedNumberOfMiniZeroGBadges, numberOfMiniZeroGBadges);
    }

    private void givenTravelerWhoDidNotBookZeroGravityExperience(TravelerId travelerId) {
        when(zeroGravityExperience.hasTravelerBooked(travelerId)).thenReturn(false);
    }

    private void givenAdultWhoBookedZeroGravityExperience() {
        when(zeroGravityExperience.hasTravelerBooked(ANY_ADULT_ID)).thenReturn(true);
    }

    private void givenSeniorWhoBookedZeroGravityExperience() {
        when(zeroGravityExperience.hasTravelerBooked(ANY_SENIOR_ID)).thenReturn(true);
        when(zeroGravityExperience.hasTravelerBooked(ANY_ADULT_ID)).thenReturn(false);
    }
}
