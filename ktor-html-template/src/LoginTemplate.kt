package com.example

import io.ktor.html.*
import kotlinx.css.div
import kotlinx.css.h2
import kotlinx.css.menu
import kotlinx.html.*

class LoginTemplate(val basicTemplate: MyBasicLAFTemplate = MyBasicLAFTemplate()): Template<HTML> {
    val greeting = Placeholder<FlowContent>()

    override fun HTML.apply() {
        insert(basicTemplate) {
            menu {
                menuItems { + "Homee" }
                menuItems { + "Privacy Policy" }
                menuItems { + "Continue without login" }
            }
            content {
                div(classes = "mt-2") {
                    h2() {
                        insert(greeting)
                    }
                }

                div(classes = "mb-3") {
                    input(type = InputType.text, classes = "form-control") {
                        this.placeholder = "Type in your username here..."
                        this.attributes.put("arial-label", "Username")
                        this.attributes.put("arial-describedby", "basic-addon1")
                    }
                }
                div(classes = "mb-3") {
                    input(type = InputType.password, classes = "form-control") {
                        this.placeholder = "Type in your password here..."
                        this.attributes.put("arial-label", "Password")
                        this.attributes.put("arial-describedby", "basic-addon1")
                    }
                }
                div(classes = "mb-3") {
                    button(classes = "btn btn-primary", type = ButtonType.button) {
                        +"Try to login"
                    }
                }
            }
        }
    }
}