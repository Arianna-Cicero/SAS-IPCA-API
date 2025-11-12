package com.ipca.routes

import com.ipca.services.CourseService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class CourseRequest(val name: String)

fun Route.courseRoutes() {
    route("/courses") {

        // GET /courses → lista todos os cursos
        get {
            val courses = CourseService.getAllCourses()
            call.respond(courses.map { mapOf("id" to it.first, "name" to it.second) })
        }

        // POST /courses → adiciona um novo curso
        post {
            val request = call.receive<CourseRequest>()
            CourseService.addCourse(request.name)
            call.respond(mapOf("message" to "Course added successfully"))
        }
    }
}
