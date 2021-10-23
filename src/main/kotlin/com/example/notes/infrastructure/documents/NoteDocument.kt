package com.example.notes.infrastructure.documents

import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

@Introspected
data class NoteDocument @BsonCreator constructor(
    @BsonProperty("id") val id: String,
    @BsonProperty("title") val title: String,
    @BsonProperty("category") val category: String,
    @BsonProperty("text") val text: String,
)