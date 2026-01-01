package com.ipca.unit.validation

import com.ipca.dto.Beneficiary.BeneficiaryCreateDTO
import com.ipca.dto.Beneficiary.BeneficiaryUpdateDTO
import com.ipca.dto.Collaborator.CollaboratorCreateDTO
import com.ipca.dto.Good.GoodCreateDTO
import com.ipca.dto.Good.GoodUpdateDTO
import com.ipca.dto.Donation.DonationCreateDTO
import com.ipca.dto.Campaign.CampaignCreateDTO
import com.ipca.dto.News.NewsCreateDTO
import com.ipca.dto.Entity.EntityCreateDTO
import com.ipca.dto.Course.CourseCreateDTO
import com.ipca.exceptions.*
import com.ipca.validation.*
import java.time.LocalDate
import kotlin.test.*

class BeneficiaryValidatorTest {
    
    @Test
    fun `should pass validation with valid beneficiary data`() {
        val dto = BeneficiaryCreateDTO(
            name = "João Silva",
            studentNumber = 12345,
            email = "joao@ipca.pt",
            telephone = "912345678",
            idCourse = 1
        )
        // Should not throw
        assertDoesNotFail { BeneficiaryValidator.validateCreate(dto) }
    }
    
    @Test
    fun `should fail validation with invalid email`() {
        val dto = BeneficiaryCreateDTO(
            name = "João Silva",
            studentNumber = 12345,
            email = "invalid-email",
            telephone = "912345678",
            idCourse = 1
        )
        assertFailsWith<ValidationException> {
            BeneficiaryValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with invalid phone`() {
        val dto = BeneficiaryCreateDTO(
            name = "João Silva",
            studentNumber = 12345,
            email = "joao@ipca.pt",
            telephone = "91234567", // 8 digits, should be 9
            idCourse = 1
        )
        assertFailsWith<ValidationException> {
            BeneficiaryValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with short name`() {
        val dto = BeneficiaryCreateDTO(
            name = "Jo",
            studentNumber = 12345,
            email = "joao@ipca.pt",
            telephone = "912345678",
            idCourse = 1
        )
        assertFailsWith<ValidationException> {
            BeneficiaryValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should pass update validation with valid data`() {
        val dto = BeneficiaryUpdateDTO(
            name = "João Silva Updated",
            email = "joao.updated@ipca.pt"
        )
        assertDoesNotFail { BeneficiaryValidator.validateUpdate(dto, existingStudentNumber = 12345) }
    }
}

class CollaboratorValidatorTest {
    
    @Test
    fun `should pass validation with valid collaborator data`() {
        val dto = CollaboratorCreateDTO(
            name = "Maria Silva",
            email = "maria@ipca.pt",
            password = "SecurePass123",
            profile = "ADMIN"
        )
        assertDoesNotFail { CollaboratorValidator.validateCreate(dto) }
    }
    
    @Test
    fun `should fail validation with weak password`() {
        val dto = CollaboratorCreateDTO(
            name = "Maria Silva",
            email = "maria@ipca.pt",
            password = "weak", // too short, no complexity
            profile = "ADMIN"
        )
        assertFailsWith<ValidationException> {
            CollaboratorValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with password missing uppercase`() {
        val dto = CollaboratorCreateDTO(
            name = "Maria Silva",
            email = "maria@ipca.pt",
            password = "securepass123", // no uppercase
            profile = "ADMIN"
        )
        assertFailsWith<ValidationException> {
            CollaboratorValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with invalid profile`() {
        val dto = CollaboratorCreateDTO(
            name = "Maria Silva",
            email = "maria@ipca.pt",
            password = "SecurePass123",
            profile = "INVALID_ROLE"
        )
        assertFailsWith<ValidationException> {
            CollaboratorValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should pass validation with valid profiles`() {
        listOf("ADMIN", "COORDINATOR", "VOLUNTEER").forEach { profile ->
            val dto = CollaboratorCreateDTO(
                name = "Maria Silva",
                email = "maria$profile@ipca.pt",
                password = "SecurePass123",
                profile = profile
            )
            assertDoesNotFail { CollaboratorValidator.validateCreate(dto) }
        }
    }
}

class GoodValidatorTest {
    
    @Test
    fun `should pass validation with valid good data`() {
        val dto = GoodCreateDTO(
            name = "Rice",
            category = "FOOD",
            quantity = 100,
            intake = LocalDate.now().minusDays(1),
            dateValidity = LocalDate.now().plusMonths(6),
            donationId = "550e8400-e29b-41d4-a716-446655440000"
        )
        assertDoesNotFail { GoodValidator.validateCreate(dto) }
    }
    
    @Test
    fun `should fail validation with invalid category`() {
        val dto = GoodCreateDTO(
            name = "Rice",
            category = "INVALID_CATEGORY",
            quantity = 100,
            intake = LocalDate.now().minusDays(1),
            dateValidity = LocalDate.now().plusMonths(6),
            donationId = "550e8400-e29b-41d4-a716-446655440000"
        )
        assertFailsWith<ValidationException> {
            GoodValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with negative quantity`() {
        val dto = GoodCreateDTO(
            name = "Rice",
            category = "FOOD",
            quantity = -10,
            intake = LocalDate.now().minusDays(1),
            dateValidity = LocalDate.now().plusMonths(6),
            donationId = "550e8400-e29b-41d4-a716-446655440000"
        )
        assertFailsWith<ValidationException> {
            GoodValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with future intake date`() {
        val dto = GoodCreateDTO(
            name = "Rice",
            category = "FOOD",
            quantity = 100,
            intake = LocalDate.now().plusDays(1),
            dateValidity = LocalDate.now().plusMonths(6),
            donationId = "550e8400-e29b-41d4-a716-446655440000"
        )
        assertFailsWith<ValidationException> {
            GoodValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with past validity date`() {
        val dto = GoodCreateDTO(
            name = "Rice",
            category = "FOOD",
            quantity = 100,
            intake = LocalDate.now().minusDays(10),
            dateValidity = LocalDate.now().minusDays(1),
            donationId = "550e8400-e29b-41d4-a716-446655440000"
        )
        assertFailsWith<ValidationException> {
            GoodValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation when intake is after validity`() {
        val dto = GoodCreateDTO(
            name = "Rice",
            category = "FOOD",
            quantity = 100,
            intake = LocalDate.now(),
            dateValidity = LocalDate.now().minusDays(1),
            donationId = "550e8400-e29b-41d4-a716-446655440000"
        )
        assertFailsWith<ValidationException> {
            GoodValidator.validateCreate(dto)
        }
    }
}

class DonationValidatorTest {
    
    @Test
    fun `should pass validation with valid donation data`() {
        val dto = DonationCreateDTO(
            nameDonor = "John Doe Foundation",
            type = "GOODS",
            dateDonor = LocalDate.now()
        )
        assertDoesNotFail { DonationValidator.validateCreate(dto) }
    }
    
    @Test
    fun `should fail validation with empty donor name`() {
        val dto = DonationCreateDTO(
            nameDonor = "",
            type = "GOODS",
            dateDonor = LocalDate.now()
        )
        assertFailsWith<ValidationException> {
            DonationValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with invalid type`() {
        val dto = DonationCreateDTO(
            nameDonor = "John Doe",
            type = "INVALID_TYPE",
            dateDonor = LocalDate.now()
        )
        assertFailsWith<ValidationException> {
            DonationValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should pass validation with all valid types`() {
        listOf("MONETARY", "GOODS", "SERVICES").forEach { type ->
            val dto = DonationCreateDTO(
                nameDonor = "John Doe",
                type = type,
                dateDonor = LocalDate.now()
            )
            assertDoesNotFail { DonationValidator.validateCreate(dto) }
        }
    }
}

class CampaignValidatorTest {
    
    @Test
    fun `should pass validation with valid campaign data`() {
        val dto = CampaignCreateDTO(
            title = "Winter Aid Campaign",
            description = "Help those in need during winter",
            dateStart = LocalDate.now().minusDays(30),
            dateEnd = LocalDate.now().plusDays(30)
        )
        assertDoesNotFail { CampaignValidator.validateCreate(dto) }
    }
    
    @Test
    fun `should fail validation with empty title`() {
        val dto = CampaignCreateDTO(
            title = "",
            description = "Help those in need during winter",
            dateStart = LocalDate.now().minusDays(30),
            dateEnd = LocalDate.now().plusDays(30)
        )
        assertFailsWith<ValidationException> {
            CampaignValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation when start is after end`() {
        val dto = CampaignCreateDTO(
            title = "Winter Aid Campaign",
            description = "Help those in need during winter",
            dateStart = LocalDate.now().plusDays(30),
            dateEnd = LocalDate.now().minusDays(30)
        )
        assertFailsWith<ValidationException> {
            CampaignValidator.validateCreate(dto)
        }
    }
}

class NewsValidatorTest {
    
    @Test
    fun `should pass validation with valid news data`() {
        val dto = NewsCreateDTO(
            title = "New Aid Initiative",
            content = "We are launching a new program to help the community",
            datePublication = LocalDate.now()
        )
        assertDoesNotFail { NewsValidator.validateCreate(dto) }
    }
    
    @Test
    fun `should fail validation with short title`() {
        val dto = NewsCreateDTO(
            title = "Aid",
            content = "We are launching a new program to help the community",
            datePublication = LocalDate.now()
        )
        assertFailsWith<ValidationException> {
            NewsValidator.validateCreate(dto)
        }
    }
    
    @Test
    fun `should fail validation with future publication date`() {
        val dto = NewsCreateDTO(
            title = "New Aid Initiative",
            content = "We are launching a new program to help the community",
            datePublication = LocalDate.now().plusDays(1)
        )
        assertFailsWith<ValidationException> {
            NewsValidator.validateCreate(dto)
        }
    }
}

class EntityValidatorTest {
    
    @Test
    fun `should pass validation with valid entity data`() {
        val dto = EntityCreateDTO(name = "IPCA Central")
        assertDoesNotFail { EntityValidator.validateCreate(dto) }
    }
    
    @Test
    fun `should fail validation with short name`() {
        val dto = EntityCreateDTO(name = "IP")
        assertFailsWith<ValidationException> {
            EntityValidator.validateCreate(dto)
        }
    }
}

class CourseValidatorTest {
    
    @Test
    fun `should pass validation with valid course data`() {
        val dto = CourseCreateDTO(name = "Computer Science")
        assertDoesNotFail { CourseValidator.validateCreate(dto) }
    }
    
    @Test
    fun `should fail validation with short name`() {
        val dto = CourseCreateDTO(name = "CS")
        assertFailsWith<ValidationException> {
            CourseValidator.validateCreate(dto)
        }
    }
}

private fun <T> assertDoesNotFail(block: () -> T): T {
    return try {
        block()
    } catch (e: Exception) {
        fail("Expected no exception but got ${e::class.simpleName}: ${e.message}")
    }
}
