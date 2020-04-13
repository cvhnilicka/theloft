package com.cormicopiastudios.theloft.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.cormicopiastudios.theloft.GameEngine.Components.TextureComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.TypeComponent;
import com.cormicopiastudios.theloft.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.theloft.GameEngine.Views.PlayScreen;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {

    private SpriteBatch batch; // ref
    private Array<Entity> renderQueue; // array used to allow sorting of images allowing us to draw images on top of each other
    private Comparator<Entity> comparator; // comparator to sort images based on the z position of the TransformComponent
    private OrthographicCamera cam; // reference to our gamecam
    static final float PPM = 32.f; // Sets the amount of pixels each meter of box2d object contain (will probably need to readjust this after the fact)
    public static final float PIXELS_TO_METERS = 1.f /PPM; // conversion ratio
    private AssetController assetController;

    // component mappers to get compoents from entities

    // covert pixels to meters
    public static float PixelsToMeters(float pixelValue) {
        return pixelValue * PIXELS_TO_METERS;
    }

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;

    private PlayScreen parent;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    @SuppressWarnings("unchecked")
    public RenderingSystem(PlayScreen parent) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());
        this.parent = parent;
        // create component mappers
        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        assetController = parent.getAssetController();

        comparator = new ZComparator();

        renderQueue = new Array<Entity>();

        this.batch = new SpriteBatch();
        cam = parent.getGamecam();
        cam.setToOrtho(false,6,6);
        map = assetController.mapLoader.load(assetController.backgroundTMX);
        renderer = new OrthogonalTiledMapRenderer(map,1/PPM);
        renderer.setView(cam);


//        this.backgroundAtlas = parent.getAssetController().manager.get(
//                parent.getAssetController().backgroundPix, TextureAtlas.class);
//        this.backgroundAnim = new Animation(0.1f, backgroundAtlas.findRegions("Background"));
//        backgroundAnim.setPlayMode(Animation.PlayMode.LOOP);
//        stateTime = 0.f;
//        hud = new Hud(this, batch);

    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // sort on z index
        renderQueue.sort(comparator);
        Entity ent = parent.getPlayer();
        Vector3 ppos = ent.getComponent(TransformComponent.class).position;
        cam.position.set(ppos);
        renderer.setView(cam);
        cam.update();
        renderer.render();

        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();


        for (Entity entity : renderQueue) {
            TextureComponent tex = textureM.get(entity);
            TransformComponent trans = transformM.get(entity);
            if (trans.isHidden) {
                continue;
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width/2.f;
            float originY = height/2.f;

//            if (entity.getComponent(TypeComponent.class).type == TypeComponent.LOCAL_PLAYER) {
                batch.draw(tex.region,
                        trans.position.x - originX, trans.position.y - originY,
                        originX, originY,
                        width, height,
                        PixelsToMeters(trans.scale.x), PixelsToMeters(trans.scale.y),
                        trans.rotation);
//            }
        }
        batch.end();
        renderQueue.clear();

    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
