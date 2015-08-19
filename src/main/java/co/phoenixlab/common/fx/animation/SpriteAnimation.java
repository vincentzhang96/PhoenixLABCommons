package co.phoenixlab.common.fx.animation;


import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * SpriteAnimation from http://blog.netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 */

public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
    private int lastIndex;

    /**
     * Creates a SpriteAnimation with the given parameters using an FPS calculated from the number of frames divided
     * by the duration of the animation and no offset from the top or left edges.
     *
     * @param imageView The ImageView containing the image to be used as a spritesheet and to display the animation
     * @param duration  The amount of time the animation should last for
     * @param count     The number of frames in the animation
     * @param columns   The number of columns in the spritesheet
     * @param width     The width of a single frame
     * @param height    The height of a single frame
     * @see #SpriteAnimation(ImageView, Duration, int, int, int, int, int, int, int)
     */
    public SpriteAnimation(ImageView imageView, Duration duration,
                           int count, int columns,
                           int width, int height) {
        this(imageView, duration, count, columns, 0, 0, width, height, (int) (count / duration.toSeconds()));
    }

    /**
     * Creates a SpriteAnimation with the given parameters using an FPS calculated from the number of frames divided
     * by the duration of the animation.
     *
     * @param imageView The ImageView containing the image to be used as a spritesheet and to display the animation
     * @param duration  The amount of time the animation should last for
     * @param count     The number of frames in the animation
     * @param columns   The number of columns in the spritesheet
     * @param offsetX   The offset from the left edge of the spritesheet for the first frame in a row
     * @param offsetY   The offset from the top edge of the spritesheet for the first row of frames
     * @param width     The width of a single frame
     * @param height    The height of a single frame
     * @see #SpriteAnimation(ImageView, Duration, int, int, int, int, int, int, int)
     */
    public SpriteAnimation(ImageView imageView, Duration duration,
                           int count, int columns,
                           int offsetX, int offsetY,
                           int width, int height) {
        this(imageView, duration, count, columns, offsetX, offsetY, width, height, (int) (count / duration.toSeconds()));
    }

    /**
     * Creates a SpriteAnimation with the given parameters and no offset from the top or left edges.
     *
     * @param imageView The ImageView containing the image to be used as a spritesheet and to display the animation
     * @param duration  The amount of time the animation should last for
     * @param count     The number of frames in the animation
     * @param columns   The number of columns in the spritesheet
     * @param width     The width of a single frame
     * @param height    The height of a single frame
     * @param fps       The framerate at which this animation should render at, which may be different from
     *                  {@code count/duration}. This value does not slow down or speed up the animation, but instead
     *                  affects how often new frames are drawn.
     * @see #SpriteAnimation(ImageView, Duration, int, int, int, int, int, int, int)
     */
    public SpriteAnimation(ImageView imageView, Duration duration,
                           int count, int columns,
                           int width, int height, int fps) {
        this(imageView, duration, count, columns, 0, 0, width, height, fps);
    }

    /**
     * Creates a SpriteAnimation with the given parameters.
     *
     * @param imageView The ImageView containing the image to be used as a spritesheet and to display the animation
     * @param duration  The amount of time the animation should last for
     * @param count     The number of frames in the animation
     * @param columns   The number of columns in the spritesheet
     * @param offsetX   The offset from the left edge of the spritesheet for the first frame in a row
     * @param offsetY   The offset from the top edge of the spritesheet for the first row of frames
     * @param width     The width of a single frame
     * @param height    The height of a single frame
     * @param fps       The framerate at which this animation should render at, which may be different from
     *                  {@code count/duration}. This value does not slow down or speed up the animation, but instead
     *                  affects how often new frames are drawn.
     */
    public SpriteAnimation(ImageView imageView, Duration duration,
                           int count, int columns,
                           int offsetX, int offsetY,
                           int width, int height, int fps) {
        super(fps);
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }
}

