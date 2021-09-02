package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
val mongoDataHandler =  MongoDataHandler()

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/all") {
            call.respond(mongoDataHandler.allSpacesShips())
        }

        post("/fuelup/{spaceshipid}") {
            val shipId = call.parameters.get("spaceshipid")!!
            log.info(shipId)
            mongoDataHandler.fuelUpSpaceShip(shipId)
            call.respond(mongoDataHandler.allSpacesShips())
        }

        post("/replace") {
            log.info("ship replace started")
            val ship = call.receive(SpaceShip::class)
            log.info("ship $ship")
            mongoDataHandler.replaceSpaceShip(ship)
            call.respond(mongoDataHandler.finOneSpaceShip(ship.id!!.toHexString())!!)
        }

        get("/oneship/{spaceshipid}") {
            val shipId = call.parameters.get("spaceshipid")!!
            log.info("shipId $shipId")
            val ship = mongoDataHandler.finOneSpaceShip(shipId)
            call.respond(ship!!)
        }

        get("/ships") {
            val fuelmin = call.parameters.get("fuelmin")!!
            log.info("fuelmin: $fuelmin")
            val ships = mongoDataHandler.shipsWithMoreFuelThan(fuelmin.toFloat())
            call.respond(ships!!)
        }

        get("/sortedships") {
            val pageNumber = call.parameters.get("page")!!
            val pageSize = call.parameters.get("pagesize")!!

            log.info("pageNumber: $pageNumber pageSize: $pageSize")
            val ships = mongoDataHandler.shipsSortedByFuelAndPaged(
                pageNumber = pageNumber.toInt(),
                pageSize = pageSize.toInt()
            )
            call.respond(ships!!)
        }
    }
}

