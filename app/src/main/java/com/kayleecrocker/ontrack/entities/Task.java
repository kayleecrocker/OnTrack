package com.kayleecrocker.ontrack.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public String notes;

    public int priority;

    // 0 - 100
    public int progress;

    // timestamp (nullable = no due date)
    public Long dueDate;

    // repeat type: 0 = none, 1 = daily, 2 = weekly, etc.
    public int repeatType;

    // optional project link (nullable = not in project)
    public Integer projectId;

    public boolean isCompleted;

    // last time progress was updated (optional but useful later)
    public Long lastUpdated;

    public int position;
}
