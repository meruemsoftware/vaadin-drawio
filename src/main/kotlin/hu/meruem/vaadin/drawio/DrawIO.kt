package hu.meruem.vaadin.drawio

import com.vaadin.server.ExternalResource
import com.vaadin.ui.*
import java.nio.file.Files
import java.nio.file.Path

@com.vaadin.annotations.JavaScript(value = ["vaadin://drawio.js", "https://www.draw.io/embed.js"])
class DrawIO(val path: Path) : BrowserFrame() {

    var saved: String

    init {
        if (!Files.exists(path)) {
            Files.createFile(path)
        }
        setSizeFull()
        id = "drawio-iframe"
        saved = String(Files.readAllBytes(path));
    }

    fun open(panel: Panel) {
        setSource(ExternalResource("https://www.draw.io/?embed=1"))
        panel.content = this

        JavaScript.getCurrent().addFunction("saved", { params ->
            saved = params.getString(0)
            Files.write(path, params.getString(0).toByteArray());
        })
        JavaScript.getCurrent().addFunction("exited", { params ->
            panel.content = null
        })
        com.vaadin.ui.JavaScript.getCurrent().execute("edit('${saved}')");
    }

}