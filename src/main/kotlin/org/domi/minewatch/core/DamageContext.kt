package org.domi.minewatch.core

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.api.IOverwatchActor

/**
 * 데미지 연산 과정을 담고 다니는 컨텍스트 상자
 */
data class DamageContext(
    val attacker: LivingEntity?, // 공격자 (낙사, 화염 등 환경 피해면 null)
    val target: LivingEntity,    // 피해자
    val originalDamage: Float,   // 스킬의 원래 데미지 (예: 100)
    var currentDamage: Float,    // 필터를 거치며 변하는 데미지
    val isCritical: Boolean = false, // 헤드샷 여부
    var isCancelled: Boolean = false // 특전 등에 의해 무효화되었는지 여부
) {
    // 타겟을 OverwatchActor로 안전하게 캐스팅하여 반환
    val owTarget: IOverwatchActor?
        get() = target as? IOverwatchActor
}