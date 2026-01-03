package org.hedgetech.fairylightsredux.server.connection;

public interface HangingLightsConnection {
    double getJingleProgress();
    Object getFastener();
    void play(Object jingle, int offset);
    Object[] getFeatures();
}

