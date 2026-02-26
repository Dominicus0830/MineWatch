package org.domi.minewatch.match.modes

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.match.TeamManager
import org.domi.minewatch.match.OwTeam

/**
 * 모든 게임 모드의 최상위 규격.
 * sealed class로 선언하여, 이 파일 밖에서는 모드를 함부로 추가할 수 없게 통제합니다.
 */
sealed class GameMode {
    abstract val modeName: String

    // 이 모드에서 승리하기 위한 목표 점수 등 공통 설정
    abstract val targetScore: Int

    // 모드 초기화 로직 (시작 시 실행)
    abstract fun onStart()

    // 🌟 모드별 피아식별 핵심 로직
    abstract fun isEnemy(entity1: LivingEntity, entity2: LivingEntity): Boolean
}

// ---------------------------------------------------------
// [구현체 1] 개인전 데스매치: 나 빼고 모두가 적!
// ---------------------------------------------------------
class FreeForAll(override val targetScore: Int = 20) : GameMode() {
    override val modeName = "데스매치"

    override fun onStart() {
        TeamManager.clearAll() // 팀 구분이 필요 없으므로 초기화
    }

    override fun isEnemy(entity1: LivingEntity, entity2: LivingEntity): Boolean {
        // 자기 자신이 아니면 무조건 적
        return entity1 != entity2
    }
}

// ---------------------------------------------------------
// [구현체 2] 팀 데스매치 (화물, 거점 등 모든 팀전의 뼈대)
// ---------------------------------------------------------
class TeamDeathmatch(override val targetScore: Int = 50) : GameMode() {
    override val modeName = "팀 데스매치"

    override fun onStart() {
        // (게임 시작 시 플레이어들을 RED, BLUE로 나누는 로직 호출)
    }

    override fun isEnemy(entity1: LivingEntity, entity2: LivingEntity): Boolean {
        if (entity1 == entity2) return false // 본인은 적이 아님

        val team1 = TeamManager.getTeam(entity1)
        val team2 = TeamManager.getTeam(entity2)

        // 둘 중 하나라도 팀이 없으면 무적/중립 취급 (기획에 따라 변경 가능)
        if (team1 == OwTeam.NONE || team2 == OwTeam.NONE) return false

        // 팀이 다르면 적!
        return team1 != team2
    }
}