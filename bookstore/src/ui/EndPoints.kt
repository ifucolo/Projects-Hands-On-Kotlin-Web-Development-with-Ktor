package com.example.ui

enum class EndPoints(val url: String) {
    LOGIN("/html/login"),
    LOGOUT("/html/logout"),
    DOLOGIN("/html/dologin"),
    HOME("/html/home"),
    BOOKS("/html/books"),
    CART("/html/cart"),
    DOBOOKSEARCH("/html/search"),
    DOREMOVEFROMCART("/html/remove-cart"),
    DOADDTOCART("/html/add-to-cart"),
    CHECKOUT("/html/checkout")
}