package org.domi.minewatch.client.hud


import com.mojang.authlib.minecraft.client.MinecraftClient
import org.domi.minewatch.api.IOverwatchActor

object OverwatchHUD {

    fun register() {
        // 🌟 수정됨: 파라미터 타입을 생략하여 코틀린이 (DrawContext, RenderTickCounter)를 자동 추론하게 만듭니다.
        HudRenderCallback.EVENT.register { drawContext, tickCounter ->
            val player = MinecraftClient.getInstance().player ?: return@register
            val actor = player as? IOverwatchActor ?: return@register

            // 영웅을 선택하지 않은 상태면 바닐라 하트를 그대로 둠
            if (actor.currentRole == null) return@register

            // 화면 크기 가져오기
            val screenWidth = MinecraftClient.getInstance().window.scaledWidth
            val screenHeight = MinecraftClient.getInstance().window.scaledHeight

            // 체력바 시작 위치 (좌측 하단)
            val startX = 20
            val startY = screenHeight - 30
            val barHeight = 10

            // 1의 체력당 몇 픽셀로 그릴 것인가 (예: 체력 150 = 150픽셀 길이)
            val pixelPerHealth = 1.0f

            val healthWidth = (actor.owHealth * pixelPerHealth).toInt()
            val armorWidth = (actor.owArmor * pixelPerHealth).toInt()
            val shieldWidth = (actor.owShield * pixelPerHealth).toInt()

            // 1. 순수 체력 (흰색) 렌더링
            if (healthWidth > 0) {
                drawContext.fill(startX, startY, startX + healthWidth, startY + barHeight, 0xFFFFFFFF.toInt())
            }

            // 2. 방어력 (노란색/주황색) 렌더링 (체력바 바로 오른쪽에 이어 붙임)
            val armorStartX = startX + healthWidth
            if (armorWidth > 0) {
                drawContext.fill(armorStartX, startY, armorStartX + armorWidth, startY + barHeight, 0xFFFFA500.toInt())
            }

            // 3. 보호막 (파란색) 렌더링 (방어력바 바로 오른쪽에 이어 붙임)
            val shieldStartX = armorStartX + armorWidth
            if (shieldWidth > 0) {
                drawContext.fill(shieldStartX, startY, shieldStartX + shieldWidth, startY + barHeight, 0xFF00BFFF.toInt())
            }

            // 4. 텍스트로 총 체력량 표시 (예: 150 / 150)
            val totalCurrent = (actor.owHealth + actor.owArmor + actor.owShield).toInt()
            val totalMax = (actor.owMaxHealth + actor.owMaxArmor + actor.owMaxShield).toInt()
            val hpText = "$totalCurrent / $totalMax"

            drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                hpText,
                startX, startY - 12,
                0xFFFFFF,
                true // 그림자 효과
            )
        }
    }
}