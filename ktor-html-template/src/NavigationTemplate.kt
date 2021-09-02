package com.example

import io.ktor.html.*
import kotlinx.html.*

class NavigationTemplate: Template<FlowContent> {
    val menuItems = PlaceholderList<UL, FlowContent>()

    override fun FlowContent.apply() {
        div {
            nav(classes = "navbar navbar-expand-md navbar-dark bg-dark") {
                a(classes = "navbar-brand", href = "#") { + "My Bookstore"}
                button(
                    classes = "navbar-toggler",
                    type = ButtonType.button
                ) {
                    this.attributes.put("data-toggle", "collapse")
                    this.attributes.put("data-target", "#navbarsExampleDefault")
                    this.attributes.put("aria-controls", "#navbarsExampleDefault")
                    this.attributes.put("aria-expanded", "collapse")
                    this.attributes.put("aria-label", "Toggle navigation")
                    span(classes = "navbar-toggler-icon") {}
                }
                div(classes = "collapse navbar-collapse") {
                    this.id = "navbarsExampleDefault"
                    ul(classes = "navbar-nav mr-auto") {
                        each(menuItems) {
                            li {
                               a(classes = "nav-link", href = "#") {
                                   insert(it)
                               }
                            }
                        }
                    }
                }
            }
        }
    }
}