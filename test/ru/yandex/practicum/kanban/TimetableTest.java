package ru.yandex.practicum.kanban;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.constants.Age;
import ru.yandex.practicum.kanban.constants.DayOfWeek;

import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySchedule.size());
        assertEquals(1, mondaySchedule.get(new TimeOfDay(13, 0)).size());

        //Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertEquals(0, tuesdaySchedule.size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySchedule =  timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        List<TrainingSession> sessions = thursdaySchedule.get(new TimeOfDay(13, 0));
        List<TrainingSession> sessionsLate = thursdaySchedule.get(new TimeOfDay(20, 0));

        // Проверяем, что в разные временные слоты разные занятия
        assertEquals(1, sessions.size(), "Должно быть одно занятие в 13:00");
        assertEquals(1, sessionsLate.size(), "Должно быть одно занятие в 20:00");
        assertEquals(thursdayChildTrainingSession, sessions.get(0));
        assertEquals(thursdayAdultTrainingSession, sessionsLate.get(0));
        // Проверить, что за вторник не вернулось занятий
        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertEquals(0, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).size());
    }

    @Test
    void testMultipleTrainingSessionsOnSameDayAndTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession session1 = new TrainingSession(groupAdult, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));
        TrainingSession session2 = new TrainingSession(groupChild, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        // Проверить, что за среду в 19:00 вернулось два занятия
        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));
        assertEquals(2, sessions.size(), "Должны быть два занятия в одно и то же время");

        // Проверить, что занятия разные
        assertEquals(session1, sessions.get(0));
        assertEquals(session2, sessions.get(1));
    }

}
