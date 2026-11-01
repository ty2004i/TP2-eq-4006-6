package uspace.domain.cruise.hyperdrive.module;

import jakarta.persistence.Embeddable;

@Embeddable
public class HyperdrivePowerLevel {

    private int powerLevel;

    public HyperdrivePowerLevel () {

    }

    public HyperdrivePowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public int toInt() {
        return powerLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        HyperdrivePowerLevel other = (HyperdrivePowerLevel) obj;
        return this.powerLevel == other.powerLevel;
    } }
