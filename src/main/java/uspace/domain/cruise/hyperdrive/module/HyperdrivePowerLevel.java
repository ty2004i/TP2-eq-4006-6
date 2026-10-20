package uspace.domain.cruise.hyperdrive.module;

public class HyperdrivePowerLevel {

    private final int powerLevel;

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
    }}
