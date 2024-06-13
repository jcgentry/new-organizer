package com.jacagen.organizer.component

import com.jacagen.organizer.Task
import io.kvision.core.Container
import io.kvision.core.onChangeLaunch
import io.kvision.core.onEvent
import io.kvision.panel.HPanel

class TaskComponent(
        private val task: Task,
): HPanel() {
    init {
        val t = resizableText(
            task.label,
        )
        t.onChangeLaunch { task.label = value ?: "" }
    }
}