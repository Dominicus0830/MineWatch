package org.domi.minewatch.client

import net.fabricmc.api.ClientModInitializer
import org.domi.minewatch.client.hud.OverwatchHUD
import org.domi.minewatch.client.input.KeybindHandler

class MinewatchClient : ClientModInitializer {

    override fun onInitializeClient() {
        println("Initializing MineWatch Client...")

        // 시각(HUD) 및 조작(Keybind)은 클라이언트에서만 렌더링/감지되어야 합니다.
        KeybindHandler.register()
        OverwatchHUD.register()

        println("MineWatch Client Ready!")
    }
}