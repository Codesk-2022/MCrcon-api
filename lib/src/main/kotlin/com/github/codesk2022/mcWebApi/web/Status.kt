package com.github.codesk2022.mcWebApi.web;

import io.ktor.client.engine.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.Serializable
import com.github.codesk2022.mcWebApi.Plugin;

@Serializable
data class StatusResponseBody(val minute: Double, val hour: Double);

@Serializable
data class StatusRequestBody(val dulation: Int);

public fun status(router: Route, plugin: Plugin){
  router.get("/status/log"){
    call.respond(JsonArray(plugin.tps.log.map { x -> JsonPrimitive(x.toDouble()) }))
  }
  router.get("/status/minute"){
    val res = plugin.tps.getMinute((call.request.queryParameters["duration"] ?: "1").toInt())
    call.respond(res)
  }
  router.get("/status/hour"){
    val res = plugin.tps.getHour((call.request.queryParameters["duration"] ?: "1").toInt())
    call.respond(res)
  }
  router.get("/status"){
    call.respond<StatusResponseBody>(StatusResponseBody(plugin.tps.getMinute(), plugin.tps.getHour()))
  }
}