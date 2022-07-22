package scenes.ezpacegame;

import audio.AudioListener;
import audio.AudioSource;
import ecs.*;
import graphics.Camera;
import graphics.Color;
import graphics.Spritesheet;
import graphics.Window;
import input.Mouse;
import org.joml.Math;
import org.joml.Vector2f;
import physics.collision.CollisionInformation;
import physics.force.Force;
import scene.Scene;
import ui.Text;
import ui.fonts.Font;
import util.Assets;
import util.Engine;
import util.MathUtils;
import util.OrderPreservingList;
import util.worldhelper.QuickCreate;

import static graphics.Graphics.setDefaultBackground;

public class EzAsteroidsDemo extends Scene {

    private final int MAX_ENEMIES = 12;

    private float summonTimer = 0.5f;
    private int enemiesSpawned = 1;
    private boolean wave1 = true;
    private boolean wave2 = false;



    public Spritesheet shipSprites;
    public Spritesheet enemyShip1Sprites, enemyShip2Sprites;
    public Spritesheet projectileSprites;
    public SpriteRenderer shipRenderer;

    public GameObject ship;
    public GameObject enemyShip;
    public GameObject background;


    public OrderPreservingList<Projectile> projectiles = new OrderPreservingList<>();
    public OrderPreservingList<EnemyShip> enemies = new OrderPreservingList<>();


    private boolean spawnProjectile = true;
    private float rateOfFire = 0.2f;
    private float time = 0.0f;
    private float rotationSpeed = 2f;
    private Vector2f lookDirection = new Vector2f();


    Font openSans;
    Text scoreText;
    Text hudText;

    public static void main(String[] args)
    {
        Engine.init(1280, 720, "Azurite Defense", 0.9f, true);
        Engine.scenes().switchScene(new EzAsteroidsDemo(), true);
        Engine.window().setIcon("src/assets/images/icon.png");
        Engine.showWindow();
    }

    @Override
    public void awake() {
        camera = new Camera();
        setDefaultBackground(0);


        //Game objects and textures
        shipSprites = new Spritesheet(Assets.getTexture("src/assets/images/ezSpaceGame/fighter_fury.png"), 32, 32, 4, 0);
        projectileSprites = new Spritesheet(Assets.getTexture("src/assets/images/ezSpaceGame/casablanca_mk1.png"), 8, 8, 4, 0);
        enemyShip1Sprites = new Spritesheet(Assets.getTexture("src/assets/images/ezSpaceGame/dragonfly.png"), 16, 16, 4, 0);
        enemyShip2Sprites = new Spritesheet(Assets.getTexture("src/assets/images/ezSpaceGame/redred.png"), 16, 16, 9, 0);

        openSans = new Font("src/assets/fonts/OpenSans-Regular.ttf", 20, true);
        scoreText = new Text("SCORE: 0", openSans, Color.WHITE, 0, 5, 1, true, false);
        hudText = new Text("", openSans, Color.GREEN, 0, 0, 1, false, true);



        ship = QuickCreate.CreateAnimatedGameObjectC("Player", shipSprites,
                new Vector2f(Window.getWidth() * 0.5f, Window.getHeight() * 0.5f),
                new Vector2f(100), "on", 0, 3, 1);
        ship.addComponent(CharacterController.standardTopDown(ship.getComponent(Dynamics.class), 1));


        background = new GameObject("Background", new Vector2f(Window.getWidth() * 0.5f, Window.getHeight() * 0.5f), 0);
        SpriteRenderer backgroundRenderer = new SpriteRenderer("src/assets/images/ezSpaceGame/galaxy.jpg", new Vector2f(graphics.Window.getWidth(), graphics.Window.getHeight()));
        background.addComponent(backgroundRenderer);


        enemyShip = QuickCreate.CreateAnimatedGameObjectC("Enemy", enemyShip1Sprites,
                new Vector2f(0, 100f),
                new Vector2f(50), "on", 0, 3, 1);

        EnemyShip enemyShip1 = new EnemyShip(enemyShip, new Vector2f(Window.getWidth() - 100f, 100f));

        enemies.add(enemyShip1);
    }

    public void update(){

        super.update();

        if(Mouse.mouseButtonDown(0))
        {
            SpawnProjectiles();
        }

        rotateShip();

        if(wave1)
        {

            if(enemiesSpawned >= MAX_ENEMIES)
            {
                wave1 = false;
                wave2 = true;
            }

            summonTimer -= Engine.deltaTime();

            if(summonTimer < 0)
            {
                summonTimer = 0.5f;
                enemiesSpawned++;
                enemies.add(new EnemyShip(QuickCreate.CreateAnimatedGameObjectC("Enemy", enemyShip1Sprites,
                        new Vector2f(0, 100f),
                        new Vector2f(50), "on", 0, 3, 1), new Vector2f(Window.getWidth() - enemiesSpawned * 100f, 100f)));
            }
        }



        UpdateProjectiles();
    }


    private void UpdateProjectiles()
    {
        for(Projectile projectile : projectiles)
        {

            CollisionInformation collisionInformation = projectile.gameObject.getComponent(PolygonCollider.class).detectCollision(enemyShip.getComponent(PolygonCollider.class));

            if(collisionInformation.collision())
            {
                showGoodText(projectile.gameObject.getPositionData()[0], projectile.gameObject.getPositionData()[1]);
                this.removeGameObjectFromScene(projectile.gameObject);
                this.removeGameObjectFromScene(enemyShip);
                break;
            }


            //Check whether the projectile has exited the screen bounds or hit another ship
            if(projectile.gameObject.getPositionData()[1] > Window.getHeight() || projectile.gameObject.getPositionData()[1] < 0
                    || projectile.gameObject.getPositionData()[0] > Window.getWidth() || projectile.gameObject.getPositionData()[0] < 0)
            {
                this.removeGameObjectFromScene(projectile.gameObject);
                break;
            }

        }

       // if(shouldRemove && projectiles.size() > 0)
       //     projectiles.remove(removeAt);

        //Check whether should we shoot now
        time += Engine.deltaTime();
        if(time > rateOfFire)
        {
            time = 0;
            spawnProjectile = true;
        }
    }

    private GameObject createFriendlyProjectile(Vector2f position)
    {
        float angle = (float) -(Math.atan2(lookDirection.x, -lookDirection.y) * MathUtils.rad2Deg());


        GameObject go = QuickCreate.CreateAnimatedGameObjectC("Projectile", projectileSprites, position, angle, new Vector2f(5, 10), "flying", 0, 3, 1);
        PointLight pointLight = new PointLight(Color.GREEN, 0.3f);
        go.addComponent(pointLight);



        return go;
    }

    private void SpawnProjectiles()
    {
        if(spawnProjectile) {

            spawnProjectile = false;

            Vector2f direction = new Vector2f();
            direction.x = lookDirection.x;
            direction.y = lookDirection.y;
            direction.normalize();

            //spawn projectile at the center of the ship
            Projectile projectile = new Projectile(createFriendlyProjectile(new Vector2f(ship.getPositionData()[0] , ship.getPositionData()[1])), direction, 200f);

            projectiles.add(projectile);
        }



    }


    private void rotateShip()
    {
        lookDirection.x = Mouse.getWorldX() - ship.getPositionData()[0];
        lookDirection.y = Mouse.getWorldY() - ship.getPositionData()[1];

        float angle = (float) (Math.atan2(lookDirection.x, -lookDirection.y) * MathUtils.rad2Deg());


        ship.getComponent(SpriteRenderer.class).rotation = -angle;
    }

    private void showGoodText(float posX, float posY)
    {
        hudText.change("GOOD!");
        hudText.setPosition(posX, posY);
    }


    public static class Projectile implements Comparable<Projectile>{

        public GameObject gameObject;
        public Vector2f direction;
        public float moveSpeed = 20f;

        public Projectile(GameObject go, Vector2f direction, float moveSpeed)
        {
            this.gameObject = go;
            this.direction = new Vector2f(direction.x, direction.y);
            this.moveSpeed = moveSpeed;

            Force f = new Force() {
                private final Vector2f dir = new Vector2f(0, 0);

                @Override
                public String identifier() {
                    return "InsaneProjectileMovementOMG";
                }

                @Override
                public boolean update(float dt) {
                    dir.set(0, 0);

                    dir.add(new Vector2f((direction.x * moveSpeed) * dt, (direction.y * moveSpeed) * dt));

                    return true;
                }

                @Override
                public Vector2f direction() {
                    return dir;
                }
            };
            gameObject.getComponent(Dynamics.class).applyForce(f);
        }



        @Override
        public int compareTo(Projectile o) {
            return gameObject.compareTo(o.gameObject);
        }

    }


    public static class EnemyShip implements Comparable<EnemyShip>{

        public GameObject gameObject;
        public AI ai;

        public EnemyShip(GameObject gameObject, Vector2f waypoint)
        {
            ai = new AI();
            this.gameObject = gameObject;
            ai.moveTo(gameObject, 150, waypoint);
        }

        public void update()
        {



        }


        @Override
        public int compareTo(EnemyShip o) {
            return gameObject.compareTo(o.gameObject);
        }
    }


    /**
     * Trebaju svemirski brodovi da se spawnuju i svaki svemirski brod puca po 1 projektil
     * koji ide dolje.
     *
     * -Spawn 1 space ship
     * -Move it to the left side of the screen
     * -Spawn 2 space ship
     * -Move it to the left side of 1 space ship
     *
     * -Three seconds passes:
     * -Shoot projectile (each space ship)
     *
     * -Napravi listu
     * -Prodje 2 sekunde dodaj spaceship novi (MAX 10)
     * -Spaceship ide u desno skroz ako je lista manja od 2
     * -Ako je lista veca od 2 spaceship ide lijevo od spaceshipa ispod liste
     *
     */
}
