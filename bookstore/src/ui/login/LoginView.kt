package com.example.ui.login

import com.example.Constants
import com.example.SecurityHandler
import com.example.ui.EndPoints
import com.example.ui.books.books2
import com.example.ui.home.HomeTemplate
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.slf4j.LoggerFactory

data class Session(val username: String)
fun Route.loginView() {
    get(EndPoints.LOGIN.url) {
        call.respondHtmlTemplate(LoginTemplate(call.sessions.get<Session>())) {

        }
    }
    get(EndPoints.HOME.url) {
        call.respondHtmlTemplate(HomeTemplate(call.sessions.get<Session>())) {

        }
    }
    get(EndPoints.LOGOUT.url) {
        call.sessions.clear(Constants.COOKIE_NAME.value)
        call.respondHtmlTemplate(LogoutTemplate(call.sessions.get<Session>())) {

        }
    }

    post(EndPoints.DOLOGIN.url) {
        val log = LoggerFactory.getLogger("LoginView")
        val multipart = call.receiveMultipart()

        call.request.headers.forEach { s, list ->
            log.info("key $s values $list")
        }

        var username = ""
        var password = ""

        while (true) {
            val part = multipart.readPart() ?: break
            when(part) {
                is PartData.FormItem -> {
                    log.info("Form item ${part.name} = ${part.value}")
                    if (part.name == "username") {
                        username = part.value
                    }
                    if (part.name == "password") {
                        password = part.value
                    }
                }
                is PartData.FileItem -> {
                    log.info("FileItem ${part.name} -> ${part.originalFileName} of ${part.contentType}")
                }
            }
            part.dispose()
            if (SecurityHandler.isValid(username, password)) {
                call.sessions.set(Constants.COOKIE_NAME.value, Session(username))
                call.respondHtmlTemplate(
                    LoginSuccessfullTemplate(call.sessions.get<Session>())
                ) {
                    greeting {
                        + "You are logged in as $username and a cookie has been created"
                    }
                }
            } else {
                call.respondHtmlTemplate(
                    LoginTemplate(call.sessions.get<Session>())
                ) {
                    greeting {
                        + "Not a valid user credentials"
                    }
                }
            }
        }
    }
}