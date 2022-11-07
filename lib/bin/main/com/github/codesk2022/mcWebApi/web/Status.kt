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
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.Serializable
import com.github.codesk2022.mcWebApi.Plugin;

@Serializable
data class StatusResponseBody(val second: Float, val minute: Float, val hour: Float);

public fun status(router: Route, plugin: Plugin){
  router.get("/status"){
    call.respond<StatusResponseBody>(StatusResponseBody(plugin.tps.getSecond(), plugin.tps.getMinute(), plugin.tps.getHour()))
  }
}