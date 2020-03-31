package com.cormicopiastudios.theloft.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cormicopiastudios.theloft.GameEngine.Components.BodyComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.theloft.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.theloft.GameEngine.GameMaster;
import com.cormicopiastudios.theloft.GameEngine.Views.PlayScreen;

public class PhysicsSystem extends IntervalIteratingSystem {
    private static final float MAX_STEP_TIME = 1/45f;

    private World world;
    //    private InputController controller;
    private Array<Entity> bodiesQueue;
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
//    private InputController controller;
    private GameMaster master;
    private PlayScreen parent;


    public PhysicsSystem(GameMaster master, PlayScreen parent) {
        super(Family.all().get(), MAX_STEP_TIME);
        this.master = master;
        this.parent = parent;
        this.world = parent.getWorld();
        this.bodiesQueue = new Array<Entity>();
//        this.controller = controller;
    }
    @Override
    protected void processEntity(Entity entity) {
        bodiesQueue.add(entity);
    }

    public void updateInterval() {
        super.updateInterval();
        world.step(MAX_STEP_TIME, 6, 2);
        for (Entity ent : bodiesQueue) {
            TransformComponent tfm = tm.get(ent);
            BodyComponent bodyComp = bm.get(ent);
            Vector2 pos = bodyComp.body.getPosition();
            if ((tfm.position.x != pos.x || tfm.position.y != pos.y) && !ent.getComponent(PlayerComponent.class).remote) {
                master.sendPosUpdate(pos);
            }
            tfm.position.x = pos.x;
            tfm.position.y = pos.y;


            bodyComp.body.setTransform(new Vector2(tfm.position.x,tfm.position.y), MathUtils.degreesToRadians * tfm.rotation);

        }

        bodiesQueue.clear();
    }
}
