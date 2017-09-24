package bricko.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import bricko.game.entities.Player;

public class Play implements Screen{

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private Player player;

    private Vector3 screenCoordinates;
    private Vector3 worldCoordinates;

    private int[] background = new int[] {0}, foreground = new int[] {1}, foreground1 = new int[] {2};

    @Override
    public void show() {
        map = new TmxMapLoader().load("koko.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();

        player = new Player(new Sprite(new Texture("player.png")), (TiledMapTileLayer) map.getLayers().get(0));

        player.setPosition( 3 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 20) * player.getCollisionLayer().getHeight());

        Gdx.input.setInputProcessor(player);


        screenCoordinates = new Vector3();
        worldCoordinates = new Vector3();

        //Animated tiles

        Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>(2);
        Array<StaticTiledMapTile> frameTiles1 = new Array<StaticTiledMapTile>(5);

        Iterator<TiledMapTile> tiles = map.getTileSets().getTileSet("jklol").iterator();
        while(tiles.hasNext()){
            TiledMapTile tile = tiles.next();
            if (tile.getProperties().containsKey("animation") && tile.getProperties().get("animation", String.class).equals("grass")){
                frameTiles.add((StaticTiledMapTile) tile);
            }
            if (tile.getProperties().containsKey("animation") && tile.getProperties().get("animation", String.class).equals("veg")){
                frameTiles1.add((StaticTiledMapTile) tile);
            }
        }

        AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(1 / 3f, frameTiles);
        AnimatedTiledMapTile animatedTile1 = new AnimatedTiledMapTile(1 / 5f, frameTiles1);

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("background");

        for(int x = 0; x < layer.getWidth(); x++)
            for(int y = 0; y < layer.getHeight(); y++) {
                Cell cell = layer.getCell(x, y);
                try {
                    if(cell.getTile().getProperties().containsKey("animation"))
                        if( cell.getTile().getProperties().get("animation", String.class).equals("grass"))
                            cell.setTile(animatedTile);
                }
                catch ( NullPointerException e){
                }
            }

        TiledMapTileLayer layer1 = (TiledMapTileLayer) map.getLayers().get("foreground");

        for(int x = 0; x < layer1.getWidth(); x++)
            for(int y = 0; y < layer1.getHeight(); y++) {
                Cell cell = layer1.getCell(x, y);
                try {
                    if(cell.getTile().getProperties().containsKey("animation"))
                        if( cell.getTile().getProperties().get("animation", String.class).equals("veg"))
                            cell.setTile(animatedTile1);
                }
                catch ( NullPointerException e){
                }
            }
    }

    @Override
    public void render(float delta) {
        //LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOL
        screenCoordinates.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCoordinates.set(screenCoordinates);
        camera.unproject(worldCoordinates);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //camera.position.set(3 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 20) * player.getCollisionLayer().getHeight(), 0);
        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        camera.update();

        renderer.setView(camera);

        renderer.render(background);

        renderer.getBatch().begin();
        player.draw(renderer.getBatch());
        renderer.getBatch().end();

        renderer.render(foreground);
        renderer.render(foreground1);

        if (Gdx.input.justTouched()){
            player.setPointX(worldCoordinates.x);
            player.setPointY(worldCoordinates.y);
            player.setOldPosX(player.getX() + player.getWidth() / 2);
            player.setOldPosY(player.getY() + player.getHeight() / 2);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 2, height / 2);
//        camera.viewportWidth = width / 3;
//        camera.viewportHeight = height / 3;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        player.getTexture().dispose();

    }
    public Vector3 getWorldCoordinates() {
        return worldCoordinates;
    }

    public Player getPlayer() {
        return player;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
