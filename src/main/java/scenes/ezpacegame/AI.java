package scenes.ezpacegame;

import ecs.Dynamics;
import ecs.GameObject;
import org.joml.Vector2f;
import physics.force.Force;

/**
 * Class used for generic AI functions like move to waypoint and such
 * @author GoldSpark
 */
public class AI {
    MoveToForce moveToForce = new MoveToForce();
    Vector2f destination = new Vector2f();
    Vector2f objectsPosition = new Vector2f();




    public void moveTo(GameObject gameObject, float speed, Vector2f waypoint)
    {
        objectsPosition.x = gameObject.getPositionData()[0];
        objectsPosition.y = gameObject.getPositionData()[1];


        waypoint.sub(objectsPosition, destination);
        destination.normalize();

        moveToForce.gameObject = gameObject;
        moveToForce.speed = speed;
        moveToForce.waypoint = waypoint;



        gameObject.getComponent(Dynamics.class).applyForce(moveToForce);



    }


    public class MoveToForce implements Force
    {
        public Vector2f waypoint;
        public float speed;
        public GameObject gameObject;
        private final Vector2f direction = new Vector2f(0, 0);
        private boolean isMoving = true;


        public MoveToForce()
        {

        }

        public MoveToForce(GameObject gameObject, Vector2f waypoint, float speed)
        {
            this.waypoint = waypoint;
            this.speed = speed;
            this.gameObject = gameObject;
        }


        @Override
        public String identifier() {
            return "MoveTo";
        }

        @Override
        public boolean update(float dt) {
            direction.set(0, 0);
            if(isMoving) {
                direction.add(destination.x * speed * dt, destination.y * speed * dt);

                float xPos = waypoint.x - gameObject.getPositionData()[0];
                float yPos = waypoint.y - gameObject.getPositionData()[1];

                if (xPos < 2 && yPos < 2) {
                    isMoving = false;
                }

                return true;
            }

            return false;
        }

        @Override
        public Vector2f direction() {
            return direction;
        }
    }





    /**
     * Treba svemirski brod kad se spawnuje da se polako krece u desno dok ne stigne do ivice ekrana
     * i kad puca treba nekad da puca u igraca a nekad ispod sebe direktno
     *
     * Pomjeranje:
     * -Spawn space ship
     * -Move to position given
     * -Ako je vece ili jednako od pozicije odmah ga zaustavi i postavi mu poziciju na tacnu datu
     *
     *
     *
     *
     *
     */







}
