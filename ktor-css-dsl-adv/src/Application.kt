package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import kotlinx.css.properties.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
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

        get("/html") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/customstyles.css?theme=light", type = "text/css")
                }
                body {
                    h1 { + "CSS-DSL" }
                    div(".colorchange") {
                        + "H1 this is a color changing box"
                    }
                    div(".divblue") {
                        + "H1 this is a blue box"
                    }
                    div(".divgreen") {
                        + "H1 this is a green box"
                    }
                    div(".divbrown") {
                        + "H1 this is a color brown box"
                    }
                }
            }
        }
        get("/customstyles.css") {
            val theme = call.parameters.get("theme")
            log.info("theme: $theme")
            val isDark = theme == "dark"

            call.respondCss {
                body {
                    color = Color.white
                    backgroundColor = if(isDark)
                        Color.black
                    else
                        Color.gray
                }

                rule("@media only screen and (max-width: 800px)") {
                    body {
                        backgroundColor = Color.white
                    }
                }

                media("only screen and (max-width: 600px)") {
                    body {
                        backgroundColor = Color.red
                    }
                }

                rule(".colorchange") {
                    animation(
                        name = "swapcolor",
                        duration = 5.s,
                        iterationCount = IterationCount.infinite,
                        timing = Timing.easeOut,
                        direction = AnimationDirection.alternate
                    )
                    height = 350.px
                }

                rule("@keyframes swapcolor") {
                    rule("0%") {
                        backgroundColor = Color("#0072f5")
                        transform {
                            scale(0.3)
                        }
                    }
                    rule("100%") {
                        backgroundColor = Color("#f58b00")
                        transform {
                            scale(1.0)
                        }
                    }
                    rule(".divblue") {
                        color = Color.white
                        backgroundColor = Color.blue
                        minHeight = 200.px
                        fontSize = 1.5.em
                    }
                    rule(".divgreen") {
                        color = Color.white
                        backgroundColor = Color.green
                        minHeight = 150.px
                        fontSize = 2.em
                    }
                    rule(".divbrown") {
                        color = Color.white
                        backgroundColor = Color("#BF6600")
                        minHeight = 100.px
                        fontSize = 2.5.em
                    }
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
