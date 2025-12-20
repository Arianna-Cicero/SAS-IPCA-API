package com.ipca.routes

import com.ipca.dto.Course.CourseCreateDTO
import com.ipca.dto.Course.CourseUpdateDTO
import com.ipca.services.CourseService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.courseRoutes() {
    route("/courses") {

        // GET /courses → list all courses
        get {
            try {
                val courses = CourseService.getAllCourses()
                call.respond(courses.map { mapOf("id" to it.first, "name" to it.second) })
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /courses → create course
        post {
            try {
                val request = call.receive<CourseCreateDTO>()
                CourseService.addCourse(request.name)
                call.respond(HttpStatusCode.Created, mapOf("message" to "Course created successfully"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /courses/{id} → get single course
        get("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val course = CourseService.getById(id)
                    ?: return@get call.respondText("Course not found", status = HttpStatusCode.NotFound)

                call.respond(course)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // PUT /courses/{id} → update course
        put("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<CourseUpdateDTO>()
                CourseService.update(id, request.name)
                call.respond(mapOf("message" to "Course updated"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // DELETE /courses/{id} → delete course
        delete("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                CourseService.delete(id)
                call.respond(mapOf("message" to "Course deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
