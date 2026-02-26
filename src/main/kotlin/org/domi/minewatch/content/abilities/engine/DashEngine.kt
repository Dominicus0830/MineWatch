package org.domi.minewatch.content.abilities.engine

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.api.IAbility

/**
 * [속 - Genus] 돌진/이동형(Dash) 스킬의 뼈대
 */
abstract class DashEngine : IAbility {
    // 하위 클래스에서 설정할 추진력 계수
    abstract val dashForce: Double
    abstract val verticalForce: Double // 위로 뛰는 힘 (윈스턴 점프팩 등)

    override var currentCooldown: Float = 0f
    override var currentAmmo: Int = 0 // 보통 이동기는 탄약을 쓰지 않지만 인터페이스 규격상 둠

    override fun cast(caster: LivingEntity) {
        // 바라보는 방향(Look Vector) 추출
        val lookVec = caster.getRotationVec(1.0f).normalize()

        // 이동 벡터 계산
        val dashVec = lookVec.multiply(dashForce)

        // 엔티티의 가속도(Velocity) 덮어쓰기
        caster.addVelocity(dashVec.x, dashVec.y + verticalForce, dashVec.z)

        // 클라이언트와 위치 동기화를 위해 velocity dirty flag 설정
        caster.velocityDirty = true
    }
}