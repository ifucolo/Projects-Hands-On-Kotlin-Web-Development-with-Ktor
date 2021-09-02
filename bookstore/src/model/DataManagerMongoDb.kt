package com.example.model

import com.example.ui.login.Session
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.*
import org.bson.BsonDocument
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

enum class DataManagerMongoDb {
    INSTANCE;
    val log = LoggerFactory.getLogger(DataManagerMongoDb::class.java)
    val database: MongoDatabase
    val bookCollection: MongoCollection<Book>
    val cartCollection: MongoCollection<Cart>

    init {
        val pojoCodecRegistry: CodecRegistry =
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        val codecRegistry: CodecRegistry =
            CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)

        val clientSettings = MongoClientSettings.builder()
            .codecRegistry(codecRegistry)
            .build()

        val mongoClient = MongoClients.create(clientSettings)

        database = mongoClient.getDatabase("development")
        bookCollection = database.getCollection(Book::class.java.simpleName, Book::class.java)
        cartCollection = database.getCollection(Cart::class.java.simpleName, Cart::class.java)
        initBooks()
    }

    private fun initBooks() {
        bookCollection.deleteMany(BsonDocument())
        cartCollection.deleteMany(BsonDocument())

        bookCollection.insertOne(Book(id = null, title = "How to grow feijao", author = "Sint Maximan", 500.0F))
        bookCollection.insertOne(Book(id = null, title = "How to grow abacate", author = "Sint Maximan 2", 600.0F))
        bookCollection.insertOne(Book(id = null, title = "How to grow repolho", author = "Sint Maximan 3", 7500.0F))
        bookCollection.insertOne(Book(id = null, title = "How to grow melao", author = "Sint Maximan 4", 1500.0F))
        bookCollection.insertOne(Book(id = null, title = "How to grow arroz", author = "Sint Maximan 5", 5400.0F))
        bookCollection.insertOne(Book(id = null, title = "How to grow berinjela", author = "Sint Maximan 6", 5400.0F))
    }

    fun newBook(book: Book): Book {
        bookCollection.insertOne(book)

        return book
    }

    fun updateBook(book: Book): Book {
        val bookFound = bookCollection.find(Document("_id", book.id)).first()
        bookFound?.apply {
            title = book.title
            author = book.author
            price = book.price
        }

        return bookFound!!
    }

    fun deleteBook(id: String): Book {
        val bookFound =  bookCollection.find(Document("_id", id)).first()
        bookCollection.deleteOne(eq("_id", ObjectId(id)))

        return bookFound!!
    }

    fun allBooks(): List<Book> {
        return bookCollection.find().toList()
    }

    fun sortedBooks(sortBy: String, asc: Boolean): List<Book> {
        val pageNumber = 1
        val pageSize = 1000
        val ascint: Int = if (asc) 1 else -1

        return bookCollection
            .find()
            .sort(Document(mapOf(Pair(sortBy, ascint), Pair("_id", -1))))
            .skip(pageNumber - 1)
            .limit(pageSize)
            .toList()
    }

    fun searchBooks(str: String): List<Book> {
        return bookCollection.find().toList().filter {
            it.title.contains(str) || it.author.contains(str)
        }
    }

    fun removeBookFromCart(session: Session?,book: Book) {
        val cartForUser = cartForUser(session)
        cartForUser.removeBook(book)
        updateCart(cartForUser)
    }

    fun updateCart(cart: Cart) {
        val replaceOne = cartCollection.replaceOne(eq("username", cart.username), cart)
        log.info("Update cart: $replaceOne")
    }

    fun addBookToCart(session: Session?,book: Book) {
        val cartForUser = cartForUser(session)
        cartForUser.addBook(book)
        updateCart(cartForUser)
    }

    fun getBookWithId(bookId: String): Book {
        return bookCollection.find(eq("_id", ObjectId(bookId))).first()
    }

    fun cartForUser(session: Session?): Cart {
        if (session == null) {
            throw  IllegalArgumentException("session is null")
        }

        val find = cartCollection.find(eq("username", session.username))

        if (find.count() == 0) {
            val cart =  Cart(username = session.username)
            cartCollection.insertOne(cart)
            return cart
        } else {
            return find.first()
        }
    }

}