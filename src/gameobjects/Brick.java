package src.gameobjects;

import src.brick_strategies.CollisionStrategy;

import danogl.GameObject;
import danogl.collisions.Collision;

public class Brick extends GameObject {
    private danogl.util.Counter counter;
    private CollisionStrategy strategy;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */

    public Brick(danogl.util.Vector2 topLeftCorner,
                 danogl.util.Vector2 dimensions,
                 danogl.gui.rendering.Renderable renderable,
                 CollisionStrategy strategy,
                 danogl.util.Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.strategy = strategy;
        this.counter = counter;
        counter.increment();

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        strategy.onCollision(this, other, counter);


    }
}
