package org.domi.minewatch.core

import net.minecraft.server.network.ServerPlayerEntity
import org.domi.minewatch.api.IOverwatchActor
import org.domi.minewatch.content.heroes.Tracer

/**
 * 플레이어에게 영웅을 장착시키고 관리하는 매니저
 */
object HeroManager {

    // (임시) 모든 영웅을 모아두는 레지스트리
    val availableHeroes = mapOf(
        "tracer" to Tracer()
        // "reinhardt" to Reinhardt()
    )

    /**
     * 플레이어에게 특정 영웅을 장착시킵니다.
     */
    fun selectHero(player: ServerPlayerEntity, heroId: String) {
        val hero = availableHeroes[heroId] ?: return

        // 1. 플레이어를 오버워치 액터로 형변환 (1단계에서 만든 Mixin 덕분에 가능!)
        val actor = player as IOverwatchActor

        // 2. 스탯 덮어쓰기 (체력 150 등)
        actor.applyHeroStats(hero.stats)

        // 3. 역할군(Role) 및 스킬(Loadout) 주입
        // (IOverwatchActor 인터페이스에 var currentHero: Tracer? 같은 변수를 추가하여 저장해둡니다)
        actor.currentRole = hero
        actor.currentLoadout = hero.loadout

        // 4. 마인크래프트 바닐라 체력 완전 회복 및 동기화
        player.health = player.maxHealth

        println("${player.name.string}님이 ${hero.roleName} 영웅을 선택했습니다!")
    }
}