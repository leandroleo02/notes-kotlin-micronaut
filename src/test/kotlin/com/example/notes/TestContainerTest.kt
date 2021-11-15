package com.example.notes

import com.example.notes.domain.NotesRepository
import com.example.notes.infrastructure.documents.NoteDocument
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@MicronautTest
class TestContainerTest {

    @Inject
    private lateinit var notesRepository: NotesRepository

    @Inject lateinit var mongoClient: MongoClient

//    @BeforeAll
//    @JvmStatic
//    fun beforeAll() {}

    @BeforeEach
    fun setUp() {
        collection().insertMany(noteFixture())
    }

    @Test
    fun test() {
        println(notesRepository.retrieveAll())
    }

    @Test
    fun test2() {
        println(notesRepository.retrieveAll())
    }

    @AfterEach
    fun tearDown() {
        collection().deleteMany(Filters.empty())
    }

    private fun collection() = mongoClient
        .getDatabase("notesapi")
        .getCollection("notes", NoteDocument::class.java)

    private fun noteFixture(): List<NoteDocument> {
        return listOf(
            NoteDocument("6175f4b9d75e0c0d5cda0f31", "Learning", "green", "First Note in Kotlin"),
            NoteDocument("6175f4b9d75e0c0d5cda0f32", "Goku", "orange", "Olá, eu sou o Goku"),
            NoteDocument("6175f4b9d75e0c0d5cda0f33", "Tyrion", "gold", "I drink and I know things"),
            NoteDocument("6175f4b9d75e0c0d5cda0f34", "Funny", "gold", "His legs flail about as if independent from his body!"),
            NoteDocument("6175f4b9d75e0c0d5cda0f35", "Cold", "white", "Winter is coming!"),
        )
    }
}

@Factory
@Requires(env = ["test"])
class MongoClientFactory {

    @Singleton
    fun mongoClient(): MongoClient {
        val container: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:5.0.3-focal"))
            .apply {
                withExposedPorts(27017)
                start()
            }

        val pojoCodecRegistry: CodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(
                PojoCodecProvider
                    .builder()
                    .automatic(true)
                    .build()
            )
        )

        val settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .applyConnectionString(ConnectionString(container.replicaSetUrl))
            .build()
        return MongoClients.create(settings)
    }
}
