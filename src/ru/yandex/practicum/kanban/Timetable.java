package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.constants.DayOfWeek;

import java.util.*;

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

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> result = new ArrayList<>();
        for (TimeOfDay time : daySchedule.navigableKeySet()) {
            result.addAll(daySchedule.get(time));
        }
        return result;
    }


    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).getOrDefault(timeOfDay, List.of());
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> coachSessionCount = new HashMap<>();

        for (DayOfWeek day : timetable.keySet()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachSessionCount.put(coach, coachSessionCount.getOrDefault(coach, 0) + 1);
                }
            }
        }

        return coachSessionCount.entrySet().stream()
            .sorted(Map.Entry.<Coach, Integer>comparingByValue().reversed())
            .collect(LinkedHashMap::new,
                     (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                     LinkedHashMap::putAll);
    }

}
