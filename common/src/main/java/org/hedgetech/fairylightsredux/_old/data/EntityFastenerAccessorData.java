package org.hedgetech.fairylightsredux._old.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

import static org.hedgetech.fairylightsredux._old.data.SharedCodecs.VEC3_CODEC;

public record EntityFastenerAccessorData(UUID uuid, @Nullable Vec3 pos) {
    public static final Codec<EntityFastenerAccessorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("UUID").forGetter(EntityFastenerAccessorData::uuid),
            VEC3_CODEC.optionalFieldOf("Pos").forGetter(data -> Optional.ofNullable(data.pos()))
    ).apply(instance, (uuid, pos) -> new EntityFastenerAccessorData(uuid, pos.orElse(null))));

    public EntityFastenerAccessorData withPos(@Nullable Vec3 newPos) {
        return new EntityFastenerAccessorData(this.uuid, newPos);
    }

    public EntityFastenerAccessorData withoutPos() {
        return new EntityFastenerAccessorData(this.uuid, null);
    }

    public boolean hasPos() {
        return this.pos != null;
    }
}
