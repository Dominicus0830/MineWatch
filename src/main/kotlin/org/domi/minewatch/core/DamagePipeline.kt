package org.domi.minewatch.core

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.api.IOverwatchActor

object DamagePipeline {

    /**
     * 게임 내의 모든 데미지는 반드시 이 함수를 거쳐야 합니다.
     * @return 최종적으로 피해를 입혔다면 true
     */
    fun processDamage(context: DamageContext): Boolean {
        // 1. 이벤트 버스 발송 (특전 개입 구간)
        EventBus.post(PreDamageEvent(context))

        if (context.isCancelled) return false

        val actor = context.owTarget ?: return false
        if (!actor.isOwAlive) return false

        var remainingDamage = context.currentDamage

        // 2. 보호막(Shield) 연산: 데미지 감소 없이 1:1로 깎임
        if (actor.owShield > 0) {
            if (actor.owShield >= remainingDamage) {
                actor.owShield -= remainingDamage
                remainingDamage = 0f
            } else {
                remainingDamage -= actor.owShield
                actor.owShield = 0f
            }
        }

        // 3. 방어력(Armor) 연산: 오버워치 룰 (데미지 30% 감소)
        if (remainingDamage > 0 && actor.owArmor > 0) {
            val armorDamageMultiplier = 0.7f // 30% 뎀감
            val effectiveDamageToArmor = remainingDamage * armorDamageMultiplier

            if (actor.owArmor >= effectiveDamageToArmor) {
                actor.owArmor -= effectiveDamageToArmor
                remainingDamage = 0f
            } else {
                // 아머가 뚫린 경우: 남은 아머만큼만 뎀감 적용, 나머지는 그대로 체력으로
                val damageBlockedByArmor = actor.owArmor / armorDamageMultiplier
                remainingDamage -= damageBlockedByArmor
                actor.owArmor = 0f
            }
        }

        // 4. 순수 체력(Health) 연산
        if (remainingDamage > 0) {
            actor.owHealth -= remainingDamage
            if (actor.owHealth < 0) actor.owHealth = 0f
        }

        // 5. 마인크래프트 바닐라 UI 및 시스템 동기화 (죽음 판정 등)
        syncVanillaHealth(context.target, actor)

        // 6. 데미지 후처리 이벤트 발송 (흡혈 특전 등)
        EventBus.post(PostDamageEvent(context))

        return true
    }

    // 오버워치 체력 비율을 마인크래프트 하트(20.0f)에 맞춰 렌더링되도록 동기화
    private fun syncVanillaHealth(vanillaEntity: LivingEntity, actor: IOverwatchActor) {
        val maxTotal = actor.owMaxHealth + actor.owMaxArmor + actor.owMaxShield
        val currentTotal = actor.owHealth + actor.owArmor + actor.owShield

        if (maxTotal <= 0) return

        val healthPercent = currentTotal / maxTotal
        val vanillaTargetHealth = vanillaEntity.maxHealth * healthPercent

        vanillaEntity.health = vanillaTargetHealth

        if (currentTotal <= 0f && vanillaEntity.isAlive) {
            // 🌟 수정됨: 엔티티가 있는 월드를 가져와서 ServerWorld인지 확인 후 데미지 함수에 전달합니다.
            val world = vanillaEntity.entityWorld
            if (world is net.minecraft.server.world.ServerWorld) {
                vanillaEntity.damage(world, vanillaEntity.damageSources.generic(), Float.MAX_VALUE)
            }
        }
    }
}