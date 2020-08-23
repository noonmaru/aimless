package com.github.noonmaru.aimless.plugin

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector
import java.util.*

object Emote {
    private val emotes = TreeMap<String, (Location) -> Unit>(String.CASE_INSENSITIVE_ORDER)

    init {
        register("angry", "화남")  {
            it.world.spawnParticle(
                    Particle.VILLAGER_ANGRY,
                    it.x,
                    it.y + 2.0,
                    it.z,
                    0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    null,
                    true
            )
        }
        register("heart", "하트")  {
            it.world.spawnParticle(
                    Particle.HEART,
                    it.x,
                    it.y + 2.0,
                    it.z,
                    0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    null,
                    true
            )
        }
        register("ㅗ", "망할")  {
            it.world.spawnParticle(
                    Particle.HEART,
                    it.x,
                    it.y + 2.0,
                    it.z,
                    0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    null,
                    true
            )
        }
        register("damage", "상처")  {
            it.world.spawnParticle(
                    Particle.DAMAGE_INDICATOR,
                    it.x,
                    it.y + 2.0,
                    it.z,
                    0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    null,
                    true
            )
        }
        register("critical", "많은상처")  {
            it.world.spawnParticle(
                    Particle.DAMAGE_INDICATOR,
                    it.x,
                    it.y + 2.0,
                    it.z,
                    16,
                    0.5,
                    0.5,
                    0.5,
                    0.0,
                    null,
                    true
            )
        }
        register("spit", "퉤")  {
            val v = it.direction
            it.world.spawnParticle(
                    Particle.SPIT,
                    it.x,
                    it.y + 1.62,
                    it.z,
                    0,
                    v.x,
                    v.y,
                    v.z,
                    1.0,
                    null,
                    true
            )

            it.world.playSound(it, Sound.ENTITY_LLAMA_SPIT, 1.0F, 1.0F)
        }
        register("no", "하지마")  {
            it.world.spawnParticle(
                    Particle.BARRIER,
                    it.x,
                    it.y + 2.5,
                    it.z,
                    0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    null,
                    true
            )

            it.world.playSound(it, Sound.BLOCK_ANVIL_LAND, 0.5F, 0.1F)
        }
        register("note", "즐")  {
            it.world.spawnParticle(
                    Particle.NOTE,
                    it.x,
                    it.y + 2.0,
                    it.z,
                    0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    null,
                    true
            )
        }
        register("rage", "열받")  {
            it.world.spawnParticle(
                    Particle.LAVA,
                    it.x,
                    it.y + 2.0,
                    it.z,
                    128,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    null,
                    true
            )

            it.world.playSound(it, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.2F)
        }
        register("tear", "눈물")  {
            for (i in 0..1) {
                val v = Vector(-0.1 + i * 0.2, 0.1, 0.4)

                v.rotateAroundX(Math.toRadians(it.pitch.toDouble())).rotateAroundY(Math.toRadians(-it.yaw.toDouble()))

                it.world.spawnParticle(
                        Particle.FALLING_WATER,
                        it.x + v.x,
                        it.y + 1.3 + v.y,
                        it.z + v.z,
                        0,
                        0.0,
                        0.0,
                        0.0,
                        1.0,
                        null,
                        true
                )
            }
        }
        register("lava", "피눈물")  {
            for (i in 0..1) {
                val v = Vector(-0.1 + i * 0.2, 0.1, 0.4)

                v.rotateAroundX(Math.toRadians(it.pitch.toDouble())).rotateAroundY(Math.toRadians(-it.yaw.toDouble()))

                it.world.spawnParticle(
                        Particle.FALLING_LAVA,
                        it.x + v.x,
                        it.y + 1.3 + v.y,
                        it.z + v.z,
                        0,
                        0.0,
                        0.0,
                        0.0,
                        1.0,
                        null,
                        true
                )
            }
        }
        register("honey", "눈꼽")  {
            for (i in 0..1) {
                val v = Vector(-0.1 + i * 0.2, 0.1, 0.4)

                v.rotateAroundX(Math.toRadians(it.pitch.toDouble())).rotateAroundY(Math.toRadians(-it.yaw.toDouble()))

                it.world.spawnParticle(
                        Particle.FALLING_HONEY,
                        it.x + v.x,
                        it.y + 1.3 + v.y,
                        it.z + v.z,
                        0,
                        0.0,
                        0.0,
                        0.0,
                        1.0,
                        null,
                        true
                )
            }
        }
    }

    private fun register(name: String, subname: String, emote: (Location) -> Unit) {
        emotes[name] = emote
        emotes[subname] = emote
    }

    fun emoteBy(name: String): ((Location) -> Unit)? {
        return emotes[name]
    }

}