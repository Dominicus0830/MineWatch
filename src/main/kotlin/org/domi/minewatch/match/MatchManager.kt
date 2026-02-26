package org.domi.minewatch.match

import org.domi.minewatch.match.modes.GameMode
import org.domi.minewatch.match.modes.FreeForAll

enum class MatchState {
    WAITING, IN_PROGRESS, ENDED
}

/**
 * 방 전체의 상태를 통제하는 최상위 매니저
 */
object MatchManager {
    var currentState: MatchState = MatchState.WAITING
        private set

    // 기본 모드는 데스매치로 설정
    var currentGameMode: GameMode = FreeForAll()
        private set

    /**
     * 특정 게임 모드로 매치를 시작합니다.
     */
    fun startMatch(mode: GameMode) {
        if (currentState == MatchState.IN_PROGRESS) {
            println("이미 게임이 진행 중입니다!")
            return
        }

        currentGameMode = mode
        currentState = MatchState.IN_PROGRESS

        // 해당 모드의 초기화 로직 실행
        currentGameMode.onStart()

        println("${currentGameMode.modeName} 매치가 시작되었습니다!")
    }

    fun endMatch() {
        currentState = MatchState.ENDED
        TeamManager.clearAll()
        println("매치가 종료되었습니다.")
    }
}