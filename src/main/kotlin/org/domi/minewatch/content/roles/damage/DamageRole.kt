package org.domi.minewatch.content.roles.damage

import org.domi.minewatch.content.roles.OwRole

/**
 * [강 - Class] 공격군 기본 패시브
 */
abstract class DamageRole : OwRole() {
    override val roleName = "공격"

    // 2026년 기준 공격군 패시브: 자가 치유 감소 효과 유발 (파이프라인 연동 필요)
    val HEAL_REDUCTION_MULTIPLIER = 0.8f
}