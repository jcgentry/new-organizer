package test.com.jacagen.organizer


import com.jacagen.organizer.App
import io.kvision.test.WSpec
import kotlinx.browser.document
import org.w3c.dom.Node
import org.w3c.dom.asList
import kotlin.test.Test

class AppSpec : WSpec {

    @Test
    fun initialState() {
        run {
            val app = App()
            app.start()
            dumpNodes(document)
        }
    }

}

private fun dumpNodes(n: Node, level: Int = 0) {
    println("\t".repeat(level) + n.nodeName + " " + n.nodeValue)
    for (c in n.childNodes.asList()) {
        dumpNodes(c, level + 1)
    }
}
