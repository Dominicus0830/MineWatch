package org.domi.minewatch.match

import net.minecraft.entity.LivingEntity
import java.util.UUID

// 확장성을 고려한 팀 식별자 (필요 시 계속 추가 가능)
enum class OwTeam {
    NONE, RED, BLUE, GREEN, YELLOW
}

/**
 * 엔티티들의 소속 팀을 관리하고 피아식별을 담당하는 싱글톤
 */
object TeamManager {
    // 엔티티 UUID를 기반으로 팀을 저장하는 맵
    private val entityTeams = mutableMapOf<UUID, OwTeam>()

    fun setTeam(entity: LivingEntity, team: OwTeam) {
        entityTeams[entity.uuid] = team
    }

    fun getTeam(entity: LivingEntity): OwTeam {
        return entityTeams[entity.uuid] ?: OwTeam.NONE
    }

    fun removeEntity(entity: LivingEntity) {
        entityTeams.remove(entity.uuid)
    }

    fun clearAll() {
        entityTeams.clear()
    }
}

// 🌟 [개발 편의성 극대화] 코틀린 확장 함수(Extension Function)
// 앞으로 모든 스킬 코드에서는 target.isEnemy(caster) 한 줄로 끝납니다.
fun LivingEntity.isEnemy(other: LivingEntity): Boolean {
    return MatchManager.currentGameMode.isEnemy(this, other)
}

fun LivingEntity.isAlly(other: LivingEntity): Boolean {
    return !this.isEnemy(other) && this != other
}