package com.github.codesk2022.mcWebApi;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import kotlin.math.min;
import kotlin.math.max;
import kotlin.io.println;

data class TPSMemo (
  var second: Double?, var minute: Double?, var hour: Double?, var lastUpdated: Long?
)

public class TPS(
  var lastUpdated: Long = System.currentTimeMillis()
) {
  public var log = ArrayDeque<UInt>()
  private val memo = TPSMemo(null, null, null, null)

  /** append to TPS log */
  fun append(epochtime: Long){
    lastUpdated = System.currentTimeMillis()
    log.add(epochtime.toUInt())
    if (log.size >= 2 * 60 * 60 * 3) {
      log.removeLast()
    }
  }

  fun getMinute(duration: Int = 1): Double {
    if (this.log.size == 0)return 0.0
    val sliced = log.slice((log.size - min(log.size, 2 * 60 * duration))..min(log.size - 1, log.size + 2 * 60 * duration - 1))
    var res: Double = 60 * duration * 1000 / (sliced.sum().toDouble() + 500 * (2 * 60 * duration - sliced.size).toDouble()) * 20.0
    this.memo.minute = res
    this.memo.lastUpdated = System.currentTimeMillis()
    return min(res, 20.0)
  }

  fun getHour(duration: Int = 1): Double {
    if (this.log.size == 0)return 0.0
    val sliced = log.slice((log.size - min(log.size, 2 * 60 * 60 * duration))..min(log.size - 1, log.size + 2 * 60 * 60 * duration - 1))
    var res: Double = 60 * 60 * duration * 1000 / (sliced.sum().toDouble() + 500 * (2 * 60 * 60 * duration - sliced.size).toDouble()) * 20.0
    this.memo.hour = res
    this.memo.lastUpdated = System.currentTimeMillis()
    return min(res, 20.0)
  }
}

public class Plugin: JavaPlugin() {
  public val tps = TPS()

  override fun onEnable(){
    saveDefaultConfig()
    val port = config.getInt("port")
    val host = config.getString("host") ?: "0.0.0.0"
    this.getLogger().info("""
      api is available on http://$host:$port/
    """)
    server(this).start(wait = false)

    val plugin = this
    var prev:Long? = null
    (object: BukkitRunnable() {
      public override fun run(){
        val t = System.currentTimeMillis()
        plugin.tps.append(t - (prev ?: t))
        prev = t
      }
    }).runTaskTimer(this, 0, 10)
  }
}