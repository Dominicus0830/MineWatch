package org.domi.minewatch.content.perks

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.api.IPerk
import org.domi.minewatch.api.IOverwatchActor
import org.domi.minewatch.core.EventBus
import org.domi.minewatch.core.PreDamageEvent
import org.domi.minewatch.core.PerkManager

object GlassCannonPerk : IPerk {
    override val id = "glass_cannon"
    override val name = "유리대포"
    override val description = "최대 체력이 30% 감소하지만, 입히는 피해량이 20% 증가합니다."

    private const val DAMAGE_MULTIPLIER = 1.2f
    private const val HEALTH_PENALTY = 0.7f

    // 1. 장착/해제 시 최대 체력 조작
    override fun onEquip(entity: LivingEntity) {
        val actor = entity as? IOverwatchActor ?: return
        actor.owMaxHealth *= HEALTH_PENALTY
        if (actor.owHealth > actor.owMaxHealth) actor.owHealth = actor.owMaxHealth
    }

    override fun onUnequip(entity: LivingEntity) {
        val actor = entity as? IOverwatchActor ?: return
        actor.owMaxHealth /= HEALTH_PENALTY // 원상 복구
    }

    // 2. 데미지 계산 파이프라인 '진입 직전(Pre)'에 개입하여 데미지 뻥튀기
    init {
        EventBus.subscribe(PreDamageEvent::class) { event ->
            val attacker = event.context.attacker ?: return@subscribe

            // 공격자가 유리대포 특전을 장착 중이라면?
            if (PerkManager.hasPerk(attacker, id)) {
                // 데미지 20% 증가!
                event.context.currentDamage *= DAMAGE_MULTIPLIER
            }
        }
    }
}