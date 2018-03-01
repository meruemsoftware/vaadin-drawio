package hu.meruem.vaadin.drawio

import com.vaadin.ui.CustomComponent
import com.vaadin.ui.JavaScript
import com.vaadin.ui.Layout
import java.nio.file.Files
import java.nio.file.Path

@com.vaadin.annotations.JavaScript(value = ["vaadin://drawio.js", "https://www.draw.io/embed.js"])
class DrawIO(val path: Path) : CustomComponent() {

    var saved: String

    init {
        if (!Files.exists(path)) {
            Files.createFile(path)
        }
        saved = String(Files.readAllBytes(path));
    }

    fun open(layout: Layout) {
        JavaScript.getCurrent().addFunction("saved", { params ->
            saved = params.getString(0)
            Files.write(path, params.getString(0).toByteArray());
        })
        com.vaadin.ui.JavaScript.getCurrent().execute("edit('${saved}')");
        layout.addComponent(this)
    }

}