package com.ipca.logging

enum class EntityType(val value: String) {
    COLLABORATOR("Collaborator"),
    BENEFICIARY("Beneficiary"),
    COURSE("Course"),
    SCHEDULING("Scheduling"),
    DELIVERY("Delivery"),
    DONATION("Donation"),
    GOOD("Good"),
    EXPIRATION_ALERT("Expiration_Alert"),
    CAMPAIGN("Campaign"),
    NEWS("News");
}
