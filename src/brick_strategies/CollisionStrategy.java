package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class CollisionStrategy {
    private GameObjectCollection gameObject;

    public CollisionStrategy(GameObjectCollection gameObject) {
        this.gameObject = gameObject;
    }

    public void onCollision(GameObject collided, GameObject collider, Counter counter) {
        gameObject.removeGameObject(collided);
        counter.decrement();


    }

}
