package org.domi.minewatch.mixin

import net.minecraft.entity.LivingEntity
import org.domi.minewatch.api.IOverwatchActor
import org.domi.minewatch.content.heroes.AbilityLoadout
import org.domi.minewatch.content.roles.OwRole
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Unique

@Mixin(LivingEntity::class)
abstract class LivingEntityMixin : IOverwatchActor {

    // 코틀린 프로퍼티 오버라이드 + Mixin 고유(Unique) 필드 선언
    @Unique
    override var owHealth: Float = 100f

    @Unique
    override var owMaxHealth: Float = 100f

    @Unique
    override var owArmor: Float = 0f

    @Unique
    override var owMaxArmor: Float = 0f

    @Unique
    override var owShield: Float = 0f

    @Unique
    override var owMaxShield: Float = 0f

    @Unique
    override var currentRole: OwRole? = null

    @Unique
    override var currentLoadout: AbilityLoadout? = null
}