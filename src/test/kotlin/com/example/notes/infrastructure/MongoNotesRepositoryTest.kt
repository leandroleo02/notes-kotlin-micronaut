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
        val iterable: FindIterable<NoteDocument> = mock()
        val cursor: MongoCursor<NoteDocument> = mock()

        whenever(iterable.iterator()).thenReturn(cursor)
        whenever(cursor.hasNext())
            .thenReturn(true, true, false)

        whenever(cursor.next())
            .thenReturn(
                NoteDocument("6175f4b9d75e0c0d5cda0f31", "Learning", "green", "First Note in Kotlin"),
                NoteDocument("6175f4b9d75e0c0d5cda0f32", "Goku", "orange", "Olá, eu sou o Goku")
            )

        whenever(mongoCollection.find()).thenReturn(iterable)

        val notes = mongoNotesRepository.retrieveAll()

        assertThat(notes)
            .isNotNull
    }

    @Test
    fun findNoteById() {
        val iterable: FindIterable<NoteDocument> = mock()
        val cursor: MongoCursor<NoteDocument> = mock()

        whenever(iterable.iterator()).thenReturn(cursor)
        whenever(cursor.hasNext())
            .thenReturn(true, false)

        whenever(cursor.next())
            .thenReturn(
                NoteDocument("6175f4b9d75e0c0d5cda0f31", "Learning", "green", "First Note in Kotlin")
            )

        whenever(mongoCollection.find(any(Bson::class.java))).thenReturn(iterable)

        val note = mongoNotesRepository.findById("6175f4b9d75e0c0d5cda0f31")

        assertThat(note).isNotNull
    }

//    private fun noteFixture(): List<NoteDocument> {
//        return listOf(
//            NoteDocument("1", "Learning", "green", "First Note in Kotlin"),
//            NoteDocument("2", "Goku", "orange", "Olá, eu sou o Goku"),
//            NoteDocument("3", "Tyrion", "gold", "I drink and I know things"),
//            NoteDocument("4", "Funny", "gold", "His legs flail about as if independent from his body!"),
//            NoteDocument("5", "Cold", "white", "Winter is coming!"),
//        )
//    }
}

//class MockCursorBuilder(private val mongoCollection: MongoCollection<NoteDocument>) {
//
//    private val iterable: FindIterable<NoteDocument> = mock()
//    private val cursor: MongoCursor<NoteDocument> = mock()
//
//    init {
//        whenever(iterable.iterator()).thenReturn(cursor)
//    }
//
//    fun withFind() {
//        whenever(mongoCollection.find()).thenReturn(iterable)
//    }
//
//    fun returningDocuments(vararg documents: NoteDocument) {
//        whenever(cursor.hasNext())
//            .thenReturn(true, *mapToHasNext(*documents))
//
//        whenever(cursor.next())
//            .thenReturn(
//                documents[0],
//                *documents.drop(1).toTypedArray()
//            )
//
//    }
//
//    private fun mapToHasNext(vararg documents: NoteDocument): Array<Boolean> {
//        val hasNextMap = documents
//            .drop(1)
//            .map { true }
//            .toList()
//
//        hasNextMap.plus(false)
//        return hasNextMap.toTypedArray()
//    }
//}