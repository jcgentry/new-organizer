package com.jacagen.organizer.component

import com.jacagen.organizer.Task
import io.kvision.core.Container
import io.kvision.core.onChangeLaunch
import io.kvision.core.onEvent
import io.kvision.form.text.Text
import io.kvision.panel.HPanel

class TaskComponent(
        private val task: Task,
): HPanel() {
    val labelContainer: Text
    init {
        labelContainer = resizableText(
            task.label,
        )
        labelContainer.onChangeLaunch { task.label = value ?: "" }
    }

    override fun focus() {
        labelContainer.focus()
        super.focus()
    }
}