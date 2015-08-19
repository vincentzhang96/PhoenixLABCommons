package co.phoenixlab.common.fx.animation.transition;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * A collection of methods that are useful when working with or needing {@link FadeTransition}.
 */
public class FadeTransitionHelper {

    private FadeTransitionHelper() {
    }

    /**
     * Fades a node out
     *
     * @param duration The time the animation should last
     * @param node     The node to fade out
     * @return A FadeTransition that will fade out the given node when played.
     */
    public static FadeTransition fadeOut(Duration duration, Node node) {
        return fadeOut(duration, node, null);
    }

    /**
     * Fades a node out and runs the given task when complete
     *
     * @param duration   The time the animation should last
     * @param node       The node to fade out
     * @param onFinished The task to run when the animation is complete
     * @return A FadeTransition that will fade out the given node when played.
     */
    public static FadeTransition fadeOut(Duration duration, Node node, Runnable onFinished) {
        FadeTransition fadeTransition = new FadeTransition(duration, node);
        fadeTransition.setInterpolator(Interpolator.EASE_IN);
        fadeTransition.setFromValue(1D);
        fadeTransition.setToValue(0D);
        if (onFinished != null) {
            fadeTransition.setOnFinished(e -> onFinished.run());
        }
        return fadeTransition;
    }

    /**
     * Fades a node in
     *
     * @param duration The time the animation should last
     * @param node     The node to fade in
     * @return A FadeTransition that will fade in the given node when played.
     */
    public static FadeTransition fadeIn(Duration duration, Node node) {
        return fadeIn(duration, node, null);
    }

    /**
     * Fades a node in and runs the given task when complete
     *
     * @param duration   The time the animation should last
     * @param node       The node to fade in
     * @param onFinished The task to run when the animation is complete
     * @return A FadeTransition that will fade in the given node when played.
     */
    public static FadeTransition fadeIn(Duration duration, Node node, Runnable onFinished) {
        FadeTransition fadeTransition = new FadeTransition(duration, node);
        fadeTransition.setInterpolator(Interpolator.EASE_OUT);
        fadeTransition.setFromValue(0D);
        fadeTransition.setToValue(1D);
        if (onFinished != null) {
            fadeTransition.setOnFinished(e -> onFinished.run());
        }
        return fadeTransition;
    }

}
