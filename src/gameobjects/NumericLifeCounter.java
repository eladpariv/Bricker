package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {
    private final GameObjectCollection gameObjectCollection;
    private final danogl.util.Counter livesCounter;
    private final Vector2 dimensions;
    private final Vector2 topLeftCorner;
    private TextRenderable textRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public NumericLifeCounter(danogl.util.Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.gameObjectCollection = gameObjectCollection;
        this.livesCounter = livesCounter;
        this.dimensions = dimensions;
        this.topLeftCorner = topLeftCorner;
        this.textRenderable = new TextRenderable("3");
        this.textRenderable.setColor(Color.green);
        renderer().setRenderable(textRenderable);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        switch (livesCounter.value()) {
            case (2):
                this.textRenderable = new TextRenderable("2");
                this.textRenderable.setColor(Color.yellow);
                renderer().setRenderable(textRenderable);
                break;
            case (1):
                this.textRenderable = new TextRenderable("1");
                this.textRenderable.setColor(Color.red);
                renderer().setRenderable(textRenderable);
                break;

        }

    }
}
