package ca.cosc3p91.a4.util;

public class Time {

    private long timeSeconds;

    public Time() {
        this.timeSeconds = 0;
    }

    protected Time(long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public Time offsetSeconds(long seconds) {
        this.timeSeconds += seconds;
        return this;
    }

    public Time offsetMinutes(long minutes) {
        return offsetSeconds(minutes * 60);
    }

    public Time offsetHours(long hours) {
        return offsetMinutes(hours * 60);
    }

    public Time offsetDays(long days) {
        return offsetHours(days * 24);
    }

    public Time offsetTime(Time time) {
        return offsetSeconds(time.timeSeconds);
    }

    public long get() {
        return timeSeconds;
    }

    public boolean occurred() {
        return getTime().timeSeconds >= timeSeconds;
    }

    public static Time getTime() {
        return new Time(System.currentTimeMillis() / 1000);
    }

}
