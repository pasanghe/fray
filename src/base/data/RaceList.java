package base.data;

/**
 *
 * @author Sang Heon, Park (991517263)
 */
public enum RaceList implements base.system.InterfaceRace {
    HUMAN(ObjectType.RACE, "Human", 15),
    ELF(ObjectType.RACE, "Elf", 15);
    
    private base.data.ObjectType raceType;
    private String raceName;
    private int raceHealthPoint;

    RaceList(ObjectType raceType, String raceName, int raceHealthPoint) {
        this.raceType = raceType;
        this.raceName = raceName;
        this.raceHealthPoint = raceHealthPoint;
    }

    @Override
    public ObjectType getType() {
        return this.raceType;
    }

    @Override
    public String getName() {
        return this.raceName;
    }

    @Override
    public int getHealthPoint() {
        return this.raceHealthPoint;
    }
}
