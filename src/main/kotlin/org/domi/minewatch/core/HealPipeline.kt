package org.domi.minewatch.core

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.api.IOverwatchActor

data class HealContext(
    val healer: LivingEntity?,
    val target: LivingEntity,
    var amount: Float,
    var isAntiHealed: Boolean = false // 힐밴 상태
)

object HealPipeline {

    fun processHeal(context: HealContext): Boolean {
        if (context.isAntiHealed) return false // 힐밴이면 즉시 종료

        val actor = context.target as? IOverwatchActor ?: return false
        if (!actor.isOwAlive) return false

        var remainingHeal = context.amount

        // 치유 순서: 체력 -> 방어력 -> 보호막 순으로 복구

        // 1. 체력 복구
        if (remainingHeal > 0 && actor.owHealth < actor.owMaxHealth) {
            val missingHealth = actor.owMaxHealth - actor.owHealth
            if (remainingHeal >= missingHealth) {
                actor.owHealth = actor.owMaxHealth
                remainingHeal -= missingHealth
            } else {
                actor.owHealth += remainingHeal
                remainingHeal = 0f
            }
        }

        // 2. 방어력 복구
        if (remainingHeal > 0 && actor.owArmor < actor.owMaxArmor) {
            val missingArmor = actor.owMaxArmor - actor.owArmor
            if (remainingHeal >= missingArmor) {
                actor.owArmor = actor.owMaxArmor
                remainingHeal -= missingArmor
            } else {
                actor.owArmor += remainingHeal
                remainingHeal = 0f
            }
        }

        // 3. 쉴드 복구
        if (remainingHeal > 0 && actor.owShield < actor.owMaxShield) {
            val missingShield = actor.owMaxShield - actor.owShield
            if (remainingHeal >= missingShield) {
                actor.owShield = actor.owMaxShield
                remainingHeal -= missingShield
            } else {
                actor.owShield += remainingHeal
                remainingHeal = 0f
            }
        }

        return true
    }
}