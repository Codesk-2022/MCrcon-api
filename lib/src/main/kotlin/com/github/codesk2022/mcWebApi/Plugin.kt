package com.github.codesk2022.mcWebApi;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import kotlin.math.min;

data class TPSMemo (
  var second: Float?, var minute: Float?, var hour: Float?, var lastUpdated: Long?
)

public class TPS(
  var lastUpdated: Long = System.currentTimeMillis()
) {
  private var log = ArrayDeque<Long>()
  private val memo = TPSMemo(null, null, null, null)

  /** append to TPS log */
  fun append(epochtime: Long){
    lastUpdated = System.currentTimeMillis()
    log.add(epochtime)
    if (log.size >= 2 * 60 * 60) {
      log.removeLast()
    }
  }

  fun getSecond(): Float {
    if (this.log.size == 0)return 0f
    val memoCopy = this.memo.copy()
    if (this.lastUpdated - (memoCopy.lastUpdated ?: 0) < 1000 && memoCopy.second != null)return memoCopy.second!!
    var res = 1000f / log.slice(0..min(log.size - 1, 20)).sum().toFloat()
    this.memo.second = res
    this.memo.lastUpdated = System.currentTimeMillis()
    return res
  }

  fun getMinute(): Float {
    if (this.log.size == 0)return 0f
    val memoCopy = this.memo.copy()
    if (this.lastUpdated - (memoCopy.lastUpdated ?: 0) < 1000 * 60 && memoCopy.minute != null)return memoCopy.minute!!
    var res = 1000f * 60f / log.slice(0..min(log.size - 1, 20*60)).sum().toFloat()
    this.memo.minute = res
    this.memo.lastUpdated = System.currentTimeMillis()
    return res
  }

  fun getHour(): Float {
    if (this.log.size == 0)return 0f
    val memoCopy = this.memo.copy()
    if (this.lastUpdated - (memoCopy.lastUpdated ?: 0) < 1000 * 60 * 60 && memoCopy.hour != null)return memoCopy.hour!!
    var res = 1000f * 60f * 60f / log.slice(0..min(log.size - 1, 20*60*60)).sum().toFloat()
    this.memo.hour = res
    this.memo.lastUpdated = System.currentTimeMillis()
    return res
  }
}

public class Plugin: JavaPlugin() {
  public val tps = TPS()

  override fun onEnable(){
    saveDefaultConfig()
    this.getLogger().info("""
      Hello
    """)
    server(this)

    val plugin = this
    (object: BukkitRunnable() {
      public override fun run(){
        plugin.tps.append(System.currentTimeMillis())
      }
    }).runTaskTimer(this, 0, 1)
  }
}