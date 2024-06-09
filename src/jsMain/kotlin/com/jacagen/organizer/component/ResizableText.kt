package com.jacagen.organizer.component

import io.kvision.core.Container
import io.kvision.core.onInputLaunch
import io.kvision.form.text.Text
import io.kvision.utils.ch

class ResizableText(value: String) : Text(value=value) {
    fun doResize() {   // TODO make private-er
        width = (value!!.length).ch
    }
}

fun Container.resizableText(value: String): ResizableText {
    val t = ResizableText(value).apply {
        readonly = true
    }
    t.onInputLaunch { doResize() }
    add(t)
    t.doResize()
    return t
}