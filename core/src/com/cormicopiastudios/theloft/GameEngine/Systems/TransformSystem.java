package com.cormicopiastudios.theloft.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cormicopiastudios.theloft.GameEngine.Components.BodyComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.theloft.GameEngine.Views.PlayScreen;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerObject;

public class TransformSystem extends IteratingSystem {
    private PooledEngine engine;
    private PlayScreen parent;

    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<BodyComponent> b2m;
    ComponentMapper<TransformComponent> tm;

    public TransformSystem(PlayScreen parent) {
        super(Family.all(TransformComponent.class).get());
        this.engine = parent.getEngine();
        this.parent = parent;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        b2m = ComponentMapper.getFor(BodyComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent pc = pm.get(entity);
        TransformComponent tc = tm.get(entity);
        BodyComponent body = b2m.get(entity);

        if (pc.remote && parent.gameMaster.getMap().containsKey(pc.tid)) {
            PlayerObject p = parent.gameMaster.getMap().get(pc.tid);
//            System.out.println(pc.tid + ":" + p.x + ":" + p.y);
            tc.position.x = p.pos.x;
            tc.position.y = p.pos.y;
            body.body.setTransform(p.pos.x,p.pos.y,body.body.getAngle());

        }


    }
}
