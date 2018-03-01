package hu.meruem.vaadin.drawio

import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.Button
import com.vaadin.ui.Panel
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import java.nio.file.Paths

@SpringUI
class TestUI : UI() {
    override fun init(p0: VaadinRequest?) {
        val l = VerticalLayout()
        val h = Panel()
        h.setWidth("1000px")
        h.setHeight("500px")

        l.addComponent(Button("c:/tmp/drawiotest.dio", { e ->
            DrawIO(Paths.get("c:/tmp/drawiotest.dio")).open(h)
        }))
        l.addComponent(Button("c:/tmp/drawiotest2.dio", { e ->
            DrawIO(Paths.get("c:/tmp/drawiotest2.dio")).open(h)
        }))

        l.addComponent(h)

        content = l
    }

}