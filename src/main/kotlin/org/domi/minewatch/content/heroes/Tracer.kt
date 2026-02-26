package org.domi.minewatch.content.heroes

import org.domi.minewatch.content.roles.damage.FlankerSubRole
import org.domi.minewatch.data_registry.HeroBalance
import org.domi.minewatch.data_registry.HeroStats
import org.domi.minewatch.content.abilities.impl.PulsePistols
import org.domi.minewatch.content.abilities.impl.TracerBlink

/**
 * [과 - Family] 트레이서
 * 암살자(FlankerSubRole)의 유전자를 그대로 물려받습니다.
 */
class Tracer : FlankerSubRole() {
    override val roleName = "트레이서"

    // 1. 스탯 주입 (코드 레지스트리에서 150 체력과 이속을 가져옴)
    val stats: HeroStats = HeroBalance.TRACER

    // 2. 코틀린의 `apply` 블록을 활용한 우아한 스킬 조립 (Builder Pattern)
    val loadout = AbilityLoadout().apply {
        primary = PulsePistols()
        ability1 = TracerBlink()
        // ultimate = PulseBomb() // 나중에 부품을 깎으면 주석만 해제하면 끝!
    }
}