package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.test.assertEquals

class JsonCustomClassDeserializer {

    @Test
    fun `Нобходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule().also {
                it.addDeserializer(Client7::class.java, Client7Deserializer())
            })

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }
}

class Client7Deserializer : JsonDeserializer<Client7?>() {
    @Throws(IOException::class)

    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext?): Client7 {
        val oc = jsonParser.codec
        val node = oc.readTree<JsonNode>(jsonParser)
        return Client7(firstName = node["client"].textValue().split(" ")[1],
            lastName = node["client"].textValue().split(" ")[0],
            middleName = node["client"].textValue().split(" ")[2])
    }
}

