package com.github.noonmaru.aimless.plugin

import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.NumberConversions
import java.util.*

/**
 * @author Nemo
 */
class AimlessPlugin : JavaPlugin() {
    override fun onEnable() {
        for (world in Bukkit.getWorlds()) {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, true)
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false)
            world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false)
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true)
        }

        Bukkit.getWorlds().first().apply {
            val border = worldBorder
            border.center = Location(this, 0.0, 0.0, 0.0)
            border.size = 16384.0
            spawnLocation = getHighestBlockAt(0, 0).location
        }

        server.pluginManager.registerEvents(EventListener(), this)
        server.scheduler.runTaskTimer(this, PlayerList, 0L , 1L)
        server.scheduler.runTaskTimer(this, Restarter(), 20L * 60L, 20L * 60L)
    }
}

class Restarter: Runnable {
    private val time = System.currentTimeMillis()

    override fun run() {
        val elapsedTime = System.currentTimeMillis() - time

        val restartTime = 1000L * 60L * 60L * 2L

        if (elapsedTime >= restartTime) {
            for (player in Bukkit.getOnlinePlayers()) {
                player.sendMessage("서버가 재시작됩니다.")
            }
            Bukkit.shutdown()
        } else if (elapsedTime >= restartTime - 60000L) {
            Bukkit.broadcastMessage("1분 뒤 서버가 재시작됩니다.")
        }
    }
}