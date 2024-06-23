package com.jacagen.organizer.component

import com.jacagen.organizer.Task
import io.kvision.core.onChange
import io.kvision.form.text.Text
import io.kvision.panel.HPanel

class TaskComponent(
    task: Task,
): HPanel() {
    val labelContainer: Text = resizableText(
        task.title,
    )

    // TODO How to handle undo?

    fun onTitleUpdateLaunch(f: (String) -> Unit) {
        console.log("Setting up on change for title")
        labelContainer.onChange/*Launch*/{ f(labelContainer.value!!) }
    }

    override fun focus() {
        labelContainer.focus()
        super.focus()
    }
}