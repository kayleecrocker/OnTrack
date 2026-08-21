package com.kayleecrocker.ontrack.algorithms;

import com.kayleecrocker.ontrack.entities.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskPriorityCalculator {

    public static int calculatePriority(Task task) {

        int priority = task.importance * 10;

        // calculate
        // %percent completion, duration (minutes, hours), hours till deadline, last update


        int timeLeft = task.duration * (1 - task.progress / 100);
        //task.hoursSinceLastUpdated = Math.round((task.lastUpdated - System.currentTimeMillis()) / (1000 * 60 * 60));

        // main calculation
        if (task.deadline != null) {
            int hoursToDeadline = Math.round((task.deadline - System.currentTimeMillis()) / (1000 * 60 * 60));
            if (hoursToDeadline < 48) {
                priority += 15;
                if (hoursToDeadline < 24) {
                    priority += 20;
                }
            }
        }

        // short task boost
        if (task.durationType == 0 && timeLeft <= 60) {
            // ADD TAG: "Quick Complete"
            priority += 20;
            if (timeLeft <= 30) {
                priority += 20;
            }
        }

        // almost done boost
        if (task.progress >= 75) {
            // ADD TAG: "Quick Complete"
            priority += 15;
            if (task.progress >= 90) {
                priority += 15;
            }
        }

        // been a while boost

        return priority;
    }
}
