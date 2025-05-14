package com.example.pantryplan.core.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.EnumSet
import java.util.UUID

@Serializable
data class Recipe(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val title: String,
    val description: String,
    val tags: List<String>,
    @Serializable(with = EnumSetSerializer::class)
    val allergens: EnumSet<Allergen>,
    val imageUrl: String?,
    val instructions: List<String>,
    // REVIEW: Is this suitable enough for generic referencing of pantry items
    val ingredients: List<Ingredient>,

    // Metadata
    val prepTime: Float,
    val cookTime: Float,
    val nutrition: NutritionInfo
)

// Implement serializers for the properties which use Java classes.
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}

object EnumSetSerializer : KSerializer<EnumSet<*>> {
    override val descriptor = SerialDescriptor(
        "BetterThanAStruct",
        ByteArraySerializer().descriptor,
    )

    override fun deserialize(decoder: Decoder): EnumSet<*> {
        val bis = ByteArrayInputStream(decoder.decodeSerializableValue(ByteArraySerializer()))
        val obj = ObjectInputStream(bis).readObject()

        if (obj is EnumSet<*>) {
            return obj
        } else {
            throw ClassCastException("oh... that's gore. that's gore of my comfort enum")
        }
    }

    override fun serialize(encoder: Encoder, value: EnumSet<*>) {
        val bos = ByteArrayOutputStream()

        try {
            val out = ObjectOutputStream(bos)
            out.writeObject(value)
            out.flush()
        } catch (_: Exception) {
            throw RuntimeException("it works")
        }
        encoder.encodeSerializableValue(ByteArraySerializer(), bos.toByteArray())
    }
}