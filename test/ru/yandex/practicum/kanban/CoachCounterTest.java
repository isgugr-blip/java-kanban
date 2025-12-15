package ru.yandex.practicum.kanban;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.constants.Age;
import ru.yandex.practicum.kanban.constants.DayOfWeek;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CoachCounterTest {
    private Timetable timetable;
    private Coach coach1;
    private Coach coach2;
    private Coach coach3;
    private Group group;

    @BeforeEach
    void setUp() {
        timetable = new Timetable();
        coach1 = new Coach("Иванов", "Иван", "Иванович");
        coach2 = new Coach("Петров", "Петр", "Петрович");
        coach3 = new Coach("Смирнов", "Сергей", "Сергеевич");
        group = new Group("Акробатика для детей", Age.CHILD, 60);
    }

    @Test
    void testEmptyTimetable() {
        Map<Coach, Integer> coachCounts = timetable.getCountByCoaches();

        assertTrue(coachCounts.isEmpty(), "Расписание пустое, количество тренировок должно быть 0");
    }

    @Test
    void testSingleCoachMultipleSessions() {
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));

        Map<Coach, Integer> coachCounts = timetable.getCountByCoaches();

        assertEquals(1, coachCounts.size(), "Должен быть только один тренер");
        assertEquals(3, coachCounts.get(coach1), "Тренер должен иметь 3 тренировки");
    }

    @Test
    void testMultipleCoachesWithDifferentSessionCounts() {
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY, new TimeOfDay(14, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));

        Map<Coach, Integer> coachCounts = timetable.getCountByCoaches();

        assertEquals(3, coachCounts.size(), "Должно быть 3 тренера");

        Object[] coaches = coachCounts.keySet().toArray();
        assertEquals(coach2, coaches[0], "Первым должен быть тренер с наибольшим количеством тренировок");
        assertEquals(3, coachCounts.get(coach2), "Второй тренер должен иметь 3 тренировки");

        assertEquals(coach1, coaches[1], "Вторым должен быть тренер со вторым количеством тренировок");
        assertEquals(2, coachCounts.get(coach1), "Первый тренер должен иметь 2 тренировки");

        assertEquals(coach3, coaches[2], "Третьим должен быть тренер с наименьшим количеством тренировок");
        assertEquals(1, coachCounts.get(coach3), "Третий тренер должен иметь 1 тренировку");
    }
}
