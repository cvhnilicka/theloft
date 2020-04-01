package com.cormicopiastudios.theloft.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cormicopiastudios.theloft.GameEngine.Components.TextureComponent;

public class AnimationSystem extends IteratingSystem {


    ComponentMapper<TextureComponent> tm;
//    ComponentMapper<AnimationComponent> am;
//    ComponentMapper<StateComponent> sm;

    @SuppressWarnings("unchecked")
    public AnimationSystem(){
        super(Family.all(TextureComponent.class).get());

        tm = ComponentMapper.getFor(TextureComponent.class);
//        am = ComponentMapper.getFor(AnimationComponent.class);
//        sm = ComponentMapper.getFor(StateComponent.class);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
