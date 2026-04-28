package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        Assertions.assertEquals(singleTrainingSession, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).get(new TimeOfDay(13, 0)).get(0));

        //Проверить, что за вторник не вернулось занятий
        Assertions.assertNotEquals(null, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));

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
        Assertions.assertEquals(mondayChildTrainingSession, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).get(new TimeOfDay(13, 0)).get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> allSessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY) // Получаем TreeMap
                .values()           // Получаем Collection<List<TrainingSession>>
                .stream()           // Создаем Stream<List<TrainingSession>>
                .flatMap(List::stream) // Разворачиваем каждый список в поток отдельных сессий
                .toList();          // Собираем в итоговый список
        List<TrainingSession> expectedList = new ArrayList<>();
        expectedList.add(thursdayChildTrainingSession);
        expectedList.add(thursdayAdultTrainingSession);
        Assertions.assertIterableEquals(expectedList, allSessions);

        // Проверить, что за вторник не вернулось занятий
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
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
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDayAndTime(singleTrainingSession.getDayOfWeek(), singleTrainingSession.getTimeOfDay()).size());


        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 00)).size());

    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Семёновна", "Лариса", "Латынина");
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession acrobaticsAdults1 = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(12, 0));
        TrainingSession acrobaticsAdults2 = new TrainingSession(groupAdult, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(12, 0));

        timetable.addNewTrainingSession(acrobaticsAdults1);
        timetable.addNewTrainingSession(acrobaticsAdults2);

        Coach coach2 = new Coach("Алоис", "Арнольд", "Шварценеггер");
        Group ironGroup = new Group("Только желез", Age.ADULT, 300);
        TrainingSession singleTrainingSession = new TrainingSession(ironGroup, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(6, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Coach coach3 = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach3,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach3,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach3,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        HashMap<Coach, Integer> numberOfTrainingSessions = timetable.getCountByCoaches();

        for (Map.Entry<Coach, Integer> coachIntegerEntry : numberOfTrainingSessions.entrySet()) {
            System.out.println(coachIntegerEntry.getKey() + " " + coachIntegerEntry.getValue());
        }
    }

}