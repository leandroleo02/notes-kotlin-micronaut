package com.example.notes.infrastructure

import com.example.notes.domain.Note
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CsvNotesRepositoryTest {

    private val csvReader = CsvReader()
    private val csvNotesRepository = CsvNotesRepository(csvReader)

    @Test
    fun readAllNotes() {
        val notes = csvNotesRepository.retrieveAll()
        assertThat(notes).isNotNull
    }

    @Test
    fun findNoteById() {
        val note = csvNotesRepository.findById("6175f4b9d75e0c0d5cda0f35")
        assertThat(note).isNotNull
    }

    @Test
    fun findNoteWithAllValues() {
        val note = csvNotesRepository.findById("6175f4b9d75e0c0d5cda0f35")
        assertThat(note)
            .usingRecursiveComparison()
            .isEqualTo(Note("6175f4b9d75e0c0d5cda0f35", "Cold", "white", "Winter is coming!"))
    }
}
