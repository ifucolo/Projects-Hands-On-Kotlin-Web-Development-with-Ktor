package com.example.ui.home

import com.example.ui.EndPoints
import com.example.ui.GeneralViewTemplate
import com.example.ui.login.Session
import io.ktor.html.*
import kotlinx.html.*

class HomeTemplate(val session: Session?, val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)): Template<HTML> {
    val greeting = Placeholder<FlowContent>()

    override fun HTML.apply() {
        insert(basicTemplate) {
            content {
                div(classes = "mt-2") {
                    h2() {
                        +"Welcome to book store"
                    }
                    p {
                        + "- We have many good deals"
                    }
                    p {
                        + "- Let us known what you want"
                    }
                }
            }
        }
    }
}