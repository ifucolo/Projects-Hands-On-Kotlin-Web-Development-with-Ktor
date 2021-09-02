package com.example

import com.mongodb.MongoClient
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.gt
import io.ktor.util.*
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId

class MongoDataHandler {
    val database: MongoDatabase
    val spacesShipCollection: MongoCollection<SpaceShip>

    init {
        val pojoCodecRegistry: CodecRegistry =  fromProviders(PojoCodecProvider.builder().automatic(true).build())
        val codecRegistry: CodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)

        val clientSettings = MongoClientSettings.builder()
            .codecRegistry(codecRegistry)
            .build()

        val mongoClient = MongoClients.create(clientSettings)

        database = mongoClient.getDatabase("development")
        spacesShipCollection = database.getCollection(SpaceShip::class.java.name, SpaceShip::class.java)
        initSpaceShips()
    }

    private fun initSpaceShips() {
        deleteAllShips()
        spacesShipCollection.insertOne(SpaceShip(null, "1", 65.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "2", 45.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "3", 35.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "4", 25.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "5", 15.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "6", 15.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "7", 15.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "8", 15.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "9", 15.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "10", 15.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "11", 15.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "12", 15.3f))
        spacesShipCollection.insertOne(SpaceShip(null, "13", 15.3f))

    }

    fun shipsSortedByFuelAndPaged(pageNumber: Int, pageSize: Int): List<SpaceShip> {
        return spacesShipCollection
            .find()
            .sort(Document(mapOf(Pair("fuel", -1), Pair("_id", 1))))
            .skip(pageNumber - 1)
            .limit(pageSize)
            .toList()
    }

    fun shipsWithMoreFuelThan(fuelLimit: Float): List<SpaceShip> {
        return spacesShipCollection.find(gt("fuel", fuelLimit)).toList()
    }
    fun fuelUpSpaceShip(hexId: String) {
        spacesShipCollection.updateOne(eq("_id", ObjectId(hexId)), Document("\$set", Document("fuel", 99.9f)))
    }

    fun replaceSpaceShip(ship: SpaceShip) {
        spacesShipCollection.replaceOne(eq("_id", ship.id), ship)
    }

    fun deleteAllShips() {
        spacesShipCollection.deleteMany(Document())
    }

    fun finOneSpaceShip(hexId: String): SpaceShip? {
        return spacesShipCollection.find(eq("_id", ObjectId(hexId))).first()
    }

    fun allSpacesShips(): List<SpaceShip> {
        val mongoResult = spacesShipCollection.find()
        mongoResult.forEach {
            println("ship: ${it.name}")
        }

        return mongoResult.toList()
    }
}