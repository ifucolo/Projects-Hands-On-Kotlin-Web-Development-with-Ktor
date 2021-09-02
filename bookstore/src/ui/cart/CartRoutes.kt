package com.example.ui.cart

import com.example.model.DataManagerMongoDb
import com.example.ui.EndPoints
import com.example.ui.books.BookTemplate
import com.example.ui.login.Session
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.i
import org.slf4j.LoggerFactory

fun Route.cart() {
    get(EndPoints.CART.url) {
        val session = call.sessions.get<Session>()
        call.respondHtmlTemplate(CartTemplate(session, DataManagerMongoDb.INSTANCE.cartForUser(session))) {

        }
    }
    post(EndPoints.DOADDTOCART.url) {
        val log = LoggerFactory.getLogger("Remove fro cart")
        val multipart = call.receiveMultipart()
        val session = call.sessions.get<Session>()
        var bookId = ""
        while (true) {
            val part = multipart.readPart()?: break
            when(part) {
                is PartData.FormItem -> {
                    if (part.name == "bookid") {
                        bookId = part.value
                    }
                }
            }
            part.dispose()
        }
        val book = DataManagerMongoDb.INSTANCE.getBookWithId(bookId)
        DataManagerMongoDb.INSTANCE.addBookToCart(session, book)
        call.respondHtmlTemplate(BookTemplate(call.sessions.get<Session>(), DataManagerMongoDb.INSTANCE.allBooks())) {
            searchFilter {
                i {
                  + "Book added to cart"
                }
            }
        }

    }

    post(EndPoints.DOREMOVEFROMCART.url) {
        val log = LoggerFactory.getLogger("Remove fro cart")
        val multipart = call.receiveMultipart()
        val session = call.sessions.get<Session>()
        var bookId = ""
        while (true) {
            val part = multipart.readPart()?: break
            when(part) {
                is PartData.FormItem -> {
                    if (part.name == "boookid") {
                        bookId = part.value
                    }
                }
            }
            part.dispose()
        }
        val book = DataManagerMongoDb.INSTANCE.getBookWithId(bookId)
        DataManagerMongoDb.INSTANCE.removeBookFromCart(session, book)
        call.respondHtmlTemplate(CartTemplate(session, DataManagerMongoDb.INSTANCE.cartForUser(session))) {

        }

    }
}