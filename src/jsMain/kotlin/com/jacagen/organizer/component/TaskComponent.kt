package com.jacagen.organizer.component

import com.jacagen.organizer.Task
import io.kvision.core.onChangeLaunch
import io.kvision.panel.HPanel

class TaskComponent(
        private val task: Task,
): HPanel() {
    init {
        resizableText(
            task.label,
        ).onChangeLaunch { task.label = value ?: "" }
    }
}