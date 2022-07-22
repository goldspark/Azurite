package util.worldhelper;


import ecs.*;
import graphics.Color;
import graphics.Spritesheet;
import graphics.Window;
import org.joml.Vector2f;
import physics.collision.Shapes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Adds bunch of functions which allows shortcuts like creating gameobjects
 * with images and collider components without having to create each components by yourself
 * !!IMPORTANT!! C at the end of every function mean it gets created with collider
 * @author GoldSpark
 */
public class QuickCreate {

    private static Vector2f midScreen = new Vector2f();

    /**
     * Creates animated game object
     *
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param position position of this gameObject in the world
     * @param angle Angle of rotation
     * @param size width and height
     * @param animName name of the animation
     * @param start index in sprite sheet of starting sprite
     * @param end index in sprite sheet of ending sprite
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObjectC(String name, Spritesheet spritesheet, Vector2f position, float angle, Vector2f size, String animName, int start, int end, int zIndex)
    {
        GameObject gameObject = new GameObject(name, position, zIndex);

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start), size);
        spriteRenderer.rotation = angle;
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);
        PolygonCollider body = new PolygonCollider(Shapes.axisAlignedRectangle(-(size.x) * 0.5f, -(size.y) * 0.5f, size.x, size.y)).mask(zIndex);
        Dynamics dynamics = new Dynamics();

        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);
        gameObject.addComponent(dynamics);
        gameObject.addComponent(body);

        return gameObject;
    }

    /**
     * Creates animated game object
     *
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param position position of this gameObject in the world
     * @param size width and height
     * @param animName name of the animation
     * @param start index in sprite sheet of starting sprite
     * @param end index in sprite sheet of ending sprite
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObjectC(String name, Spritesheet spritesheet, Vector2f position, Vector2f size, String animName, int start, int end, int zIndex)
    {
        GameObject gameObject = new GameObject(name, position, zIndex);

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start), size);
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);
        PolygonCollider body = new PolygonCollider(Shapes.axisAlignedRectangle(-(size.x) * 0.5f, -(size.y) * 0.5f, size.x, size.y)).mask(zIndex);
        Dynamics dynamics = new Dynamics();

        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);
        gameObject.addComponent(dynamics);
        gameObject.addComponent(body);

        return gameObject;
    }

    /**
     * Creates animated object
     * NOTE : This is only used if sprite sheet has only 1 row !
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param position position of this gameObject in the world
     * @param size width and height
     * @param animName name of the animation
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObjectC(String name, Spritesheet spritesheet, Vector2f position, Vector2f size, String animName, int zIndex)
    {
        GameObject gameObject = new GameObject(name, position, zIndex);

        int start = 0;
        int end = spritesheet.getSize() - 1;

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start), size);
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);
        PolygonCollider body = new PolygonCollider(Shapes.axisAlignedRectangle(-(size.x) * 0.5f, -(size.y) * 0.5f, size.x, size.y)).mask(zIndex);
        Dynamics dynamics = new Dynamics();

        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);
        gameObject.addComponent(dynamics);
        gameObject.addComponent(body);

        return gameObject;
    }


    /**
     * Creates animated object
     * NOTE : This is only used if sprite sheet has only 1 row and wants to use the size of texture itself!
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param position position of this gameObject in the world
     * @param animName name of the animation
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObjectC(String name, Spritesheet spritesheet, Vector2f position, String animName, int zIndex)
    {
        GameObject gameObject = new GameObject(name, position, zIndex);

        int start = 0;
        int end = spritesheet.getSize() - 1;

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start));
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);
        PolygonCollider body = new PolygonCollider(Shapes.axisAlignedRectangle(-(spriteRenderer.getSize().x) * 0.5f, -(spriteRenderer.getSize().y) * 0.5f, spriteRenderer.getSize().x, spriteRenderer.getSize().y)).mask(zIndex);
        Dynamics dynamics = new Dynamics();

        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);
        gameObject.addComponent(dynamics);
        gameObject.addComponent(body);

        return gameObject;
    }

    /**
     * Creates animated object
     * NOTE : This is only used if sprite sheet has only 1 row and wants to use the size of texture itself!
     *        Position will be middle of the screen
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param animName name of the animation
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObjectC(String name, Spritesheet spritesheet, String animName, int zIndex)
    {
        midScreen.x = Window.getWidth() * 0.5f;
        midScreen.y = Window.getHeight() * 0.5f;
        GameObject gameObject = new GameObject(name, midScreen, zIndex);

        int start = 0;
        int end = spritesheet.getSize() - 1;

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start));
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);
        PolygonCollider body = new PolygonCollider(Shapes.axisAlignedRectangle(-(spriteRenderer.getSize().x) * 0.5f, -(spriteRenderer.getSize().y) * 0.5f, spriteRenderer.getSize().x, spriteRenderer.getSize().y)).mask(zIndex);
        Dynamics dynamics = new Dynamics();

        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);
        gameObject.addComponent(dynamics);
        gameObject.addComponent(body);

        return gameObject;
    }


    /**
     * Creates animated game object
     *
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param position position of this gameObject in the world
     * @param angle Angle of rotation
     * @param size width and height
     * @param animName name of the animation
     * @param start index in sprite sheet of starting sprite
     * @param end index in sprite sheet of ending sprite
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObject(String name, Spritesheet spritesheet, Vector2f position, float angle, Vector2f size, String animName, int start, int end, int zIndex)
    {
        GameObject gameObject = new GameObject(name, position, zIndex);

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start), size);
        spriteRenderer.rotation = angle;
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);


        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);


        return gameObject;
    }

    /**
     * Creates animated game object NO COLLISION
     *
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param position position of this gameObject in the world
     * @param size width and height
     * @param animName name of the animation
     * @param start index in sprite sheet of starting sprite
     * @param end index in sprite sheet of ending sprite
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObject(String name, Spritesheet spritesheet, Vector2f position, Vector2f size, String animName, int start, int end, int zIndex)
    {
        GameObject gameObject = new GameObject(name, position, zIndex);

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start), size);
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);

        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);

        return gameObject;
    }

    /**
     * Creates animated object NO COLLISION
     * NOTE : This is only used if sprite sheet has only 1 row !
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param position position of this gameObject in the world
     * @param size width and height
     * @param animName name of the animation
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObject(String name, Spritesheet spritesheet, Vector2f position, Vector2f size, String animName, int zIndex)
    {
        GameObject gameObject = new GameObject(name, position, zIndex);

        int start = 0;
        int end = spritesheet.getSize() - 1;

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start), size);
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);


        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);


        return gameObject;
    }


    /**
     * Creates animated object NO COLLISION
     * NOTE : This is only used if sprite sheet has only 1 row and wants to use the size of texture itself!
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param position position of this gameObject in the world
     * @param animName name of the animation
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObject(String name, Spritesheet spritesheet, Vector2f position, String animName, int zIndex)
    {
        GameObject gameObject = new GameObject(name, position, zIndex);

        int start = 0;
        int end = spritesheet.getSize() - 1;

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start));
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);


        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);


        return gameObject;
    }

    /**
     * Creates animated object NO COLLISION
     * NOTE : This is only used if sprite sheet has only 1 row and wants to use the size of texture itself!
     *        Position will be middle of the screen
     * @param name Name of the gameObject
     * @param spritesheet reference to sprite sheet object
     * @param animName name of the animation
     * @param zIndex physics collision mask and zIndex of this sprite
     * @return created GameObject
     */
    public static GameObject CreateAnimatedGameObject(String name, Spritesheet spritesheet, String animName, int zIndex)
    {
        midScreen.x = Window.getWidth() * 0.5f;
        midScreen.y = Window.getHeight() * 0.5f;
        GameObject gameObject = new GameObject(name, midScreen, zIndex);

        int start = 0;
        int end = spritesheet.getSize() - 1;

        SpriteRenderer spriteRenderer = new SpriteRenderer(spritesheet.getSprite(start));
        SpriteAnimation animation = new SpriteAnimation(spriteRenderer, spritesheet.getSprite(start), 0.05f);
        animation.setAnimation(animName, Arrays.asList(spritesheet.getSprite(start), spritesheet.getSprite(end)));
        animation.nextAnimation(animName, -1);

        gameObject.addComponent(spriteRenderer);
        gameObject.addComponent(animation);


        return gameObject;
    }

}
