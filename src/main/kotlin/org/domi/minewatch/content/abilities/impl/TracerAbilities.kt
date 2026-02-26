package org.domi.minewatch.content.abilities.impl

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.content.abilities.engine.HitscanEngine
import org.domi.minewatch.content.abilities.engine.DashEngine

// [종 - Species] 트레이서의 평타 (펄스 쌍권총)
class PulsePistols : HitscanEngine() {
    override val name = "펄스 쌍권총"
    // 수치는 AbilityBalance 레지스트리에서 가져옵니다
    override val damage = 6.0f
    override val maxRange = 20.0
    override val headshotMultiplier = 2.0f

    override fun canCast(caster: LivingEntity): Boolean {
        return currentAmmo > 0 // 총알이 있어야 쏨
    }
}

// [종 - Species] 트레이서의 이동기 (점멸)
class TracerBlink : DashEngine() {
    override val name = "점멸"
    override val dashForce = 3.5 // 수평 추진력
    override val verticalForce = 0.0 // 위로는 안 뜀

    override fun canCast(caster: LivingEntity): Boolean {
        return currentCooldown <= 0f // 쿨타임이 돌아야 씀 (실제론 스택형 로직 추가 필요)
    }
}