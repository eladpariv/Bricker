package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    private static final int LIVES_NUM = 3;
    private GameObject[] gameLives = new GameObject[LIVES_NUM];

    private Counter counter;
    private GameObjectCollection gameObjectColection;
    private int numOfLives;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public GraphicLifeCounter(danogl.util.Vector2 widgetTopLeftCorner,
                              danogl.util.Vector2 widgetDimensions,
                              danogl.util.Counter livesCounter,
                              danogl.gui.rendering.Renderable widgetRenderable,
                              danogl.collisions.GameObjectCollection gameObjectsCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, null);
        this.counter = livesCounter;
        this.gameObjectColection = gameObjectsCollection;
        this.numOfLives = numOfLives;
//        counter.increaseBy(3);
        Vector2 position = widgetTopLeftCorner;
        for (int i = 0; i < numOfLives; i++) {
            GameObject heart = new GameObject(position, widgetDimensions, widgetRenderable);
            gameObjectColection.addGameObject(heart, Layer.UI);
            gameLives[i] = heart;
            position = new Vector2(position.x() + widgetDimensions.x(), position.y());

        }


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        System.out.println(counter.value());

        if (numOfLives > counter.value()) {
            numOfLives -= 1;
            gameObjectColection.removeGameObject(gameLives[counter.value()], Layer.UI);


//            this.setDimensions(Vector2.ZERO);
//            gameObjectColection.removeGameObject(this);

        }

    }

}