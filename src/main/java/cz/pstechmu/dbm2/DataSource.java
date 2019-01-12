package cz.pstechmu.dbm2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class DataSource {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d. M. yyyy H:m:s");

    private final int eventId;
    private final LocalDateTime[] times = new LocalDateTime[10];
    private final EmergencyBase emergencyBase;
    private final ActionCharacter actionCharacter;
    private boolean valid;

    private static void parseDate(LocalDateTime[] array, String[] raw, int sourceIndex, int timeIndex) {
        if (raw[sourceIndex].isEmpty()) {
            array[timeIndex] = LocalDateTime.of(0, 1, 1, 0, 0);
            return;
        }

        try {
            array[timeIndex] = LocalDateTime.parse(raw[sourceIndex], FORMATTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DataSource(String data) {
        String[] raw = data.split(";");
        eventId = Integer.parseInt(raw[11]);
        Arrays.stream(EventType.values()).forEach(eventType -> parseDate(times, raw, eventType.sourceIndex, eventType.ordinal()));
        emergencyBase = EmergencyBase.valueOf(raw[12]);
        actionCharacter = ActionCharacter.valueOf(raw[10]);

        valid = eventId != -1;
        for (LocalDateTime time : times) {
            valid &= time != null;
            if (!valid) {
                break;
            }
        }
    }

    public LocalDateTime getTime(EventType eventType) {
        return times[eventType.ordinal()];
    }

    public EmergencyBase getEmergencyBase() {
        return emergencyBase;
    }

    public ActionCharacter getActionCharacter() {
        return actionCharacter;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        return String.format("Event: %d; emergency base: %s", eventId, emergencyBase.cityName);
    }
}
