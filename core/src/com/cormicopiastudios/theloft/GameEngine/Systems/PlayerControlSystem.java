package com.cormicopiastudios.theloft.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.cormicopiastudios.theloft.GameEngine.Components.BodyComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.theloft.GameEngine.Controllers.InputController;
import com.cormicopiastudios.theloft.GameEngine.GameMaster;

public class PlayerControlSystem extends IteratingSystem {
    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<BodyComponent> b2m;
    ComponentMapper<TransformComponent> tm;
    private PooledEngine en;

    InputController controller;

    GameMaster master;

    @SuppressWarnings("unchecked")
    public PlayerControlSystem(InputController controller, PooledEngine en, GameMaster master) {
        super(Family.all(PlayerComponent.class).get());
        this.controller = controller;
        this.en = en;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        b2m = ComponentMapper.getFor(BodyComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        this.master = master;

    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent b2body = b2m.get(entity);
        PlayerComponent player = pm.get(entity);
        TransformComponent trans = tm.get(entity);
        if (!player.remote) {


            int maxSpeed = 3;
            boolean moved = false;
            if (controller.left && b2body.body.getLinearVelocity().x > -maxSpeed) {
                b2body.body.applyLinearImpulse(new Vector2(-.3f, 0), b2body.body.getWorldCenter(), true);
                moved = true;
            }
            if (controller.right && b2body.body.getLinearVelocity().x < maxSpeed) {
                b2body.body.applyLinearImpulse(new Vector2(.3f, 0), b2body.body.getWorldCenter(), true);
                moved = true;


            }

            if (controller.up && b2body.body.getLinearVelocity().y < maxSpeed) {
                b2body.body.applyLinearImpulse(new Vector2(0, .3f), b2body.body.getWorldCenter(), true);
                moved = true;

            }
            if (controller.down && b2body.body.getLinearVelocity().y > -maxSpeed) {
                moved = true;
                b2body.body.applyLinearImpulse(new Vector2(0, -.3f), b2body.body.getWorldCenter(), true);

            }

        }






    }
}
