package org.domi.minewatch.api

import net.minecraft.entity.LivingEntity

/**
 * 모든 특전(Perk) 모듈이 상속받아야 할 인터페이스
 */
interface IPerk {
    val id: String       // 시스템 내부 식별자 (예: "glass_cannon")
    val name: String     // 인게임 표시 이름
    val description: String

    // 특전을 장착할 때 1회 실행 (스탯 증감 등에 사용)
    fun onEquip(entity: LivingEntity) {}

    // 특전을 해제할 때 1회 실행 (스탯 원상복구 등에 사용)
    fun onUnequip(entity: LivingEntity) {}
}