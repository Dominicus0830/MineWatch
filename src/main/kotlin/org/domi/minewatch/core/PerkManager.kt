package org.domi.minewatch.core

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.api.IPerk
import java.util.UUID

/**
 * 플레이어(또는 몹)의 특전 장착 상태를 관리하는 싱글톤 매니저
 */
object PerkManager {
    // 엔티티 UUID별 장착 중인 특전 ID 목록
    private val equippedPerks = mutableMapOf<UUID, MutableSet<String>>()

    // 게임 내 존재하는 모든 특전 목록 (레지스트리 역할)
    private val perkRegistry = mutableMapOf<String, IPerk>()

    fun registerPerk(perk: IPerk) {
        perkRegistry[perk.id] = perk
    }

    fun equipPerk(entity: LivingEntity, perkId: String) {
        val perk = perkRegistry[perkId] ?: return
        equippedPerks.getOrPut(entity.uuid) { mutableSetOf() }.add(perkId)
        perk.onEquip(entity) // 장착 효과 발동
    }

    fun unequipPerk(entity: LivingEntity, perkId: String) {
        val perk = perkRegistry[perkId] ?: return
        if (equippedPerks[entity.uuid]?.remove(perkId) == true) {
            perk.onUnequip(entity) // 해제 효과 발동 (원상복구)
        }
    }

    fun hasPerk(entity: LivingEntity, perkId: String): Boolean {
        return equippedPerks[entity.uuid]?.contains(perkId) == true
    }
}