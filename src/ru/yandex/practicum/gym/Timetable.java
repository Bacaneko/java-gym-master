package ru.yandex.practicum.gym;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {

    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> sessionsByTime =
                timetable.computeIfAbsent(dayOfWeek, day -> new TreeMap<>());

        List<TrainingSession> sessions =
                sessionsByTime.computeIfAbsent(timeOfDay, time -> new ArrayList<>());

        sessions.add(trainingSession);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.containsKey(dayOfWeek)) {
            return timetable.get(dayOfWeek);
        } else {
            return new TreeMap<>();
        }
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (timetable.containsKey(dayOfWeek) && timetable.get(dayOfWeek).containsKey(timeOfDay)) {
            return timetable.get(dayOfWeek).get(timeOfDay);
        } else {
            return new ArrayList<>();
        }
    }

    public HashMap<Coach, Integer> getCountByCoaches() {
        return timetable.values().stream()
                // 1. Уходим с уровня Дней в уровень временных промежутков (TreeMap)
                .flatMap(timeMap -> timeMap.values().stream())
                // 2. Уходим с уровня TreeMap в уровень списков List<TrainingSession>
                .flatMap(List::stream)
                // 3. Группируем по тренеру и считаем количество его тренировок
                .collect(Collectors.groupingBy(
                        TrainingSession::getCoach,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ))
                // 4. Теперь у нас есть Map<Coach, Integer>, превращаем её в стрим для сортировки
                .entrySet().stream()
                // 5. Сортируем по значению (количеству) в обратном порядке
                .sorted(Map.Entry.<Coach, Integer>comparingByValue().reversed())
                // 6. Сохраняем результат в LinkedHashMap, чтобы не потерять порядок
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}
