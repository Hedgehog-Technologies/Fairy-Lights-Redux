package org.hedgetech.fairylightsredux.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class SharedCodecs {
    public static final Codec<Vec3> VEC3_CODEC = Codec.DOUBLE.listOf().comapFlatMap(list -> {
        if (list.size() != 3) {
            return DataResult.error(() -> "Vec3 must have exactly 3 elements");
        }
        return DataResult.success(new Vec3(list.get(0) ,list.get(1), list.get(2)));
    }, vec -> List.of(vec.x, vec.y, vec.z));

    private SharedCodecs() {}
}
