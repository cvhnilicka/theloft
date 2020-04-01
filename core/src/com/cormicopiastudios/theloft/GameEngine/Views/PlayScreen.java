package com.cormicopiastudios.theloft.GameEngine.Views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cormicopiastudios.theloft.GameEngine.Components.BodyComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.theloft.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.theloft.GameEngine.Controllers.InputController;
import com.cormicopiastudios.theloft.GameEngine.Factories.BodyFactory;
import com.cormicopiastudios.theloft.GameEngine.Factories.LevelFactory;
import com.cormicopiastudios.theloft.GameEngine.GameMaster;
import com.cormicopiastudios.theloft.GameEngine.Systems.PhysicsDebugSystem;
import com.cormicopiastudios.theloft.GameEngine.Systems.PhysicsSystem;
import com.cormicopiastudios.theloft.GameEngine.Systems.PlayerControlSystem;
import com.cormicopiastudios.theloft.GameEngine.Systems.PlayerSystem;
import com.cormicopiastudios.theloft.GameEngine.Systems.RenderingSystem;
import com.cormicopiastudios.theloft.GameEngine.Systems.TransformSystem;

public class PlayScreen implements Screen {

    static final float PPM = 32.f; // Sets the amount of pixels each meter of box2d object contain (will probably need to readjust this after the fact)


    // This gets the height and width of our camera frutsum based off the width and height of the screen and our pixel ration
    static final float FRUTSUM_W = Gdx.graphics.getWidth()/PPM;
    static final float FRUTSUM_H = Gdx.graphics.getHeight()/PPM;

    public final int PPR = 1/32;

    public GameMaster gameMaster;
    private World world;
    private PooledEngine engine;
    private Entity player;
    private BodyFactory bodyFactory;
    private LevelFactory lvlF;
    private AssetController assetController;
    private Viewport gamePort;

    private OrthographicCamera gamecam;
    private InputController inputController;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    public PlayScreen(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
        world = new World(new Vector2(0,-0.f),true);
        inputController = new InputController();
        assetController = new AssetController();
        gamecam = new OrthographicCamera();

//        gamePort = new FitViewport(320,320, gamecam);


//        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
        gamecam.setToOrtho(false, 10,10);
//        gamecam.position.x = MathUtils.clamp(gamecam.position.x, gamePort.getWorldWidth()/32,gamePort.getWorldWidth()/2);
//        gamecam.position.y = MathUtils.clamp(gamecam.position.y, gamePort.getWorldHeight()/32,gamePort.getWorldHeight()/2);
        RenderingSystem renderingSystem = new RenderingSystem(this);

        map = assetController.mapLoader.load(assetController.backgroundTMX);
        renderer = new OrthogonalTiledMapRenderer(map,1/PPM);
        renderer.setView(gamecam);
//        renderer.setView(gamecam.projection,-gamecam.viewportWidth/2,-gamecam.viewportHeight/2,320,320);
        engine = new PooledEngine();
        lvlF = new LevelFactory(this);
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(gameMaster, this));
//        engine.addSystem(new PhysicsDebugSystem(world,gamecam));
        player = lvlF.createLocalPlayer();
        engine.addSystem(new PlayerControlSystem(inputController, engine, gameMaster));
        engine.addSystem(new TransformSystem(this));
        engine.addSystem(new PlayerSystem(this));
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputController);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public PooledEngine getEngine() {
        return engine;
    }

    public World getWorld() {
        return world;
    }

    public OrthographicCamera getGamecam() {
        return gamecam;
    }

    public void setPlayerTID(int tid) {
        if (player != null) {
            player.getComponent(PlayerComponent.class).tid = tid;
        }
    }

    public void removeRemotePlayer(int tid) {
        ImmutableArray<Entity> playerChars = this.engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        for (int i = 0; i < playerChars.size(); i++) {
            if (playerChars.get(i).getComponent(PlayerComponent.class).tid == tid) {
                world.destroyBody(playerChars.get(i).getComponent(BodyComponent.class).body);
                this.engine.removeEntity(playerChars.get(i));
                gameMaster.getMap().remove(tid);
            }
        }
    }

    public void createRemotePlayerInstance(int tid) {
        lvlF.createRemotePlayer(tid);
    }

}
