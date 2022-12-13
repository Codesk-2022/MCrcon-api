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
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.Serializable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.github.codesk2022.mcWebApi.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.CommandException

@Serializable
data class SendRequestBody(var to: String?, val content: JsonElement);

public fun chat(router: Route, plugin: Plugin){
  val queue = ArrayDeque<() -> Unit>()

  suspend fun runAsSuspend(func: suspend () -> Unit){
    func.invoke()
  }

  val scope = CoroutineScope(Dispatchers.Default)

  router.route("/chat"){
    post("/send"){
      val req = call.receive<SendRequestBody>();
      if (req.to == null)req.to = "@a";
      this.coroutineContext

      var isResponded = false
      queue.add {
        plugin.server.dispatchCommand(plugin.server.getConsoleSender(), "tellraw ${req.to} ${req.content.toString()}")
        scope.launch {
          call.respond(req.content)
          isResponded = true
        }
      }
      while (!isResponded){
        delay(16) // なとなく60hz
      }
    }
  }

  fun proc(){
    var f: (() -> Unit)?
    while (queue.size != 0){
      f = queue.removeLastOrNull()
      if (f == null)break;
      f()
    }
    (object: BukkitRunnable() {
      public override fun run(){
        proc()
      }
    }).runTaskLater(plugin, 10L) // なるべくすぐキューを消化させたいので0.5秒毎に処理
  }

  (object: BukkitRunnable() {
    public override fun run(){
      proc()
    }
  }).runTaskLater(plugin, 10L)
}