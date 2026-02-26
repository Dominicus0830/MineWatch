package org.domi.minewatch.content.roles.damage

import org.domi.minewatch.api.IOverwatchActor
import org.domi.minewatch.core.DamageContext

/**
 * [목 - Order] 암살자(Flanker) 세부 역할군
 * 대표 영웅: 트레이서, 겐지 등
 */
abstract class FlankerSubRole : DamageRole() {

    // 암살자 패시브: 적 처치 시 짧은 이속 버프 (예시 기믹)
    override fun onDamageDealt(actor: IOverwatchActor, context: DamageContext) {
        super.onDamageDealt(actor, context)

        // 타겟의 체력이 이 공격으로 0이 되었다면 (처치했다면)
        val targetActor = context.owTarget
        if (targetActor != null && targetActor.owHealth - context.currentDamage <= 0) {
            // (TODO: EventBus를 통해 이속 버프 특수 효과 발동)
            // println("암살자 패시브 발동! 처치 후 이동속도가 증가합니다.")
        }
    }
}