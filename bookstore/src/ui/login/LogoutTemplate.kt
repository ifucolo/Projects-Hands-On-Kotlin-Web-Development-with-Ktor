package com.example.ui.login

import com.example.ui.EndPoints
import com.example.ui.GeneralViewTemplate
import io.ktor.html.*
import io.ktor.routing.*
import kotlinx.html.*

class LogoutTemplate(val session: Session?, val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)): Template<HTML> {
    val greeting = Placeholder<FlowContent>()

    override fun HTML.apply() {
        insert(basicTemplate) {
            content {
                div(classes = "mt-2") {
                    h2() {
                        +"You have been logged out"
                    }
                }
            }
        }
    }
}