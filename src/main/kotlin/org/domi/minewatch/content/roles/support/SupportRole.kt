package org.domi.minewatch.content.roles.support

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.content.roles.OwRole
import org.domi.minewatch.api.IOverwatchActor
import org.domi.minewatch.core.HealContext
import org.domi.minewatch.core.HealPipeline

/**
 * [강 - Class] 지원군 기본 패시브
 */
abstract class SupportRole : OwRole() {
    override val roleName = "지원"
    private var outOfCombatTicks = 0

    // 지원군 공통 패시브: 비전투 2.5초(50틱) 경과 시 초당 15 자동 회복
    override fun onTick(actor: IOverwatchActor, entity: LivingEntity) {
        // (피격 시 outOfCombatTicks를 0으로 초기화하는 로직은 EventBus 쪽에 구현)
        outOfCombatTicks++
        if (outOfCombatTicks >= 50 && actor.owHealth < actor.owMaxHealth) {
            // 1틱당 0.75 회복 = 초당 15 회복
            HealPipeline.processHeal(HealContext(entity, entity, 0.75f))
        }
    }
}

/**
 * [목 - Order] 의무관(Medic) 세부 역할군
 * 대표 영웅: 메르시, 미즈키(신규) 등
 */
abstract class MedicSubRole : SupportRole() {

    // 의무관 패시브: 아군을 치유할 때, 그 치유량의 25%만큼 자신도 회복합니다.
    private val SELF_HEAL_RATIO = 0.25f

    override fun onHealDealt(actor: IOverwatchActor, context: HealContext) {
        super.onHealDealt(actor, context)

        val healerEntity = context.healer ?: return

        // 남을 힐했을 때만 발동 (자힐 무한 루프 방지)
        if (healerEntity != context.target) {
            val selfHealAmount = context.amount * SELF_HEAL_RATIO

            // 파이프라인에 새로운 힐 요청을 보냄!
            HealPipeline.processHeal(HealContext(
                healer = healerEntity,
                target = healerEntity, // 나 자신을 타겟으로
                amount = selfHealAmount
            ))
        }
    }
}