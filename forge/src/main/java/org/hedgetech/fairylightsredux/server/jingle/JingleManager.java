package org.hedgetech.fairylightsredux.server.jingle;

import java.util.HashMap;

public final class JingleManager {
    public static final JingleManager INSTANCE = new JingleManager();
    private final java.util.Map<String, Jingle> libs = new HashMap<>();

    public JingleManager() {
        libs.put(JingleLibrary.RANDOM, new Jingle(4));
        libs.put(JingleLibrary.CHRISTMAS, new Jingle(6));
        libs.put(JingleLibrary.HALLOWEEN, new Jingle(5));
    }

    public Jingle get(String lib) { return libs.get(lib); }
}

