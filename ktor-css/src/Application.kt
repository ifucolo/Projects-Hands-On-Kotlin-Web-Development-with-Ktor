package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/htmlexample") {
            call.respondHtml {
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
                    div(classes = "container") {
                        div(classes = "row") {
                            div(classes = "offset-md-4 col-md-4 order-md-2 mb-4") {
                                h1 { + "My app"}
                                ul {
                                    for(n in 1..10) {
                                        li { + "$n" }
                                    }
                                }
                                button(classes = "btn btn-warning") {
                                    + "Test 123"
                                }
                                br() {}
                                br() {}
                                div(classes = "alert alert-success") {
                                    this.role = "alert"
                                    + "My ktor and Bootstrap"
                                }
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
        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
                }
            }
        }
    }
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
