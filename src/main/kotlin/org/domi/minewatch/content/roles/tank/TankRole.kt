package org.domi.minewatch.content.roles.tank

import org.domi.minewatch.content.roles.OwRole
import org.domi.minewatch.api.IOverwatchActor
import org.domi.minewatch.core.DamageContext

/**
 * [강 - Class] 돌격군 기본 패시브
 */
abstract class TankRole : OwRole() {
    override val roleName = "돌격"

    // 탱커 공통 패시브: 궁극기 셔틀 방지 (받는 피해로 인한 궁극기 충전량 감소 로직 등)
    // 넉백 저항은 마인크래프트 바닐라 Attribute(Knockback Resistance)로 엔티티 생성 시 주입합니다.
}

/**
 * [목 - Order] 투사(Bruiser) 세부 역할군
 * 대표 영웅: 마우가, 오리사 등
 */
abstract class BruiserSubRole : TankRole() {

    // 투사 패시브: 헤드샷(치명타) 피해를 25% 덜 받습니다.
    private val CRIT_REDUCTION_MULTIPLIER = 0.75f

    override fun onDamageTaken(actor: IOverwatchActor, context: DamageContext) {
        super.onDamageTaken(actor, context)

        // 파이프라인이 전달한 데미지 컨텍스트를 확인
        if (context.isCritical) {
            // 데미지를 깎아버립니다!
            context.currentDamage *= CRIT_REDUCTION_MULTIPLIER
            // 로그용 (실제 개발 시 삭제)
            // println("투사 패시브 발동! 치명타 피해가 감소했습니다.")
        }
    }
}