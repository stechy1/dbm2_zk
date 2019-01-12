package cz.pstechmu.dbm2;

public enum  EventType {

    // Datum a čas události - komunikuje volající s dispatchingem
    START_TIME(1, "Datum a cas udalosti - komunikuje volajici s dispatchingem"),
    // Datum a čas výzvy - dispatcher řekne: jeďte
    DISPATCHER_SAYS_GO_TIME(2, "Datum a cas vyzvy - dispatcher rekne: jedte"),
    // Datum a čas výjezdu - kdy opravdu vyjeli
    DEPARTURE_FROM_BASE_TIME(3, "Datum a cas vyjezdu - kdy opravdu vyjeli"),
    // Datum a čas na místě - kdy přijel k pacientovi
    ARRIVAL_TO_PATIENT_TIME(4, "Datum a čas na miste - kdy prijel k pacientovi"),
    // Datum a čas transportu - jsme vyřešený na místě, jedeme do nemocnice
    TRANSPORT_TIME(5, "Datum a cas transportu - jsme vyreseny na miste, jedeme do nemocnice"),
    // Datum a čas příjezdu k záchytce
    ARRIVAL_TO_AMBULANCE(7, "Datum a cas príjezdu k zachytce"),
    // Datum a čas předání pacienta
    PATIENT_HANDOVER_TIME(6, "Datum a cas predání pacienta"),
    // Datum a čas odjezdu od záchytky
    DEPARTURE_FROM_AMBULANCE(0, "Datum a cas odjezdu od zachytky"),
    // Datum a čas příjezdu na základnu
    ARRIVAL_TO_BASE(8, "Datum a cas prijezdu na zakladnu"),
    // Datum a čas ukončení zásahu
    FINISH_TIME(9, "Datum a cas ukonceni zasahu");

    public final int sourceIndex;
    public final String description;

    EventType(int sourceIndex, String description) {
        this.sourceIndex = sourceIndex;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
