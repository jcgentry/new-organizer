package test.com.jacagen.organizer


import com.jacagen.organizer.App
import com.jacagen.organizer.component.TaskComponent
import io.kvision.test.DomSpec
import io.kvision.test.SimpleSpec
import io.kvision.test.WSpec
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.CustomEventInit
import org.w3c.dom.Node
import org.w3c.dom.asList
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.test.Test
import kotlin.test.assertTrue

class AppSpec : WSpec {

    @Test
    fun initialState() {
        run{
            val app = App()
            app.start()
            dumpNodes(document)
        }
    }

    @Test
    fun addRowAfterRowWithNoChildren() {
        run {
            val app = App()
            app.controller.outline.root.nodeContainer.addAfterInsertHook { vnode ->
                console.log("After insert hook")
            }
            app.start()
            // create a new keyboard event and set the key t"o "Enter"
            val event = KeyboardEvent("keydown", KeyboardEventInit(
                    key = "Enter",
                    code = "Enter",
            ))

            console.log("Element is ${(app.controller.outline.root.nodeContainer as TaskComponent).labelContainer.getElement()}")
            (app.controller.outline.root.nodeContainer as TaskComponent).labelContainer.dispatchEvent("keydown", CustomEventInit())
            console.log("Dispatched")
            //dumpNodes(document)
        }


    }
}

private fun dumpNodes(n: Node, level: Int = 0) {
//    println("\t".repeat(level) + n.nodeName + " " + n.nodeValue)
//    for (c in n.childNodes.asList()) {
//        dumpNodes(c, level + 1)
//    }
}
