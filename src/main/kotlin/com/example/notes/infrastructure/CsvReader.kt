package com.example.notes.infrastructure

import com.opencsv.CSVReader
import jakarta.inject.Singleton
import java.io.*

@Singleton
class CsvReader {

    fun readFile(filePath: String): List<Array<String>> {
        val fileReader = BufferedReader(FileReader(filePath))
        return readFile(fileReader)
    }

    fun readFile(fileStream: InputStream): List<Array<String>> {
        val fileReader = BufferedReader(InputStreamReader(fileStream))
        return readFile(fileReader)
    }

    private fun readFile(reader: Reader): List<Array<String>> {
        val csvReader = CSVReader(reader)
        csvReader.skip(1)
        return csvReader.readAll()
    }
}