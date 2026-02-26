package org.domi.minewatch

import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import org.domi.minewatch.content.perks.GlassCannonPerk
import org.domi.minewatch.content.perks.VampirePerk
import org.domi.minewatch.core.HeroManager
import org.domi.minewatch.core.PerkManager
import org.domi.minewatch.network.ModPackets

class Minewatch : ModInitializer {

    override fun onInitialize() {
        println("Initializing MineWatch Core...")

        // 1. 패킷 및 특전 등록
        ModPackets.registerC2SPackets()
        PerkManager.registerPerk(VampirePerk)
        PerkManager.registerPerk(GlassCannonPerk)

        // 2. 테스트용 명령어 등록: /hero tracer
        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, environment ->
            dispatcher.register(CommandManager.literal("hero")
                .then(CommandManager.argument("heroName", StringArgumentType.string())
                    .executes { context ->
                        val player = context.source.player
                        val heroName = StringArgumentType.getString(context, "heroName")

                        if (player != null) {
                            HeroManager.selectHero(player, heroName)
                        }
                        1 // 명령 성공 반환값
                    }
                )
            )
        }

        println("MineWatch Core Ready!")
    }
}