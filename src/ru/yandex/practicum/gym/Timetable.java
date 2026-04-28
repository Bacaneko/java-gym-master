package ru.yandex.practicum.gym;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {

    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (timetable.containsKey(trainingSession.getDayOfWeek())) {
            if (timetable.get(trainingSession.getDayOfWeek()).containsKey(trainingSession.getTimeOfDay())) {
                TreeMap<TimeOfDay, List<TrainingSession>> session = timetable.get(trainingSession.getTimeOfDay());
                List<TrainingSession> tempList = session.get(trainingSession.getTimeOfDay());
                tempList.add(trainingSession);
                session.put(trainingSession.getTimeOfDay(), tempList);
            } else {
                TreeMap<TimeOfDay, List<TrainingSession>> currentTrainingSession = timetable.get(trainingSession.getDayOfWeek());
                List<TrainingSession> newList = new ArrayList<>();
                newList.add(trainingSession);
                currentTrainingSession.put(trainingSession.getTimeOfDay(), newList);
                timetable.put(trainingSession.getDayOfWeek(), currentTrainingSession);
            }
        } else {
            List<TrainingSession> listTrainingSession = new ArrayList<>();
            listTrainingSession.add(trainingSession);
            TreeMap<TimeOfDay, List<TrainingSession>> currentTrainingSession = new TreeMap<>();
            currentTrainingSession.put(trainingSession.getTimeOfDay(), listTrainingSession);
            timetable.put(trainingSession.getDayOfWeek(), currentTrainingSession);
        }
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
