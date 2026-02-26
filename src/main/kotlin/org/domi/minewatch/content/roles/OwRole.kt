package org.domi.minewatch.content.roles

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.api.IOverwatchActor
import org.domi.minewatch.core.DamageContext
import org.domi.minewatch.core.HealContext

/**
 * 모든 역할군(Role)과 세부 역할군(SubRole)의 최상위 부모 클래스
 */
abstract class OwRole {
    abstract val roleName: String

    // 1. 내가 데미지를 받을 때 (방어력 계산 전, 파이프라인에서 호출)
    open fun onDamageTaken(actor: IOverwatchActor, context: DamageContext) {}

    // 2. 내가 남을 공격할 때 (파이프라인에서 호출)
    open fun onDamageDealt(actor: IOverwatchActor, context: DamageContext) {}

    // 3. 내가 남을 치유할 때 (힐 파이프라인에서 호출)
    open fun onHealDealt(actor: IOverwatchActor, context: HealContext) {}

    // 4. 매 틱마다 실행 (비전투 시 자동 회복 등 패시브용)
    open fun onTick(actor: IOverwatchActor, entity: LivingEntity) {}
}