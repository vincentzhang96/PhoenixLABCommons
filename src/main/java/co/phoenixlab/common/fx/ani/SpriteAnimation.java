package co.phoenixlab.common.fx.ani;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * SpriteAnimation from http://blog.netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 * with some changes
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

    //  TYPE 4
    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count,
            int width, int height
    ) {
        this(imageView, duration, count, count, width, height);
    }

    //  TYPE 3
    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int width, int height
    ) {
        this(imageView, duration, count, columns, 0, 0, width, height);
    }

    //  TYPE 2
    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int width, int height, int fps
    ) {
        this(imageView, duration, count, columns, 0, 0, width, height, fps);
    }

    //  TYPE 1
    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height) {
        this(imageView, duration, count, columns, offsetX, offsetY, width, height, 15);
    }

    //  TYPE 0
    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height, int fps) {
        super(fps);
        this.imageView = Objects.requireNonNull(imageView);
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(Objects.requireNonNull(duration));
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }

    public static SpriteAnimation fromProperties(Properties p, ImageView imageView) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(imageView);
        Map<String, String> map = new HashMap<>(p.size());
        p.forEach((k, v) -> map.put((String) k, (String) v));
        return fromMap(map, imageView);
    }

    public static SpriteAnimation fromMap(Map<String, String> p, ImageView imageView) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(imageView);
        int type = getInt(p, "type");
        SpriteAnimation ret;
        switch (type) {
            case 0:
                ret = new SpriteAnimation(imageView,
                        Duration.seconds(getDouble(p, "duration")),
                        getInt(p, "count"), getInt(p, "columns"),
                        getInt(p, "offsetX"), getInt(p, "offsetY"),
                        getInt(p, "width"), getInt(p, "height"), getInt(p, "fps"));
                break;
            case 1:
                ret = new SpriteAnimation(imageView,
                        Duration.seconds(getDouble(p, "duration")),
                        getInt(p, "count"), getInt(p, "columns"),
                        getInt(p, "offsetX"), getInt(p, "offsetY"),
                        getInt(p, "width"), getInt(p, "height"));
                break;
            case 2:
                ret = new SpriteAnimation(imageView,
                        Duration.seconds(getDouble(p, "duration")),
                        getInt(p, "count"), getInt(p, "columns"),
                        getInt(p, "width"), getInt(p, "height"), getInt(p, "fps"));
                break;
            case 3:
                ret = new SpriteAnimation(imageView,
                        Duration.seconds(getDouble(p, "duration")),
                        getInt(p, "count"), getInt(p, "columns"),
                        getInt(p, "width"), getInt(p, "height"));
                break;
            case 4:
                ret = new SpriteAnimation(imageView,
                        Duration.seconds(getDouble(p, "duration")),
                        getInt(p, "count"),
                        getInt(p, "width"), getInt(p, "height"));
                break;
            default:
                throw new IllegalArgumentException("No such sprite animation type " + type);
        }
        if (Boolean.parseBoolean(p.get("loop"))) {
            ret.setCycleCount(INDEFINITE);
        }
        return ret;
    }

    private static int getInt(Map<String, String> p, String key) {
        return parseInt(p.get(key));
    }

    private static double getDouble(Map<String, String> p, String key) {
        return parseDouble(p.get(key));
    }
}

