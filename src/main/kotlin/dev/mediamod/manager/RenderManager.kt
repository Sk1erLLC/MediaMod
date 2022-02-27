package dev.mediamod.manager

import dev.mediamod.ui.PlayerComponent
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.universal.UMatrixStack
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.LiteralText
import org.lwjgl.glfw.GLFW

class RenderManager {
    private val window = Window(ElementaVersion.V1)
    private lateinit var playerComponent: PlayerComponent

    fun init() {
        HudRenderCallback.EVENT.register(HudRenderCallback { matrixStack, _ ->
            if (window.children.isEmpty()) {
                playerComponent = PlayerComponent()
                window.addChild(playerComponent)
            }

            window.draw(UMatrixStack(matrixStack))
        })

        val testKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "test",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                KeyBinding.UI_CATEGORY
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            if (testKey.isPressed) {
                window.children.remove(playerComponent)
                playerComponent = PlayerComponent()
                window.addChild(playerComponent)

                it.player?.sendMessage(LiteralText("Reloaded player component"), false)
            }
        })
    }
}