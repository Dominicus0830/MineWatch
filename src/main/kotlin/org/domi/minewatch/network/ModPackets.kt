package org.domi.minewatch.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import org.domi.minewatch.api.IOverwatchActor

// 🌟 1.21+ 전용: 데이터를 담아서 보낼 '페이로드' 데이터 클래스
data class CastAbilityPayload(val slotIndex: Int) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = ID

    companion object {
        // 패킷의 고유 ID
        val ID = CustomPayload.Id<CastAbilityPayload>(Identifier.of("minewatch", "cast_ability"))

        // 코덱(Codec): 데이터(Int)를 네트워크 신호로 압축하고 풀어주는 역할
        val CODEC: PacketCodec<RegistryByteBuf, CastAbilityPayload> = PacketCodec.tuple(
            PacketCodecs.INTEGER, CastAbilityPayload::slotIndex,
            ::CastAbilityPayload
        )
    }
}

object ModPackets {
    fun registerC2SPackets() {
        // 1. 레지스트리에 페이로드 타입 등록 (이걸 안 하면 서버가 패킷을 거부함)
        PayloadTypeRegistry.playC2S().register(CastAbilityPayload.ID, CastAbilityPayload.CODEC)

        // 2. 수신기 등록 (파라미터가 payload와 context 두 개로 깔끔해짐)
        ServerPlayNetworking.registerGlobalReceiver(CastAbilityPayload.ID) { payload, context ->
            val server = context.server()
            val player = context.player()
            val slotIndex = payload.slotIndex

            // 메인 스레드에서 안전하게 실행
            server.execute {
                val actor = player as? IOverwatchActor ?: return@execute
                val loadout = actor.currentLoadout ?: return@execute

                val abilityToCast = when (slotIndex) {
                    0 -> loadout.primary
                    1 -> loadout.secondary
                    2 -> loadout.ability1
                    3 -> loadout.ability2
                    4 -> loadout.ultimate
                    else -> null
                }

                if (abilityToCast != null && abilityToCast.canCast(player)) {
                    abilityToCast.cast(player)
                }
            }
        }
    }
}