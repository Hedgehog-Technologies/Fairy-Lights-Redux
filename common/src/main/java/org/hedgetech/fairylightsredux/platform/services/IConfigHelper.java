package org.hedgetech.fairylightsredux.platform.services;

public interface IConfigHelper {
    int DEFAULT_JINGLE_AMPLITUDE = 40;
    boolean DEFAULT_CHRISTMAS_JINGLES = true;
    boolean DEFAULT_HALLOWEEN_JINGLES = true;
    String DEFAULT_TUTORIAL_PROGRESS = "none"; // TODO - Make this an enum?

    /**
     * Whether redstone-triggered jingles pick from Christmas songs during Christmas.
     * @return True to use the Christmas sons from December 24 to 26.
     */
    boolean christmasJingles();

    /**
     * Whether redstone-triggered jingles pick from Halloween songs during Halloween.
     * @return True to use the Halloween songs from October 30 to November 1.
     */
    boolean halloweenJingles();

    /**
     * The distance that jingles can be heard, in blocks.
     * @return The jingle amplitude, at least 1.
     */
    int jingleAmplitude();

    /**
     * How far this client is through the hanging lights tutorial. Client only.
     * @return "none", "hanging_lights", or "complete", though a hand-edited config can hold anything.
     */
    String tutorialProgress();

    /**
     * Saves the hanging lights tutorial progress to the client config. Client only.
     * @param progress The new progress.
     */
    void setTutorialProgress(String progress);
}
