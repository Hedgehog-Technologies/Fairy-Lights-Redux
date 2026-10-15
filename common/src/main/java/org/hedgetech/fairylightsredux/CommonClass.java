package org.hedgetech.fairylightsredux;

import org.hedgetech.fairylightsredux.platform.Services;

public class CommonClass {

    public static void init() {
        Constants.LOG.info("Loading {} on {} in a {} environment", Constants.MOD_NAME, Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
    }
}
