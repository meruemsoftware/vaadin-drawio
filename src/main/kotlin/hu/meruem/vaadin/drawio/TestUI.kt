package hu.meruem.vaadin.drawio

import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.*
import java.nio.file.Paths

@SpringUI
class TestUI : UI() {
    override fun init(p0: VaadinRequest?) {
        val l = VerticalLayout()
        val h = HorizontalLayout()
        h.setWidth("300px")
        h.setHeight("300px")

        l.addComponent(Button("drawio", { e ->
            DrawIO(Paths.get("c:/tmp/drawiotest.dio")).open(h)
        }))

        l.addComponent(h)

        content = l
    }

}