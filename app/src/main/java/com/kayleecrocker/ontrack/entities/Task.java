package com.kayleecrocker.ontrack.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "tasks")
public class Task implements Comparable<Task> {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public String notes;

    public int priority;
    public int manualPriorityAdjustment;

    public int importance;

    // 0 - 100
    public int progress;

    // timestamp (nullable = no due date)
    public Long deadline;

    public int hoursToDeadline;

    public int duration;
    // 0 = minutes, 1 = hours
    public int durationType;

    // repeat type: 0 = none, 1 = daily, 2 = weekly, etc.
    public int repeatType;

    // optional project link (nullable = not in project)
    public Integer projectId;

    public boolean isCompleted;

    // last time progress was updated (optional but useful later)
    public Long lastUpdated;

    public int hoursSinceLastUpdated;
    public long creationDate;

    public int position;

    //public ArrayList<String> tags;

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }
}
