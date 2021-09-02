package com.example.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


class Book(
        id: ObjectId?,
        title: String,
        author: String,
        price: Float
) {
    @BsonId
    var id: ObjectId?
    var title: String
    var author: String
    var price: Float

    constructor() : this(null, "not_set", "not_set", 0.00f)
    init {
        this.id = id
        this.title = title
        this.author = author
        this.price = price
    }
}

data class ShoppingCart(
        var id: String,
        var userId: String,
        val items: ArrayList<ShoppingtItem>
)

data class ShoppingtItem(
        var bookid: String,
        var qty: Int
)

data class User(
        var id: String,
        var name: String,
        var userName: String,
        var password: String
)