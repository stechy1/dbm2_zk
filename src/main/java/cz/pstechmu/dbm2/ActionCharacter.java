package cz.pstechmu.dbm2;

public enum ActionCharacter {

    // Primární
    PRI("Primarni"),
    // Sekundární
    SEK("Sekundarni"),
    // Sekundární plánovaný
    SEKP("Sekundarni planovany");

    public final String description;

    ActionCharacter(String description) {
        this.description = description;
    }
}
