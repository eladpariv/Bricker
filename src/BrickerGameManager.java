package src;


import src.brick_strategies.CollisionStrategy;
import src.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.*;
import java.util.Random;


public class BrickerGameManager extends GameManager {
    private static final int BORDER_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 120;

    private static final int BRICK_HEIGHT = 10;

    private static final int BRICK_WIDTH = 100;
    private static final int BALL_RADIUS = 20;
    private static final float BALL_SPEED = 200;
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(80, 140, 250));
    private static final int ROWS_NUMBER = 8;
    private static final float GRAPHIC_LIVES_POSITION = 60;
    private static final int NUM_OF_LIVES = 3;
    private static final float LIVES_AND_NUMERIC_COUNTERS_SIZE = 15;
    private static final float DISTANS_FROM_THE_EDGE = 25;
    private static final float BOARD_CENTER = 0.5F;
    private static final float OPOSSITE = -1;
    private static final int NUM_OF_COLS = 7;
    private static final float BOARD_WIDTH = 900;
    private static final float BOARD_HEIGHT = 500;
    private static final int FIRST_BRICK_X_COORDINATE = 50;
    private static final int FIRST_BRICK_Y_COORDINATE = 30;
    private static final float HALF = 0.5F;
    private static final int MIN_DIST_FROM_EDGE = 100;
    private static final String PADDLE_IMG = "assets/paddle.png";
    private static final String BRICK_IMG = "assets/brick.png";
    private static final String BACK_GROUND = "assets/DARK_BG2_small.jpeg";
    private static final String HEART_IMG = "assets/heart.png";
    private static final String WIN_DECLARATION = "You win!";
    private static final String LOSE_DECLERATION = "You Lose!";
    private static final String ASK_FOR_NEXT_GAME = " Play again?";
    private static final String BALL_IMG = "assets/ball.png";
    private static final String BALL_SOUND = "assets/blop_cut_silenced.wav";
    private static final String WINDOW_TITLE = "Bouncing Ball";
    private final Counter brickCounter;
    private final Counter liveCounter;
    private int numOfLives;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private UserInputListener inputListener;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.brickCounter = new danogl.util.Counter();
        this.liveCounter = new danogl.util.Counter(NUM_OF_LIVES);


    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;
        this.inputListener = inputListener;
        
        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();

        //create ball
        createBall(imageReader, soundReader, windowController);

        //create paddles
        Renderable paddleImage = imageReader.readImage(
                PADDLE_IMG, false);
        createUserPaddle(paddleImage, inputListener, windowDimensions);

        //create borders
        createBorders(windowDimensions);
        
        //create bricks
        Renderable brickImage = imageReader.readImage(BRICK_IMG, true);
        createBricks(windowDimensions, brickImage, ROWS_NUMBER);
        
        //createGraphicLivesCounter
        createGraphicLivesCounter(new Vector2(GRAPHIC_LIVES_POSITION,
                        windowDimensions.y() - DISTANS_FROM_THE_EDGE)
                , new Vector2(LIVES_AND_NUMERIC_COUNTERS_SIZE,
                        LIVES_AND_NUMERIC_COUNTERS_SIZE), imageReader);

        //create Numeric counter
        createNumericLiveCounter(new Vector2(DISTANS_FROM_THE_EDGE,
                        windowDimensions.y() - DISTANS_FROM_THE_EDGE),
                new Vector2(LIVES_AND_NUMERIC_COUNTERS_SIZE, LIVES_AND_NUMERIC_COUNTERS_SIZE));
        
        // create background
        createBackGround(imageReader, windowDimensions);

    }

    private void createBackGround(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable renderable = imageReader.readImage(BACK_GROUND, false);
        GameObject background = new GameObject(new Vector2(0, 0), windowDimensions, renderable);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    private void createNumericLiveCounter(Vector2 position, Vector2 widgetDimension) {
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(liveCounter, position,
                widgetDimension, gameObjects());
        gameObjects().addGameObject(numericLifeCounter, Layer.UI);

    }

    private void createGraphicLivesCounter(Vector2 position, Vector2 widgetDimension, ImageReader imageReader) {
        Renderable liveImage = imageReader.readImage(HEART_IMG, true);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(position,
                widgetDimension, liveCounter, liveImage, gameObjects(), NUM_OF_LIVES);
        gameObjects().addGameObject(graphicLifeCounter, Layer.UI);
    }

    private void checkForGameEnd() {

        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if (brickCounter.value() == 0 || this.inputListener.isKeyPressed('W')) {
            //we win
            prompt = WIN_DECLARATION;
        }
        if (ballHeight > windowDimensions.y()) {
            //we lost
            if (liveCounter.value() == 1) {
                prompt = LOSE_DECLERATION;

            } else {
                liveCounter.decrement();

                ball.setCenter(windowDimensions.mult(BOARD_CENTER));
                float ballVelX = BALL_SPEED;
                float ballVelY = BALL_SPEED;
                Random rand = new Random();
                if (rand.nextBoolean())
                    ballVelX *= OPOSSITE;
                if (rand.nextBoolean())
                    ballVelY *= OPOSSITE;
                ball.setVelocity(new Vector2(ballVelX, ballVelY));
            }

        }
        if (!prompt.isEmpty()) {
            prompt += ASK_FOR_NEXT_GAME;
            if (windowController.openYesNoDialog(prompt)) {
                liveCounter.reset();
                liveCounter.increaseBy(NUM_OF_LIVES);
                brickCounter.reset();
                brickCounter.increaseBy(NUM_OF_COLS * ROWS_NUMBER);

                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader,
                            WindowController windowController) {
        Renderable ballImage =
                imageReader.readImage(BALL_IMG, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND);
        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(BOARD_CENTER));
        gameObjects().addGameObject(ball, 0);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= OPOSSITE;
        if (rand.nextBoolean())
            ballVelY *= OPOSSITE;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    private void createUserPaddle(Renderable paddleImage, UserInputListener inputListener,
                                  Vector2 windowDimensions) {
        GameObject userPaddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener, new Vector2(BOARD_WIDTH, BOARD_HEIGHT), MIN_DIST_FROM_EDGE);

        userPaddle.setCenter(
                new Vector2(windowDimensions.x() * HALF, (int) windowDimensions.y() - DISTANS_FROM_THE_EDGE));
        gameObjects().addGameObject(userPaddle);
    }

    private void createBricks(Vector2 windowDimensions, Renderable brickImage, int rows) {
        CollisionStrategy collisionStrategy = new CollisionStrategy(gameObjects());
        int x = FIRST_BRICK_X_COORDINATE;
        int y = FIRST_BRICK_Y_COORDINATE;
        for (int row = 0; row < rows; row++) {
            for (int i = 0; i < windowDimensions.x() / BRICK_WIDTH; i++) {

                GameObject brick = new Brick(new Vector2(x, y), new Vector2(BRICK_WIDTH, BRICK_HEIGHT)
                        , brickImage, collisionStrategy, this.brickCounter);
                brick.setCenter(new Vector2(x, y));
                gameObjects().addGameObject(brick, 0);
                x += BRICK_WIDTH;
            }
            x = FIRST_BRICK_X_COORDINATE;
            y += BRICK_HEIGHT;
        }



    }

    private void createBorders(Vector2 windowDimensions) {
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        BORDER_RENDERABLE)
        );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        BORDER_RENDERABLE)
        );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(0, 0),
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        BORDER_RENDERABLE)
        );
    }

    public static void main(String[] args) {
        new BrickerGameManager(
                WINDOW_TITLE,
                new Vector2(BOARD_WIDTH, BOARD_HEIGHT)).run();
    }
}
