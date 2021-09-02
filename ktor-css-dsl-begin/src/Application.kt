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

        get("/html") {
            call.respondHtml {
                head {
                    link(rel= "stylesheet", href = "/customstyles.css", type = "text/css")
                }
                body {
                    h1 { +"CSS-DSL Example" }
                    div("divBlue") {
                        + "Hi this is a Blue box"
                    }
                    div("divGreen") {
                        + "Hi this is a Green box"
                    }
                    div("divBrown") {
                        + "Hi this is a Brown box"
                    }
                }
            }
        }

        get("/customstyles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.gray
                }
                rule(".divBlue") {
                    color = Color.white
                    backgroundColor = Color.blue
                    minHeight = 200.px
                    fontSize = 1.5.em
                }
                rule(".divGreen") {
                    color = Color.white
                    backgroundColor = Color.green
                    minHeight = 150.px
                    fontSize = 2.em
                }
                rule(".divBrown") {
                    color = Color.white
                    backgroundColor = Color("#BF6600")
                    minHeight = 100.px
                    fontSize = 2.5.em
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
