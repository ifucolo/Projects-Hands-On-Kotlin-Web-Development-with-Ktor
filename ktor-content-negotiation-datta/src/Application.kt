package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.http.content.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.InputStream
import java.io.OutputStream

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        post("/text") {
            val text = call.receiveText()
            println("text received: $text")
            call.respondText("Thanks we received $text")
        }
        post("/form") {
            val parameters = call.receiveParameters()
            parameters.names().forEach {
                val myValue = parameters.get(it)
                println("key: $it, value $myValue")
            }
            call.respondText("Thank you for the form data")
        }
        post("/file") {
            val multIPart = call.receiveMultipart()
            var title = ""
            val uploadDir = "./upload"

            multIPart.forEachPart { part ->
                when(part) {
                    is PartData.FormItem -> {
                        if (part.name == "title") {
                            title = part.value
                        }
                    }
                    is PartData.FileItem -> {
                        val ext = File(part.originalFileName).extension
                        val file =  File(uploadDir, "upload-${System.currentTimeMillis()}-${title.hashCode()}")
                        part.streamProvider().use { input -> file.outputStream().buffered().use { output ->
                                input.copyToSuspend(output)
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied =  0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0}?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield > yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }

        return@withContext bytesCopied
    }

}

