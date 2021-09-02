package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.util.pipeline.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
    }

    val mike = PipelinePhase("Mike")
    insertPhaseBefore(ApplicationCallPipeline.Call, mike)
    intercept(ApplicationCallPipeline.Setup) {
        log.info("Setup")
    }
    intercept(ApplicationCallPipeline.Call) {
        log.info("Call")
    }
    intercept(ApplicationCallPipeline.Features) {
        log.info("Features")
    }
    intercept(ApplicationCallPipeline.Monitoring) {
        log.info("Monitoring")
    }
    intercept(ApplicationCallPipeline.Fallback) {
        log.info("Fallback")
    }

    intercept(mike) {
        log.info("phase ${call.request.uri}")
        if (call.request.uri.contains("mike")) {
            log.info("contains mike")
            call.respondText("The endpoint contains Mike")
            finish()
        }
    }


    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("something/mike/another") {
            call.respondText("was handled")
        }
    }
}

