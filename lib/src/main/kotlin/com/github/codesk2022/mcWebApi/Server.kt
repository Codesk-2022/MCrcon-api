package com.github.codesk2022.mcWebApi;

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.github.codesk2022.mcWebApi.web.chat

public fun server(plugin: Plugin): NettyApplicationEngine {
  val config = plugin.getConfig();
  return embeddedServer(Netty, port = config.getInt("port", host = config.getString("host"))) {
    routing {
      chat(this, plugin)
    }
  }
}