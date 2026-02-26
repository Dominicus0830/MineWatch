package org.domi.minewatch.data_registry

// 영웅의 기본 스탯을 정의하는 데이터 클래스
data class HeroStats(
    val maxHealth: Float,
    val maxArmor: Float,
    val maxShield: Float,
    val moveSpeedMultiplier: Float // 마인크래프트 기본 이속 대비 배율
)

/**
 * 모든 영웅의 체력/방어력/보호막/이속 밸런스를 관리하는 싱글톤 객체
 */
object HeroBalance {
    // 트레이서: 체력 150, 빠름
    val TRACER = HeroStats(
        maxHealth = 150f,
        maxArmor = 0f,
        maxShield = 0f,
        moveSpeedMultiplier = 1.2f
    )

    // 라인하르트: 체력 300, 방어력 250, 느림
    val REINHARDT = HeroStats(
        maxHealth = 300f,
        maxArmor = 250f,
        maxShield = 0f,
        moveSpeedMultiplier = 0.9f
    )

    // 자리야: 체력 225, 보호막 225 (추가 예시)
    val ZARYA = HeroStats(
        maxHealth = 225f,
        maxArmor = 0f,
        maxShield = 225f,
        moveSpeedMultiplier = 1.0f
    )
}