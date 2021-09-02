package com.example.ui

import com.example.ui.login.Session
import io.ktor.html.*
import kotlinx.css.menu
import kotlinx.html.*

class GeneralViewTemplate(val session: Session?) : Template<HTML> {
    val content = Placeholder<HtmlBlockTag>()

        override fun HTML.apply() {
        head {
            link (
                href = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css",
                rel = "stylesheet",
                type = "text/css"
            ) {
                this.integrity ="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x"
                this.attributes.put("crossorigin", "anonymous")
            }
        }

        body {
            insert(NavigationTemplate()) {
                menuItems {
                    a(classes = "nav-link", href = EndPoints.HOME.url) {
                        + "Home"
                    }
                }
                if (session == null) {
                    menuItems {
                        a(classes = "nav-link", href = EndPoints.LOGIN.url) {
                            + "Login"
                        }
                    }
                } else {
                    menuItems {
                        a(classes = "navv-link", href = EndPoints.LOGOUT.url) {
                            +"Logout"
                        }
                    }
                    menuItems {
                        a(classes = "nav-link", href = EndPoints.BOOKS.url) {
                            + "Books"
                        }
                    }
                    menuItems {
                        a(classes = "nav-link", href = EndPoints.CART.url) {
                            + "Cart"
                        }
                    }
                }
            }

            div(classes = "container") {
                div(classes = "row") {
                    div(classes = "col-md-6 offset-md-3") {
                        insert(content)
                    }
                }
            }
            script(
                type = "javascript",
                src = "https://code.jquery.com/jquery-3.5.1.slim.min.js"
            ) {
                integrity = "sha256-u7e5khyithlIdTpu22PHhENmPcRdFiHRjhAuHcs05RI="
                this.attributes.put("crossorigin", "anonymous")
            }
            script(
                type = "javascript",
                src = "https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
            ) {
                integrity = "sha384-DfXdz2hhtPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
                this.attributes.put("crossorigin", "anonymous")
            }
            script(
                type = "javascript",
                src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            ) {
                integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
                this.attributes.put("crossorigin", "anonymous")
            }
        }
    }
}