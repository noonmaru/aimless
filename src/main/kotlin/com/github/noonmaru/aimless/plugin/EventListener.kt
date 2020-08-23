package com.github.noonmaru.aimless.plugin

import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import org.bukkit.event.server.TabCompleteEvent
import org.bukkit.util.NumberConversions
import java.awt.Color
import java.util.*
import kotlin.random.Random.Default.nextInt

class EventListener : Listener {
    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        if (event.result == PlayerLoginEvent.Result.KICK_FULL)
            event.allow()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null

        PlayerList.update()

        val player = event.player

        if (!player.hasPlayedBefore()) {
            player.teleport(getSpawnLocation(player.name))
        }

        player.compassTarget = Bukkit.getWorlds().first().spawnLocation
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage = null

        PlayerList.update()
    }

    @EventHandler(ignoreCancelled = true)
    fun onTabComplete(event: TabCompleteEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        event.isCancelled = true

        val message = event.message.removePrefix("/")
        Emote.emoteBy(message)?.invoke(event.player.location)
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.deathMessage = "${ChatColor.RED}사람이 죽었다."
    }

    @EventHandler(ignoreCancelled = true)
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        event.isCancelled = true

        val message = event.message
        val emote = Emote.emoteBy(message)

        if (emote != null) {
            emote.invoke(event.player.location)

            val component = TextComponent()
            component.text = "[$message]"
            component.color = ChatColor.RED
            component.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/$message")
            event.player.sendMessage(component)
        }
    }

    @EventHandler
    fun onPaperServerListPing(event: PaperServerListPingEvent) {
        val c = Calendar.getInstance()

        event.numPlayers = c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100 + c.get(Calendar.DAY_OF_MONTH)
        event.maxPlayers = c.get(Calendar.HOUR) * 10000 + c.get(Calendar.MINUTE) * 100 + c.get(Calendar.SECOND)
        event.motd = "${ChatColor.of(Color(nextInt(0xFFFFFF)))}${ChatColor.BOLD}AIMLESS SERVER 2020"
        event.playerSample.clear()
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerSign(event: SignChangeEvent) {
        val block = event.block
        val x = block.x
        val z = block.z

        if (!(x in -16..15 && z in -16..15)) {
            event.lines.forEachIndexed { index, s ->
                event.setLine(index, s.removeLang())
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerBookEdit(event: PlayerEditBookEvent) {
        val loc = event.player.location
        val x = loc.blockX
        val z = loc.blockZ

        if (!(x in -16..15 && z in -16..15)) {
            val meta = event.newBookMeta
            val pages = meta.pages
            meta.pages = pages.map { it.removeLang() }
            meta.title?.let {
                meta.title = it.removeLang()
            }

            event.newBookMeta = meta
        }
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        if (event.isBedSpawn || event.isAnchorSpawn) return

        event.respawnLocation = getSpawnLocation(event.player.name)
    }

    private fun getSpawnLocation(name: String): Location {
        val seed = name.hashCode()
        val random = Random(seed.toLong() xor 0x19940423)
        val world = Bukkit.getWorlds().first()
        val border = world.worldBorder
        val size = border.size / 2.0

        val x = random.nextDouble() * size - size / 2.0
        val z = random.nextDouble() * size - size / 2.0
        val block = world.getHighestBlockAt(NumberConversions.floor(x), NumberConversions.floor(z))

        return block.location.add(0.5, 1.0, 0.5)
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerKick(event: PlayerKickEvent) {
        event.isCancelled = true
    }
}

fun String.removeLang(): String {
    return this.replace("([a-zA-Z])|([ㄱ-힣])".toRegex(), "?")
}