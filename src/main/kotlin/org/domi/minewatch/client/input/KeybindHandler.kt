package org.domi.minewatch.client.input

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.domi.minewatch.network.CastAbilityPayload
import org.lwjgl.glfw.GLFW

object KeybindHandler {
    private lateinit var ability1Key: KeyBinding // Shift
    private lateinit var ability2Key: KeyBinding // E
    private lateinit var ultimateKey: KeyBinding // Q

    fun register() {
        // 🌟 수정됨: String 대신 1.21 규격인 KeyBinding.Category.MISC 사용!
        ability1Key = KeyBindingHelper.registerKeyBinding(KeyBinding(
            "key.minewatch.ability1",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_SHIFT,
            KeyBinding.Category.MISC
        ))

        ability2Key = KeyBindingHelper.registerKeyBinding(KeyBinding(
            "key.minewatch.ability2",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_E,
            KeyBinding.Category.MISC
        ))

        ultimateKey = KeyBindingHelper.registerKeyBinding(KeyBinding(
            "key.minewatch.ultimate",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Q,
            KeyBinding.Category.MISC
        ))

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client ->
            while (ability1Key.wasPressed()) {
                sendCastPacket(2)
            }
            while (ability2Key.wasPressed()) {
                sendCastPacket(3)
            }
            while (ultimateKey.wasPressed()) {
                sendCastPacket(4)
            }
        })
    }

    private fun sendCastPacket(slotIndex: Int) {
        ClientPlayNetworking.send(CastAbilityPayload(slotIndex))
    }
}