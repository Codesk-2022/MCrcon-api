package com.github.codesk2022.mcWebApi;

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.locations.*

public fun router(){
  return routing { route("/chat"){
    post("/send"){
      call.
    }
  }}
}