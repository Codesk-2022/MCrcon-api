package com.github.codesk2022.mcWebApi;

import io.ktor.client.engine.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.routing
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.locations.*
import io.ktor.server.request.*

public fun route(router: Route){
  router.route("/chat"){
    post("/send"){
      call.receiveText()
    }
  }
}