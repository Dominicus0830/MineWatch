package org.domi.minewatch.core

import kotlin.reflect.KClass

// 이벤트 마커 인터페이스
interface OverwatchEvent

// 데미지 발생 직전/직후 이벤트 예시
data class PreDamageEvent(val context: DamageContext) : OverwatchEvent
data class PostDamageEvent(val context: DamageContext) : OverwatchEvent

object EventBus {
    // 이벤트 타입별로 리스너(콜백 함수)들을 모아두는 맵
    private val listeners = mutableMapOf<KClass<out OverwatchEvent>, MutableList<(OverwatchEvent) -> Unit>>()

    /**
     * 특정 이벤트에 리스너를 등록합니다.
     * 예: EventBus.subscribe(PreDamageEvent::class) { event -> ... }
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : OverwatchEvent> subscribe(eventType: KClass<T>, listener: (T) -> Unit) {
        listeners.getOrPut(eventType) { mutableListOf() }.add(listener as (OverwatchEvent) -> Unit)
    }

    /**
     * 이벤트를 파이프라인에 쏘아 올립니다.
     * 예: EventBus.post(PreDamageEvent(context))
     */
    fun post(event: OverwatchEvent) {
        listeners[event::class]?.forEach { listener ->
            listener.invoke(event)
        }
    }
}