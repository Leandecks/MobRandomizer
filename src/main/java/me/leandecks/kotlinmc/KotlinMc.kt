package me.leandecks.kotlinmc

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class KotlinMc : JavaPlugin(), Listener, CommandExecutor {
    private var challengeStarted = false

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onBreakBlock(event: BlockBreakEvent) {
        if (challengeStarted) {
            val entities = EntityType.values()
            val randomNumber = Random.nextInt(entities.size)
            val randomEntity = entities[randomNumber]

            val brokenBlock = event.block
            val brokenBlockLocation = brokenBlock.location
            brokenBlockLocation.world?.spawnEntity(brokenBlockLocation, randomEntity)
        }
    }

    @EventHandler
    fun onDragonDeath(event: EntityDeathEvent) {
        if (event.entityType == EntityType.ENDER_DRAGON) {
            challengeStarted = false
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "timer pause")
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamemode spectator @a")
            Bukkit.broadcastMessage("${ChatColor.GOLD}${ChatColor.BOLD}> The Ender Dragon was defeated. Challenge Won!")
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            when(command.name) {
                "challenge" -> {
                    if (!challengeStarted) {
                        challengeStarted = true
                        Bukkit.broadcastMessage("${ChatColor.BLUE}> Challenge started")
                    } else {
                        challengeStarted = false
                        Bukkit.broadcastMessage("${ChatColor.BLUE}> Challenge stopped")
                    }
                }
            }
        }

        return true
    }
}