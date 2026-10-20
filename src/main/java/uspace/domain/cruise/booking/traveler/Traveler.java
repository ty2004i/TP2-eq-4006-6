package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.*;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Traveler {

    @Id
    protected TravelerId id;

    private TravelerName name;

    @Enumerated(EnumType.STRING)
    private TravelerCategory category;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Badge> badges;

    protected Traveler() {

    }

    protected Traveler(TravelerId id, TravelerName name, TravelerCategory category, List<Badge> badges)
    {
        this.id = id;
        this.name = name;
        this.category = category;
        this.badges = badges;
    }

    public TravelerId getId() {
        return id;
    }

    public TravelerName getName() {
        return name;
    }

    public TravelerCategory getCategory() {
        return category;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public abstract void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience,
                                                   List<Traveler> travelersInBooking);

    public abstract boolean canAccompagnyChildForZeroGravityExperience(ZeroGravityExperience zeroGravityExperience);

    protected void earnOneTimeBadge(Badge badge) {
        if (!badges.contains(badge))
        {
            badges.add(badge);
        }
    }
}
