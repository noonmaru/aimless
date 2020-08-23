package com.github.noonmaru.aimless.plugin

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedGameProfile
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object PlayerList: Runnable {

    private var update = false

    fun update() {
        update = true
    }

    override fun run() {
        if (update) {
            update = false
            updatePlayerList()
        }
    }

    private fun updatePlayerList() {
        val packet = PacketContainer(PacketType.Play.Server.PLAYER_INFO)
        val list = ArrayList<PlayerInfoData>()

        for (offlinePlayer in Bukkit.getOfflinePlayers().asSequence()) {
            val profile = if (offlinePlayer is Player) WrappedGameProfile.fromPlayer(offlinePlayer)
            else {
                WrappedGameProfile.fromOfflinePlayer(offlinePlayer).withName(offlinePlayer.name)
            }

            list += PlayerInfoData(
                    profile,
                    0,
                    EnumWrappers.NativeGameMode.NOT_SET,
                    WrappedChatComponent.fromText(offlinePlayer.name)
            )
        }

        packet.playerInfoAction.write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER)
        packet.playerInfoDataLists.write(0, list)

        val pm = ProtocolLibrary.getProtocolManager()

        for (player in Bukkit.getOnlinePlayers()) {
            pm.sendServerPacket(player, packet)
        }
    }
}