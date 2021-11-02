package com.example.notes.entrypoint.rest

import com.example.notes.application.NoteService
import com.example.notes.application.config.Logging
import com.example.notes.application.config.logger
import com.example.notes.domain.Note
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("notes")
class NotesController(private val noteService: NoteService) {

    companion object : Logging {
        val logger = logger()
    }

    @Get
    fun retrieveNotes(): List<NotesResponse> {
        logger.info("Retrieving Notes")
        return noteService.retrieveNotes()
            .convert { NotesResponse(it) }
    }

    @Get("{id}")
    fun getNoteById(id: String): HttpResponse<NotesResponse> {
        logger.info("Retrieving Note $id")
        return noteService.getNoteById(id)?.let {
            HttpResponse.status<NotesResponse?>(HttpStatus.OK).body(NotesResponse(it))
        } ?: HttpResponse.status(HttpStatus.NOT_FOUND)
    }
}

data class NotesResponse(
    val id: String,
    val title: String,
    val category: String,
    val text: String,
) {
    constructor(note: Note) :
        this(note.id, note.title, note.category, note.text)
}
