package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import org.hedgetech.fairylightsredux.server.feature.light.IStandardLightBehavior;
import org.hedgetech.fairylightsredux.server.feature.light.Light;

public class ColorLightModel extends LightModel<IStandardLightBehavior> {
    public ColorLightModel(final ModelPart root) {
        super(root);
    }

    @Override
    public void animate(final Light<?> light, final IStandardLightBehavior behavior, final float delta) {
        super.animate(light, behavior, delta);
        this.brightness = behavior.getBrightness(delta);
        this.red = behavior.getRed(delta);
        this.green = behavior.getGreen(delta);
        this.blue = behavior.getBlue(delta);
    }
}
