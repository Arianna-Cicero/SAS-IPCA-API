package com.ipca.auth

import io.ktor.server.auth.Principal

data class CollaboratorPrincipal(
    val id: String
) : Principal
