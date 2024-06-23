package com.jacagen.organizer.component

import com.jacagen.organizer.Controller
import com.jacagen.organizer.Task
import io.kvision.core.onChange
import io.kvision.core.onChangeLaunch
import io.kvision.form.text.Text
import io.kvision.panel.HPanel

class TaskComponent(
    val task: Task,
): HPanel() {
    val labelContainer: Text
    init {
        labelContainer = resizableText(
            task.title,
        )
    }

    // TODO How to handle undo?

    fun onTitleUpdateLaunch(f: /*suspend*/ (String) -> Unit) {
        console.log("Setting up on change for title")
        labelContainer.onChange/*Launch*/{ f(labelContainer.value!!) }
    }

    override fun focus() {
        labelContainer.focus()
        super.focus()
    }
}