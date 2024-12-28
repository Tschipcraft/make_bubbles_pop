package net.tschipcraft.make_bubbles_pop;

public final class MakeBubblesPop {

    public static final String MOD_ID = "make_bubbles_pop";

    public static final boolean MIDNIGHTLIB_INSTALLED = false;

    public static void init() {
        // Write common init code here.
    }

    public static double getConfigInitialVelocity(double original) {
        return (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY) ? original : 0D;
    }

}
