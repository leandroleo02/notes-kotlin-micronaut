package com.example.notes.infrastructure

import com.example.notes.infrastructure.documents.NoteDocument
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoCursor
import com.mongodb.client.MongoDatabase
import org.assertj.core.api.Assertions.assertThat
import org.bson.conversions.Bson
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MongoNotesRepositoryTest {

    private lateinit var mongoNotesRepository: MongoNotesRepository
    private lateinit var mongoCollection: MongoCollection<NoteDocument>

    @BeforeEach
    fun setUp() {
        val mockMongoClient: MongoClient = mock()
        val mongoDatabase: MongoDatabase = mock()
        mongoCollection = mock()

        whenever(mockMongoClient.getDatabase(anyString())).thenReturn(mongoDatabase)
        whenever(mongoDatabase.getCollection(anyString(), eq(NoteDocument::class.java))).thenReturn(mongoCollection)

        mongoNotesRepository = MongoNotesRepository(mockMongoClient)
    }

    @Test
    fun readAllNotes() {
        val mockCursorBuilder = MockCursorBuilder(mongoCollection)
        mockCursorBuilder
            .noFilter()
            .returningDocuments(*noteFixture())

        val notes = mongoNotesRepository.retrieveAll()

        assertThat(notes)
            .isNotNull
    }

    @Test
    fun findNoteById() {
        val mockCursorBuilder = MockCursorBuilder(mongoCollection)
        mockCursorBuilder
            .anyFilter()
            .returningDocuments(
                NoteDocument("6175f4b9d75e0c0d5cda0f31", "Learning", "green", "First Note in Kotlin")
            )

        val note = mongoNotesRepository.findById("6175f4b9d75e0c0d5cda0f31")

        assertThat(note).isNotNull
    }

    private fun noteFixture(): Array<NoteDocument> {
        return arrayOf(
            NoteDocument("6175f4b9d75e0c0d5cda0f31", "Learning", "green", "First Note in Kotlin"),
            NoteDocument("6175f4b9d75e0c0d5cda0f32", "Goku", "orange", "Olá, eu sou o Goku"),
            NoteDocument("6175f4b9d75e0c0d5cda0f33", "Tyrion", "gold", "I drink and I know things"),
            NoteDocument("6175f4b9d75e0c0d5cda0f34", "Funny", "gold", "His legs flail about as if independent from his body!"),
            NoteDocument("6175f4b9d75e0c0d5cda0f35", "Cold", "white", "Winter is coming!"),
        )
    }
}

class MockCursorBuilder(private val mongoCollection: MongoCollection<NoteDocument>) {

    private val cursor: MongoCursor<NoteDocument> = mock()
    private val iterable: FindIterable<NoteDocument> = mock()

    init {
        whenever(iterable.iterator()).thenReturn(cursor)
    }

    fun noFilter(): MockCursorBuilder {
        whenever(mongoCollection.find()).thenReturn(iterable)
        return this
    }

    fun anyFilter(): MockCursorBuilder {
        whenever(mongoCollection.find(any(Bson::class.java))).thenReturn(iterable)
        return this
    }

    fun returningDocuments(vararg documents: NoteDocument): MockCursorBuilder {
        whenever(cursor.hasNext())
            .thenReturn(true, *mapToHasNext(*documents))

        whenever(cursor.next())
            .thenReturn(
                documents[0],
                *documents.drop(1).toTypedArray()
            )
        return this
    }

    private fun mapToHasNext(vararg documents: NoteDocument): Array<Boolean> {
        val hasNextMap = documents
            .drop(1)
            .map { true }
            .toList()

        return hasNextMap
            .plus(false)
            .toTypedArray()
    }
}
