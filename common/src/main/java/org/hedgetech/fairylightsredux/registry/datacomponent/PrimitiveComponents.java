package org.hedgetech.fairylightsredux.registry.datacomponent;

import com.mojang.serialization.Codec;
import org.checkerframework.common.value.qual.EnsuresMinLenIf;

import java.util.ArrayList;
import java.util.List;

public final class PrimitiveComponents {
    public static final Codec<float[]> FLOAT_ARRAY_CODEC = Codec.FLOAT.listOf()
            .xmap(
                    list -> {
                        float[] arr = new float[list.size()];
                        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
                        return arr;
                    },
                    arr -> {
                        List<Float> list = new ArrayList<>(arr.length);
                        for (float f : arr) list.add(f);
                        return list;
                    }
            );


    private PrimitiveComponents() {}
}
