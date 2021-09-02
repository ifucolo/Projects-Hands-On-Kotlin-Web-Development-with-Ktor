package com.example.ui.cart

import com.example.model.Cart
import com.example.ui.EndPoints
import com.example.ui.GeneralViewTemplate
import com.example.ui.login.Session
import kotlinx.html.HTML
import io.ktor.html.*
import kotlinx.html.*

class CartTemplate(val session: Session?, val cart: Cart): Template<HTML> {
    val basicTemplate = GeneralViewTemplate(session)

    override fun HTML.apply() {
        insert(basicTemplate) {
            content {
                div(classes = "mt-2 row") {
                    h2() {
                        +"Your shopping cart"
                    }

                    table(classes = "table table-striped") {
                        thead {
                            tr {
                                th(scope = ThScope.col) { +"Title" }
                                th(scope = ThScope.col) { +"Author" }
                                th(scope = ThScope.col) { +"Price" }
                                th(scope = ThScope.col) { +"Quantity" }
                                th(scope = ThScope.col) { +"Sum" }
                                th(scope = ThScope.col) { +"" }
                            }
                        }
                        tbody {
                            cart.entries.forEach {
                                tr {
                                    td { +"${it.book.title}" }
                                    td { +"${it.book.author}" }
                                    td { +"${it.book.price}" }
                                    td { +"${it.qty}" }
                                    td { +"${it.sum}" }
                                    td {
                                        form(
                                            method = FormMethod.post,
                                            encType = FormEncType.multipartFormData,
                                            action = EndPoints.DOREMOVEFROMCART.url
                                        ) {
                                            input(type = InputType.hidden, name = "boookid") {
                                                this.value = it.book.id.toString()
                                                this.attributes.put("aria-label", "bookid")
                                                this.attributes.put("aria-describedby", "basic-addon1")
                                            }
                                            button(classes = "btn btn-success", type = ButtonType.submit) {
                                                +"Remove 1 from cart"
                                            }
                                        }
                                    }
                                }
                            }
                            tr {

                            }
                            tr {
                                td {
                                    + "Sum"
                                }
                                td {

                                }
                                td {

                                }
                                td {
                                    + cart.qtyTotal.toString()
                                }
                                td {
                                    +cart.sum.toString()
                                }
                            }
                        }
                    }
                }
                div(classes = "row mt-3") {
                    form(
                        method = FormMethod.post,
                        encType = FormEncType.multipartFormData,
                        action = EndPoints.CHECKOUT.url
                    ) {
                        button(classes = "btn btn-success", type = ButtonType.submit) {
                            +"Checkout and pay"
                        }
                    }
                }
            }
        }
    }
}