package objectclasses;

import interfaces.Fray_Race;

public class ElfRace implements Fray_Race {	
	
	private String raceName = "Elf";
	private String racePassive = "";

	@Override
	public void setRacePassive(String racePassive) {
		this.racePassive = racePassive;		
	}

	@Override
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	
	@Override
	public String toString() {
		return "Race Name: " + raceName + ", Race Passive: " + racePassive;
	}
}
