package com.example

import io.ktor.html.*
import kotlinx.html.*


class MyBasicLAFTemplate: Template<HTML> {
    val content = Placeholder<HtmlBlockTag>()
    val menu = TemplatePlaceholder<NavigationTemplate>()

    override fun HTML.apply() {
        head {
            link (
                href = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css",
                rel = "stylesheet",
                type = "text/css"
            ) {
                this.integrity ="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x"
                this.attributes.put("crossorigin", "anonymous")
            }
        }

        body {
            insert(NavigationTemplate(), menu)

            div(classes = "container") {
                div(classes = "row") {
                    div(classes = "col-md-6 offset-md-3") {
                        insert(content)
                    }
                }
            }
            script(
                type = "javascript",
                src = "https://code.jquery.com/jquery-3.5.1.slim.min.js"
            ) {
                integrity = "sha256-u7e5khyithlIdTpu22PHhENmPcRdFiHRjhAuHcs05RI="
                this.attributes.put("crossorigin", "anonymous")
            }
            script(
                type = "javascript",
                src = "https://cdn.jsdelivr.net//npm/popper.js@1.16.1/dist/umd/popper.min.js"
            ) {
                this.attributes.put("crossorigin", "anonymous")
            }
            script(
                type = "javascript",
                src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            ) {
                integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
                this.attributes.put("crossorigin", "anonymous")
            }
        }
    }
}