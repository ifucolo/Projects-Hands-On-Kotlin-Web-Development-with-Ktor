package com.example.ui.checkout

import com.example.model.DataManagerMongoDb
import com.example.ui.EndPoints
import com.example.ui.login.Session
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.receipt() {
    get(EndPoints.CHECKOUT.url) {
        val session =  call.sessions.get<Session>()
        call.respondHtmlTemplate(ReceipTemplate(session, DataManagerMongoDb.INSTANCE.cartForUser(session))) {

        }
    }
}