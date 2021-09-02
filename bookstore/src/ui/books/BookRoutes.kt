package com.example.ui.books

import com.example.model.DataManagerMongoDb
import com.example.ui.EndPoints
import com.example.ui.login.Session
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.i
import org.slf4j.LoggerFactory

fun Route.books2() {
    val dataManager = DataManagerMongoDb.INSTANCE
    get(EndPoints.BOOKS.url) {
        val searchBooks = dataManager.searchBooks("")
        call.respondHtmlTemplate(BookTemplate(call.sessions.get<Session>(), searchBooks)) {}
    }
    post(EndPoints.DOBOOKSEARCH.url) {

        val log = LoggerFactory.getLogger("LoginView")
        val multipart = call.receiveMultipart()
        var search = ""

        while (true) {
            val part = multipart.readPart()?: break
            when(part) {
                is PartData.FormItem -> {
                    log.info("FormItem ${part.name} = ${part.value}")
                    if (part.name == "search") {
                        search = part.value
                    }
                }
            }
            part.dispose()

            val searchBooks = dataManager.searchBooks(search)
            call.respondHtmlTemplate(BookTemplate(call.sessions.get<Session>(), searchBooks)) {
                searchFilter {
                    i {
                        + "Search filter: $search"
                    }
                }
            }
        }
    }
}