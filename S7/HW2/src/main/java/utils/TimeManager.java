package utils;

/**
 * A class to work with time.
 */
@SuppressWarnings("SpellCheckingInspection")
public class TimeManager {
    /**
     * Gets current {@code unixtime}.
     *
     * @return Current time in seconds from 1st Jan of 1970.
     */
    public static int getUnixTimeNow() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    /**
     * Gets time in unix format of {@code now minus hours}.
     * <p>
     * {@code hours} must not negative valued.
     *
     * @param hours Hours to subtract from now time.
     * @return Unix time of now with hours offset ago.
     */
    public static int getUnixTimeOffsetHoursAgo(int hours) {
        assert (hours >= 0);

        return getUnixTimeNow() - hours * 60 * 60;
    }
}
