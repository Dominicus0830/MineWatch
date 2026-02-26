package org.domi.minewatch.content.perks

import org.domi.minewatch.api.IPerk
import org.domi.minewatch.core.EventBus
import org.domi.minewatch.core.PostDamageEvent
import org.domi.minewatch.core.PerkManager
import org.domi.minewatch.core.HealPipeline
import org.domi.minewatch.core.HealContext

object VampirePerk : IPerk {
    override val id = "vampire"
    override val name = "흡혈"
    override val description = "적에게 입힌 최종 피해량의 15%를 체력으로 회복합니다."

    private const val HEAL_RATIO = 0.15f

    init {
        // 🌟 마법이 일어나는 곳: 전역 데미지 이벤트 후처리 구독
        EventBus.subscribe(PostDamageEvent::class) { event ->
            val attacker = event.context.attacker ?: return@subscribe

            // 공격자가 '흡혈' 특전을 장착 중이고, 데미지가 들어갔다면?
            if (PerkManager.hasPerk(attacker, id) && event.context.currentDamage > 0) {
                val healAmount = event.context.currentDamage * HEAL_RATIO

                // 힐 파이프라인에 회복 요청! (치유 증폭/힐밴 등은 파이프라인이 알아서 계산함)
                HealPipeline.processHeal(
                    HealContext(healer = attacker, target = attacker, amount = healAmount)
                )
                // println("흡혈 특전 발동! ${healAmount} 회복")
            }
        }
    }
}