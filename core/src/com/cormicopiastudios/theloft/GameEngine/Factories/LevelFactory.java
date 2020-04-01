package com.cormicopiastudios.theloft.GameEngine.Factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cormicopiastudios.theloft.GameEngine.Components.BodyComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.TextureComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.TypeComponent;
import com.cormicopiastudios.theloft.GameEngine.Views.PlayScreen;

public class LevelFactory {

    private PlayScreen parent;
    private PooledEngine engine;
    private World world;
    private BodyFactory bodyFactory;
    private OrthographicCamera gamecam;

    public LevelFactory(PlayScreen parent) {
        this.parent = parent;
        this.engine = parent.getEngine();
        this.world = parent.getWorld();
        this.gamecam = parent.getGamecam();
        this.bodyFactory = BodyFactory.getInstance(this.world);
    }

    public Entity createLocalPlayer() {

        float posx = this.gamecam.viewportWidth/2;  // TEMP
        float posy = this.gamecam.viewportHeight/2;   // TEMP


        Entity localPlayer = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);

        bodyComponent.body = bodyFactory.makeBoxPolyBody(posx,posy,1,2,
                BodyFactory.FIXTURE_TYPE.STEEL, BodyDef.BodyType.DynamicBody,true);

        textureComponent.region = this.parent.gameMaster.getAssetController().manager.get(
                this.parent.gameMaster.getAssetController().loftcharacters, TextureAtlas.class).findRegion("person-practice4loft-v1");
        typeComponent.type = TypeComponent.LOCAL_PLAYER;

        // set object pos
        transformComponent.position.set(posx,posy,0);
        transformComponent.scale.x = 2f;
        transformComponent.scale.y = 2f;
        bodyComponent.body.setUserData(localPlayer);
        playerComponent.cam = gamecam;
        playerComponent.tid = parent.gameMaster.getLocalTid();
        playerComponent.remote = false;
        System.out.println("Local Player TID: " + playerComponent.tid);

        // add components to entity
        localPlayer.add(bodyComponent);
        localPlayer.add(transformComponent);
        localPlayer.add(playerComponent);
        localPlayer.add(textureComponent);
        localPlayer.add(typeComponent);

        // add to engine
        engine.addEntity(localPlayer);
        return localPlayer;
    }

    public void createRemotePlayer(int tid) {

        float posx = this.gamecam.viewportWidth/2;  // TEMP
        float posy = this.gamecam.viewportHeight/2;   // TEMP


        Entity remotePlayer = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);

        bodyComponent.body = bodyFactory.makeBoxPolyBody(posx,posy,1,2,
                BodyFactory.FIXTURE_TYPE.STEEL, BodyDef.BodyType.DynamicBody,true);

        textureComponent.region = this.parent.gameMaster.getAssetController().manager.get(
                this.parent.gameMaster.getAssetController().loftcharacters, TextureAtlas.class).findRegion("superhero-looking-character-large2");
        typeComponent.type = TypeComponent.REMOTE_PLAYER;


        // set object pos
        transformComponent.position.set(posx,posy,0);
        transformComponent.scale.x = 2f;
        transformComponent.scale.y = 2f;
        bodyComponent.body.setUserData(remotePlayer);
        playerComponent.cam = gamecam;
        playerComponent.tid = tid;
        playerComponent.remote = true;
        System.out.println("Remote Player TID: " + playerComponent.tid);

        // add components to entity
        remotePlayer.add(bodyComponent);
        remotePlayer.add(transformComponent);
        remotePlayer.add(playerComponent);
        remotePlayer.add(textureComponent);
        remotePlayer.add(typeComponent);

        // add to engine
        engine.addEntity(remotePlayer);
    }

}
