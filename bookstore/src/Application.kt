package com.example

import com.example.ui.books.books2
import com.example.ui.cart.cart
import com.example.ui.checkout.receipt
import com.example.ui.login.Session
import com.example.ui.login.loginView
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.sessions.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.auth.*
import io.ktor.gson.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.locations.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Sessions) {
        cookie<Session>(Constants.COOKIE_NAME.value)
    }
    install(StatusPages) {
        exception<Throwable> { case ->
            call.respond(HttpStatusCode.InternalServerError)
            throw case
        }
    }

    install(Locations) {

    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    val users = listOf<String>("shop1", "shop2", "shop3")
    val admins = listOf<String>("admin")

    install(Authentication) {
        basic("bookStoreAuth") {
            realm = "Book Store"
            validate {
                if ((users.contains(it.name) || admins.contains(it.name)) && it.password == "123") {
                    UserIdPrincipal(it.name)
                } else {
                    null
                }
            }
        }
    }

    install(PartialContent) {
        // Maximum number of ranges that will be accepted from a HTTP request.
        // If the HTTP request specifies more ranges, they will all be merged into a single range.
        maxRangeCount = 10
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
    routing {
            loginView()
            books2()
            cart()
            receipt()

            get("/") {
                call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
            }

            authenticate("bookStoreAuth") {
                get("/api/tryauth") {
                    val principal = call.principal<UserIdPrincipal>()
                    call.respondText("Hello ${principal?.name}")
                }
            }
    }

}

data class MySession(val count: Int = 0)

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

data class JsonSampleClass(val hello: String)

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
