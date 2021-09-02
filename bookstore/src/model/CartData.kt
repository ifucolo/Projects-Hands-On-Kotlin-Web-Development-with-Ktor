package com.example.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CartEntry(var bookId: ObjectId?, var book: Book, var qty: Int, var sum: Float) {
    constructor(): this(null, Book(), 0, 0.0f)
}

class Cart(
    id: ObjectId? = null,
    username: String,
    qtyTotal: Int = 0,
    sum: Float = 0.0f,
    entries: MutableList<CartEntry> = arrayListOf()
) {
    @BsonId
    var id: ObjectId?
    var username: String
    var entries: MutableList<CartEntry>
    var qtyTotal: Int = 0
    var sum: Float = 0.0f

    constructor(): this(null, "", 0, 0.00f, arrayListOf())
    init {
        this.id = id
        this.username = username
        this.entries = entries
        this.qtyTotal = qtyTotal
        this.sum = sum
    }

    fun addBook(book: Book) {
        val find = entries.find {
            it.book.id?: null == book.id
        }

        if (find == null) {
            entries.add(CartEntry(book.id, book, 1, book.price))
        } else {
            find.qty += 1
            find.sum += book.price
        }

        this.qtyTotal += 1
        this.sum += book.price
    }

    fun removeBook(book: Book) {
        val find = entries.find {
            it.book.id?: null == book.id
        }

        if (find == null) {
            return
        } else {
            find.qty -= 1
            find.sum -= book.price
            if (find.qty <= 0) {
                entries.remove(find)
            }
        }
        this.qtyTotal -= 1
        this.sum -= book.price
    }
}