package com.github.codesk2022.mcWebApi.web;

import io.ktor.client.engine.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.http.content.resolveResource
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.Serializable
import com.github.codesk2022.mcWebApi.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

@Serializable
data class SendRequestBody(var to: String?, val content: JsonElement);

public fun chat(router: Route, plugin: Plugin){
  val queue = ArrayDeque<() -> Unit>()

  router.route("/chat"){
    post("/send"){
      val req = call.receive<SendRequestBody>();
      if (req.to == null)req.to = "@a";

      val content = (JsonPrimitive(req.content.toString()).toString())
      call.respond("")
      queue.add {
        plugin.server.dispatchCommand(plugin.server.getConsoleSender(), "tellraw ${req.to} ${content}")
      }
    }
  }

  (object: BukkitRunnable() {
    public override fun run(){
      var f: (() -> Unit)?
      while (queue.size != 0){
        f = queue.removeLastOrNull()
        if (f == null)break;
        f()
      }
    }
  }).runTaskTimer(plugin, 0, 20L * 5L)
}