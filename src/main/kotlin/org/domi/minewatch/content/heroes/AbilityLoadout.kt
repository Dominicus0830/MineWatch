package org.domi.minewatch.content.heroes

import org.domi.minewatch.api.IAbility

/**
 * 영웅의 스킬 슬롯을 관리하는 데이터 컨테이너
 */
class AbilityLoadout {
    var primary: IAbility? = null   // 좌클릭 (기본 무기)
    var secondary: IAbility? = null // 우클릭 (보조 무기)
    var ability1: IAbility? = null  // Shift (스킬 1)
    var ability2: IAbility? = null  // E (스킬 2)
    var ultimate: IAbility? = null  // Q (궁극기)

    // 장착된 모든 스킬을 리스트로 반환 (쿨타임 업데이트 등 일괄 처리를 위함)
    fun getAllAbilities(): List<IAbility> {
        return listOfNotNull(primary, secondary, ability1, ability2, ultimate)
    }
}