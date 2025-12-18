package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.constants.DayOfWeek;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {
    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(trainingSession.getDayOfWeek());
        List<TrainingSession> dayTimeSchedule = daySchedule.get(trainingSession.getTimeOfDay());
        if (dayTimeSchedule == null) {
            dayTimeSchedule = new ArrayList<>();
            dayTimeSchedule.add(trainingSession);
            daySchedule.put(trainingSession.getTimeOfDay(), dayTimeSchedule);
        } else {
            dayTimeSchedule.add(trainingSession);
        }
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }


    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).getOrDefault(timeOfDay, List.of());
    }

    public List<CoachCounter> getCountByCoaches() {
        HashMap<Coach, Integer> coachSessionCount = new HashMap<>();

        for (DayOfWeek day : timetable.keySet()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    coachSessionCount.merge(session.getCoach(), 1, Integer::sum);
                }
            }
        }

        return coachSessionCount.entrySet().stream()
            .sorted(Map.Entry.<Coach, Integer>comparingByValue().reversed())
            .map(entry -> new CoachCounter(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

}
