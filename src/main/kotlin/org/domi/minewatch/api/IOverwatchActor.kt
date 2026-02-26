package org.domi.minewatch.api

import org.domi.minewatch.content.heroes.AbilityLoadout
import org.domi.minewatch.content.roles.OwRole
import org.domi.minewatch.data_registry.HeroStats

/**
 * 오버워치의 시스템이 적용된 모든 생명체(플레이어, 몹, 소환물 등)가 가져야 할 인터페이스
 */
interface IOverwatchActor {
    // 1. 순수 체력 (Health)
    var owHealth: Float
    var owMaxHealth: Float

    // 2. 방어력 (Armor) - 데미지 감소 효과
    var owArmor: Float
    var owMaxArmor: Float

    // 3. 보호막 (Shield) - 비전투 시 자동 회복
    var owShield: Float
    var owMaxShield: Float

    var currentRole: OwRole?
    var currentLoadout: AbilityLoadout?

    // 현재 영웅 스탯 주입 (과/속 단계에서 호출됨)
    fun applyHeroStats(stats: HeroStats) {
        this.owMaxHealth = stats.maxHealth
        this.owHealth = stats.maxHealth
        this.owMaxArmor = stats.maxArmor
        this.owArmor = stats.maxArmor
        this.owMaxShield = stats.maxShield
        this.owShield = stats.maxShield
    }

    // 살아있는지 확인
    val isOwAlive: Boolean
        get() = owHealth > 0f

    // 총 체력 (UI 표시용)
    val totalEffectiveHealth: Float
        get() = owHealth + owArmor + owShield
}