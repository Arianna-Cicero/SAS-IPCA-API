package com.ipca.unit.dto

import com.ipca.dto.Beneficiary.BeneficiaryCreateDTO
import com.ipca.dto.Campaign.CampaignResponseDTO
import com.ipca.dto.Donation.DonationResponseDTO
import kotlinx.serialization.json.Json
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DTOSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `should serialize and deserialize BeneficiaryCreateDTO`() {
        val dto = BeneficiaryCreateDTO(
            name = "João Silva",
            studentNumber = 12345,
            email = "joao@ipca.pt",
            telephone = "912345678",
            idCourse = 1
        )

        val serialized = json.encodeToString(BeneficiaryCreateDTO.serializer(), dto)
        val deserialized = json.decodeFromString(BeneficiaryCreateDTO.serializer(), serialized)

        assertEquals(dto.name, deserialized.name)
        assertEquals(dto.studentNumber, deserialized.studentNumber)
        assertEquals(dto.email, deserialized.email)
        assertEquals(dto.telephone, deserialized.telephone)
        assertEquals(dto.idCourse, deserialized.idCourse)
    }

    @Test
    fun `should serialize BeneficiaryCreateDTO to valid JSON`() {
        val dto = BeneficiaryCreateDTO(
            name = "Maria",
            studentNumber = 54321,
            email = "maria@ipca.pt",
            telephone = "987654321",
            idCourse = 2
        )

        val serialized = json.encodeToString(BeneficiaryCreateDTO.serializer(), dto)

        assertNotNull(serialized)
        assert(serialized.contains("\"name\""))
        assert(serialized.contains("\"studentNumber\""))
        assert(serialized.contains("Maria"))
    }

    @Test
    fun `should serialize DonationResponseDTO with nullable description`() {
        val dtoWithDescription = DonationResponseDTO(
            id = "550e8400-e29b-41d4-a716-446655440000",
            nameDonor = "John Doe",
            type = "food",
            dateDonor = LocalDate.of(2025, 12, 20),
            description = "Food donation"
        )

        val serialized = json.encodeToString(DonationResponseDTO.serializer(), dtoWithDescription)
        val deserialized = json.decodeFromString(DonationResponseDTO.serializer(), serialized)

        assertEquals(dtoWithDescription.description, deserialized.description)
    }

    @Test
    fun `should serialize DonationResponseDTO with null description`() {
        val dtoWithoutDescription = DonationResponseDTO(
            id = "550e8400-e29b-41d4-a716-446655440000",
            nameDonor = "John Doe",
            type = "clothing",
            dateDonor = LocalDate.of(2025, 12, 20),
            description = null
        )

        val serialized = json.encodeToString(DonationResponseDTO.serializer(), dtoWithoutDescription)
        val deserialized = json.decodeFromString(DonationResponseDTO.serializer(), serialized)

        assertEquals(null, deserialized.description)
    }

    @Test
    fun `should serialize CampaignResponseDTO with LocalDate`() {
        val dto = CampaignResponseDTO(
            id = 1,
            title = "Christmas Campaign",
            description = "Campaign for Christmas",
            dateStart = LocalDate.of(2025, 12, 1),
            dateEnd = LocalDate.of(2025, 12, 31),
            totalCollected = 5000
        )

        val serialized = json.encodeToString(CampaignResponseDTO.serializer(), dto)
        val deserialized = json.decodeFromString(CampaignResponseDTO.serializer(), serialized)

        assertEquals(dto.dateStart, deserialized.dateStart)
        assertEquals(dto.dateEnd, deserialized.dateEnd)
        assertEquals(dto.totalCollected, deserialized.totalCollected)
    }
}
