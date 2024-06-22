package dev.bruno.wheretowatch.services.discover

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class LocalDateSerializer : KSerializer<LocalDate?> {

    private val stringSerializer = String.serializer().nullable
    override val descriptor: SerialDescriptor
        get() = stringSerializer.descriptor

    override fun deserialize(decoder: Decoder): LocalDate? {
        val result = stringSerializer.deserialize(decoder)
        if (result.isNullOrBlank()) return null
        val date = result.substring(0,10) //in case the string contains time/time zone we get only the date

        return try {
            LocalDate.parse(date)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun serialize(encoder: Encoder, value: LocalDate?) {
        stringSerializer.serialize(encoder, value?.toString())
    }
}
