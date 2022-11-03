package com.github.codesk2022.mcWebApi;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin: JavaPlugin() {
  override fun onEnable(){
    saveDefaultConfig()
    this.getLogger().info("""
      Hello
    """)
    server(this)
  }
}