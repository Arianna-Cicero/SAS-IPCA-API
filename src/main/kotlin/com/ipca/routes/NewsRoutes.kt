package com.ipca.routes

import com.ipca.dto.News.NewsCreateDTO
import com.ipca.dto.News.NewsUpdateDTO
import com.ipca.dto.common.CreateResponseDTO
import com.ipca.services.NewsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.newsRoutes() {
    route("/news") {
        // GET /news → list all news
        get {
            try {
                val newsList = NewsService.getAll()
                call.respond(newsList)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /news → create news
        post {
            try {
                val request = call.receive<NewsCreateDTO>()
                val id = NewsService.create(request)
                call.respond(HttpStatusCode.Created, CreateResponseDTO("News created", id.toString()))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /news/{id} → get single news
        get("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val news = NewsService.getById(id)
                    ?: return@get call.respondText("News not found", status = HttpStatusCode.NotFound)

                call.respond(news)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // PUT /news/{id} → update news
        put("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<NewsUpdateDTO>()
                NewsService.update(id, request)
                call.respond(mapOf("message" to "News updated"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // DELETE /news/{id} → delete news
        delete("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                NewsService.delete(id)
                call.respond(mapOf("message" to "News deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // GET /news/campaign/{campaignId} → list news by campaign
        get("campaign/{campaignId}") {
            try {
                val campaignId = call.parameters["campaignId"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid campaign ID", status = HttpStatusCode.BadRequest)

                val newsList = NewsService.getByCampaignId(campaignId)
                call.respond(newsList)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
