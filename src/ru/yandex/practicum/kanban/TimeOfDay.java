package ru.yandex.practicum.kanban;

public class TimeOfDay implements Comparable<TimeOfDay> {

    //часы (от 0 до 23)
    private int hours;
    //минуты (от 0 до 59)
    private int minutes;

    public TimeOfDay(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public int compareTo(TimeOfDay o) {
        int thisMinutes = this.hours * 60 + this.minutes;
        int otherMinutes = o.hours * 60 + o.minutes;

        return Integer.compare(thisMinutes, otherMinutes);
    }
}
