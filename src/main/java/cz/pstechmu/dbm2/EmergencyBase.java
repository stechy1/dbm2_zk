package cz.pstechmu.dbm2;

public enum EmergencyBase {

    LOC("Lochotin"),
    BOR("Bory"),
    KOT("Koterov"),
    DOU("Doubravka"),
    KRA("Kralovice"),
    MAN("Manětín"),
    STO("Stod"),
    PRE("DZS Prestice"),
    VLC("Vlcice"),
    NEP("Nepomuk"),
    LID("LZS Line"),
    RO("Rokycany"),
    RAD("Radnice"),
    DO("Domazlice"),
    BEL("Bela nad Radbuzou"),
    BUT("Bor"),
    TC("Tachov"),
    PLA("Plana u M. Lazni"),
    STR("Stribro"),
    KON("Konstantinovy lazne"),
    KT("Klatovy"),
    NY("Nyrsko"),
    ZEL("Zelezna Ruda"),
    SU("Susice"),
    MOD("Modrava"),
    HD("Horazdovice");

    public final String cityName;

    EmergencyBase(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }
}
