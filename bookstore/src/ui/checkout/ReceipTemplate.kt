package com.example.ui.checkout

import com.example.model.Cart
import com.example.ui.EndPoints
import com.example.ui.GeneralViewTemplate
import com.example.ui.login.Session
import io.ktor.html.*
import kotlinx.html.*

class ReceipTemplate(val session: Session?, val cart: Cart): Template<HTML> {
    val basicTemplate = GeneralViewTemplate(session)
    override fun HTML.apply() {
        insert(basicTemplate) {
            content {
                div(classes = "mt2 row") {
                    div(classes = "card") {
                        this.style {
                            +"width 19rem"
                        }
                        div(classes = "card-body") {
                            h5(classes = "card-title") {
                                + "Thank you for shopping a my Bookstore"
                            }
                            p(classes = "card-text") {
                                + """You just purchased ${cart.qtyTotal} books for a total of ${cart.sum} gold coind. The titles you purchased are listed here:"""
                                ul {
                                   cart.entries.forEach {
                                       li {
                                           + "${it.book.title} written by ${it.book.author}"
                                       }
                                   }
                                }
                            }
                            p(classes = "card-text") {
                                + "When come back"
                            }
                            a(href = EndPoints.BOOKS.url, classes = "btn btn-primary") {
                                + "Shop some more"
                            }
                        }
                    }
                }
            }
        }
    }

}