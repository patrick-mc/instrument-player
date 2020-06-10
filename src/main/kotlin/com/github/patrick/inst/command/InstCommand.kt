package com.github.patrick.inst.command

import com.github.noonmaru.kommand.KommandBuilder
import com.github.noonmaru.kommand.KommandSyntaxException
import com.github.noonmaru.kommand.argument.integer
import com.github.noonmaru.kommand.argument.player
import com.github.patrick.inst.InstObject
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

object InstCommand {
    internal fun register(builder: KommandBuilder) {
        builder.run {
            then("item") {
                require { isOp }
                then("itemType" to InstArguments.material()) {
                    executes {
                        val itemType = it.getArgument("itemType")
                        InstObject.instMaterial = Material.getMaterial(itemType)?: throw KommandSyntaxException("unknown item: $itemType")
                        it.sender.sendMessage("Inst item is now $itemType")
                    }
                }
            }
            then("sound") {
                require { isOp || (this is Player && this == InstObject.instPlayer) }
                then("type") {
                    then("soundType" to InstArguments.sound()) {
                        executes {
                            val soundType = it.getArgument("soundType")
                            InstObject.instSound = InstObject.instSoundMap[soundType]?: throw KommandSyntaxException("unknown sound: $soundType")
                            it.sender.sendMessage("Inst sound is now $soundType")
                        }
                    }
                }
                then("bpm") {
                    then("bpmCount" to integer()) {
                        executes {
                            val bpmCount = it.getArgument("bpmCount").toInt()
                            if (bpmCount in 1..1000) {
                                InstObject.instBpm = bpmCount
                                it.sender.sendMessage("Inst BPM is now $bpmCount")
                            } else
                                throw KommandSyntaxException("bpm count invalid")
                        }
                    }
                }
                then("perBar") {
                    then("perBarCount" to integer()) {
                        executes {
                            val perBarCount = it.getArgument("perBarCount").toInt()
                            if (perBarCount in 1..16) {
                                InstObject.instPerBar = perBarCount
                                it.sender.sendMessage("Inst per-bar is now $perBarCount")
                            } else
                                throw KommandSyntaxException("per-bar count invalid")
                        }
                    }
                }
                then("bar") {
                    then("barCount" to integer()) {
                        executes {
                            val barCount = it.getArgument("barCount").toInt()
                            if (barCount in 1..32) {
                                InstObject.instBar = barCount
                                it.sender.sendMessage("Inst total bars are now $barCount")
                            } else
                                throw KommandSyntaxException("total bar count invalid")
                        }
                    }
                }
                then("start") {

                }
                then("stop") {

                }
            }
            then("player") {
                require { isOp }
                then("set") {
                    then("player" to player()) {
                        executes {
                            val player = Bukkit.getPlayerExact(it.getArgument("player"))
                            player?.run {
                                InstObject.instPlayer = this
                                it.sender.sendMessage("Inst player is now $player")
                            }?: throw KommandSyntaxException("unknown player: $player")
                        }
                    }
                }
                then("add") {
                    then("player" to player()) {
                        executes {
                            val player = Bukkit.getPlayerExact(it.getArgument("player"))
                            player?.run {
                                InstObject.instSupporter.add(this)
                                it.sender.sendMessage("$player is now part of Inst supporters")
                            }?: throw KommandSyntaxException("unknown player: $player")
                        }
                    }
                }
                then("remove") {
                    then("player" to player()) {
                        executes {
                            val player = Bukkit.getPlayerExact(it.getArgument("player"))
                            player?.run {
                                InstObject.instSupporter.remove(this)
                                it.sender.sendMessage("$player is no longer part of Inst supporters")
                            }?: throw KommandSyntaxException("unknown player: $player")
                        }
                    }
                }
                then("clear") {
                    executes {
                        InstObject.instPlayer = null
                        InstObject.instSupporter.clear()
                        it.sender.sendMessage("Inst player is now null")
                    }
                }
            }
        }
    }
}