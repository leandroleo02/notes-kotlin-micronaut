package com.example.notes.infrastructure

import com.example.notes.domain.Note
import com.example.notes.domain.Notes
import com.example.notes.domain.NotesRepository
import com.example.notes.infrastructure.documents.NoteDocument
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.bson.types.ObjectId

@Singleton
@Requires(notEnv = ["dev"])
class MongoNotesRepository(
    private val mongoClient: MongoClient,
    @Value("\${app.db}")
    private val database: String
) : NotesRepository {

    override fun retrieveAll(): Notes {
        return getCollection().find()
            .toList()
            .map(::toNote)
            .let(::Notes)
    }

    override fun findById(id: String): Note? {
        return getCollection().find(Filters.eq("_id", ObjectId(id)))
            .toList()
            .map(::toNote)
            .firstOrNull()
    }

    private fun toNote(noteDocument: NoteDocument) =
        Note(
            noteDocument.id,
            noteDocument.title,
            noteDocument.category,
            noteDocument.text
        )

    private fun getCollection() =
        mongoClient
            .getDatabase(database)
            .getCollection("notes", NoteDocument::class.java)
}
