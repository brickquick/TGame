package bricko.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector3;

public class Player extends Sprite implements InputProcessor {

    private Vector3 velocity = new Vector3();
    private Vector3 position = new Vector3(getX(), getY(), 0);
    private float speed = 80 * 2, gravity = 60 * 1.8f;


    private float pointX, pointY, oldPosX, oldPosY, percent = 50.0f;

    private TiledMapTileLayer collisionLayer;

    private String blockedKey = "blocked";

    public Player(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;

    }

    @Override
    public void draw(Batch spriteBatch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
        setSize(32, 32);
        //setSize((float) differnce(getX() + getWidth() / 2, getY() + getHeight() / 2, pointX, pointY) / 2 + 32, (float) differnce(getX() + getWidth() / 2, getY() + getHeight() / 2, pointX, pointY) / 2 + 32);
    }

    public void update(float delta) {
        // apply gravity
        //velocity.y -= gravity * delta;
        // clamp velocity
        //if(velocity.y > speed)
         //   velocity.y = speed;
        //else if(velocity.y < -speed)
          //  velocity.y = -speed;

        // save old position
        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY = false;

        // move on x
        setX(getX() + velocity.x * delta);

        if(velocity.x < 0) // going left
            collisionX = collidesLeft();
        else if(velocity.x > 0) // going right
            collisionX = collidesRight();

        // react to x collision
        if(collisionX) {
            setX(oldX);
            velocity.x = 0;
        }

        // move on y
        setY(getY() + velocity.y * delta * 5f);

        if(velocity.y < 0) // going down
            collisionY = collidesBottom();
        else if(velocity.y > 0) // going up
            collisionY = collidesTop();

        // react to y collision
        if(collisionY) {
            setY(oldY);
            velocity.y = 0;
        }

        //===============================================================================================

        if (pointX != 0 && pointY != 0){
            //velocity.x = -((getX()+32/2 - pointX) / 100 * percent) * 5;
            //velocity.y = -((getY()+32/2 - pointY) / 100 * percent);
            velocity.x = -(((getX()+32/2 - pointX) / (float) differnce(oldPosX + getWidth() / 2, oldPosY + getHeight() / 2, pointX, pointY)) * percent) * 5;
            velocity.y = -((getY()+32/2 - pointY) / (float) differnce(oldPosX + getWidth() / 2, oldPosY + getHeight() / 2, pointX, pointY)) * percent;
            System.out.println(velocity.x + "  ");
            System.out.println(velocity.y);
        }
        if ((int) differnce(getX() + getWidth() / 2, getY() + getHeight() / 2, pointX, pointY) <= 2) {
            velocity.x = 0;
            velocity.y = 0;
            //pointY = 0;
            //pointX = 0;
        }

    }

    private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
    }

    public boolean collidesRight() {
        for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellBlocked(getX() + getWidth(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesLeft() {
        for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellBlocked(getX(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesTop() {
        for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellBlocked(getX() + step, getY() + getHeight()))
                return true;
        return false;

    }

    public boolean collidesBottom() {
        for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellBlocked(getX() + step, getY()))
                return true;
        return false;
    }

    private boolean isMoved(){
        if (velocity.x > 0 || velocity.y > 0) {
            return true;
        } else {
            velocity.x = 0;
            velocity.y = 0;
            return false;
        }
    }

    public double differnce(float x1, float y1, float x2, float y2){
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.W:
                velocity.y = speed / 5;
                break;
            case Input.Keys.A:
                velocity.x = -speed;
                break;
            case Input.Keys.S:
                velocity.y = -speed / 5;
                break;
            case Input.Keys.D:
                velocity.x = speed;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Input.Keys.W:
                velocity.y = 0;
                break;
            case Input.Keys.A:
                velocity.x = 0;
                break;
            case Input.Keys.S:
                velocity.y = 0;
                break;
            case Input.Keys.D:
                velocity.x = 0;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setPointY(float pointY) {
        this.pointY = pointY;
    }

    public void setPointX(float pointX) {
        this.pointX = pointX;
    }

    public void setOldPosX(float oldPosX) {
        this.oldPosX = oldPosX;
    }

    public void setOldPosY(float oldPosY) {
        this.oldPosY = oldPosY;
    }
}
