package com.example

import com.example.model.Book
import com.example.model.DataManagerMongoDb
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


@Location("/book/list")
data class BookListLocation(val sortBy: String, val asc: Boolean)

fun Route.books() {
    val dataManager = DataManagerMongoDb.INSTANCE

    authenticate("bookStoreAuth") {
        get<BookListLocation> {
            call.respond(dataManager.sortedBooks(it.sortBy, it.asc))
        }
    }

    route("/book") {
        get("/") {
          call.respond(dataManager.allBooks())
        }

        post("/{id}") {
            val id = call.parameters["id"]
            val book = call.receive(Book::class)
            val updatedBook = dataManager.updateBook(book)
            call.respond(updatedBook!!)
        }

        put("/") {
            val book = call.receive(Book::class)
            val newBook = dataManager.newBook(book)
            call.respond(newBook)
        }

        delete("/{id}") {
            val id = call.parameters["id"].toString()
            val deletedBook = dataManager.deleteBook(id)
            call.respond(deletedBook!!)
        }
    }
}