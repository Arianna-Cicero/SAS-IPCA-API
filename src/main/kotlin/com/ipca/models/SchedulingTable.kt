package com.ipca.models

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.util.UUID

object SchedulingTable : Table("Scheduling"){
    val id = integer("id_scheduling").autoIncrement()
    val idBeneficiary = integer("id_beneficiary").references(BeneficiaryTable.studentNumber, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val idCollaborator = uuid("id_collaborator").references(CollaboratorTable.id_collaborator, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.SET_NULL)
    val dateDelivery = date("date_delivery")
    val status = varchar("status", 120).nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_Scheduling_ID")
}