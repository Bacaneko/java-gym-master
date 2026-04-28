package ru.yandex.practicum.gym;

public class TrainingSession implements Comparable<TrainingSession> {

    private final Group group;
    private final Coach coach;
    private final DayOfWeek dayOfWeek;
    private final TimeOfDay timeOfDay;

    public TrainingSession(Group group, Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        this.group = group;
        this.coach = coach;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public Group getGroup() {
        return group;
    }

    public Coach getCoach() {
        return coach;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    @Override
    public int compareTo(TrainingSession trainingSession) {
        int thisTime = this.timeOfDay.getHours() * 60 + this.timeOfDay.getMinutes();
        int otherTime = trainingSession.timeOfDay.getHours() * 60 + trainingSession.timeOfDay.getMinutes();

        return Integer.compare(thisTime, otherTime);
    }
}