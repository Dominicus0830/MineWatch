package org.domi.minewatch.content.abilities.engine

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World
import org.domi.minewatch.api.IAbility
import org.domi.minewatch.core.DamageContext
import org.domi.minewatch.core.DamagePipeline
import org.domi.minewatch.match.isEnemy

/**
 * [속 - Genus] 즉시 적중형(Hitscan) 스킬의 뼈대
 */
abstract class HitscanEngine : IAbility {
    // 하위 종(Specific Ability)에서 설정할 수치들
    abstract val damage: Float
    abstract val maxRange: Double
    abstract val headshotMultiplier: Float

    override var currentCooldown: Float = 0f
    override var currentAmmo: Int = 0

    override fun cast(caster: LivingEntity) {
        val world: World = caster.entityWorld
        if (world.isClient) return // 데미지 판정은 무조건 서버에서만 처리

        val startPos = caster.getCameraPosVec(1.0f)
        val lookVec = caster.getRotationVec(1.0f)
        val endPos = startPos.add(lookVec.x * maxRange, lookVec.y * maxRange, lookVec.z * maxRange)

        // 마인크래프트 바닐라의 Raycast(광선 추적) 유틸리티 사용
        val hitResult = ProjectileUtil.raycast(
            caster, startPos, endPos,
            caster.boundingBox.stretch(lookVec.multiply(maxRange)).expand(1.0),
            { target -> !target.isSpectator && target is LivingEntity && target.isAlive },
            maxRange * maxRange
        )

        // 1. 엔티티(적)에 적중했을 경우
        if (hitResult != null && hitResult.type == HitResult.Type.ENTITY) {
            val targetEntity = (hitResult as EntityHitResult).entity as LivingEntity

            // 🌟 피아식별 로직 연동: 적인지 확인!
            if (targetEntity.isEnemy(caster)) {
                // 헤드샷(치명타) 판정 로직 (y좌표를 기반으로 머리 부근인지 체크)
                val isHeadshot = (hitResult.pos.y - targetEntity.y) > (targetEntity.height * 0.8)
                val finalDamage = if (isHeadshot) damage * headshotMultiplier else damage

                // 🌟 파이프라인에 데미지 연산 위임
                val context = DamageContext(
                    attacker = caster,
                    target = targetEntity,
                    originalDamage = finalDamage,
                    currentDamage = finalDamage,
                    isCritical = isHeadshot
                )

                DamagePipeline.processDamage(context)

                // 타격음 재생, 파티클 생성 로직 추가 (Client Network로 패킷 전송)
            }
        }
        // 2. 블록(벽)에 적중했을 경우
        else {
            // 벽 타격 파티클 생성 로직
        }
    }
}