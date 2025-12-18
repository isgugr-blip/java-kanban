package ru.yandex.practicum.kanban;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.constants.Age;
import ru.yandex.practicum.kanban.constants.DayOfWeek;

import java.util.List;

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
        List<CoachCounter> coachCounts = timetable.getCountByCoaches();

        assertTrue(coachCounts.isEmpty(), "Расписание пустое, количество тренировок должно быть 0");
    }

    @Test
    void testSingleCoachMultipleSessions() {
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));

        List<CoachCounter> coachCounts = timetable.getCountByCoaches();
        assertEquals(1, coachCounts.size(), "Должен быть только один тренер");
        assertEquals(3, coachCounts.getFirst().getSessionCount(), "Тренер должен иметь 3 тренировки");
    }

    @Test
    void testMultipleCoachesWithDifferentSessionCounts() {
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY, new TimeOfDay(14, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));

        List<CoachCounter> coachCounts = timetable.getCountByCoaches();

        assertEquals(3, coachCounts.size(), "Должно быть 3 тренера");

        assertEquals(coach2, coachCounts.get(0).getCoach(), "Первым должен быть тренер с наибольшим количеством тренировок");
        assertEquals(coach1, coachCounts.get(1).getCoach(), "Вторым должен быть тренер со вторым количеством тренировок");
        assertEquals(coach3, coachCounts.get(2).getCoach(), "Третьим должен быть тренер с наименьшим количеством тренировок");

        assertEquals(2, coachCounts.get(1).getSessionCount(), "Первый тренер должен иметь 2 тренировки");
        assertEquals(3, coachCounts.get(0).getSessionCount(), "Второй тренер должен иметь 3 тренировки");
        assertEquals(1, coachCounts.get(2).getSessionCount(), "Третий тренер должен иметь 1 тренировку");
    }
}
