package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }

        get("/library/book/{bookId}/reserve") {
            val bookId = call.parameters.get("bookid")
            val bookReserveResponse = BookReserveResponse(
                    message = "You reserved out the book $bookId",
                    listOf()
            )
            call.respond(bookReserveResponse)
        }

        get("/library/book/{bookId}/checkout") {
            val bookId = call.parameters.get("bookid")
            call.respond("You checked out the book $bookId")
        }

        get("/library/book/{bookid}") {
            val bookId = call.parameters.get("bookid")
            val book = Book(bookId!!, ",y book", "mr. iago")
            val hypermedialinks = listOf<HypermediaLink>(
                    HypermediaLink(
                            "http://localhost:8080/library/book/$bookId/checkout",
                            "checkout",
                            "GET"
                    ),
                    HypermediaLink(
                            "http://localhost:8080/library/book/$bookId/checkout",
                            "reserve",
                            "GET"
                    )
            )

            val bookResponse = BookResponse(
                    book,
                    hypermedialinks
            )

            call.respond(bookResponse)
        }
    }
}

data class Book(
        val id: String,
        val title: String,
        val author: String
)

data class BookResponse(
        val book: Book,
        val link: List<HypermediaLink>
)

data class BookReserveResponse(
        val message: String,
        val link: List<HypermediaLink>
)

data class HypermediaLink(
        val href: String,
        val rel: String,
        val type: String
)

