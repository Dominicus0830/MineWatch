package org.domi.minewatch.api

import net.minecraft.entity.LivingEntity

/**
 * 모든 스킬이 상속받아야 하는 최상위 생명주기 인터페이스
 */
interface IAbility {
    val name: String

    // 쿨타임 및 탄약 상태 (AbilityBalance.kt 에서 주입받음)
    var currentCooldown: Float
    var currentAmmo: Int

    /**
     * 스킬을 시전할 수 있는 상태인지 확인 (기절, 침묵, 탄약 부족, 쿨타임 체크)
     */
    fun canCast(caster: LivingEntity): Boolean

    /**
     * 스킬 시전! (여기서 총알이 나가거나 대시를 합니다)
     */
    fun cast(caster: LivingEntity)

    /**
     * 스킬이 지속되는 동안 매 틱(Tick)마다 실행 (예: 겐지 튕겨내기, 솔저 질주)
     */
    fun onTick(caster: LivingEntity) {}

    /**
     * 기절(Stun)이나 해킹(Hack)을 당했을 때 스킬을 강제 취소하는 로직
     */
    fun interrupt(caster: LivingEntity) {}
}