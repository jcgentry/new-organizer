package com.jacagen.organizer.component

import com.jacagen.organizer.Task
import io.kvision.panel.HPanel

class TaskComponent(val task: Task): HPanel() {
    init {
        resizableText(task.label, {})
    }
}