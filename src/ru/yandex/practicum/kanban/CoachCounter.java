package ru.yandex.practicum.kanban;

public class CoachCounter {
    private Coach coach;
    private int sessionCount;

    public CoachCounter(Coach coach, Integer sessionCount) {
        this.coach = coach;
        this.sessionCount = sessionCount;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getSessionCount() {
        return sessionCount;
    }
}
