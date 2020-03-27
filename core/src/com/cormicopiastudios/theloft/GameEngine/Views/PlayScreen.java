package com.cormicopiastudios.theloft.GameEngine.Views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cormicopiastudios.theloft.GameEngine.GameMaster;

public class PlayScreen implements Screen {

    static final float PPM = 32.f; // Sets the amount of pixels each meter of box2d object contain (will probably need to readjust this after the fact)


    // This gets the height and width of our camera frutsum based off the width and height of the screen and our pixel ration
    static final float FRUTSUM_W = Gdx.graphics.getWidth()/PPM;
    static final float FRUTSUM_H = Gdx.graphics.getHeight()/PPM;

    public GameMaster gameMaster;
    private World world;
    private PooledEngine engine;
    private Entity player;

    private OrthographicCamera gamecam;


    public PlayScreen(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
        world = new World(new Vector2(0,-0.f),true);

        gamecam = new OrthographicCamera(FRUTSUM_W, FRUTSUM_H);

        gamecam.position.set(FRUTSUM_W/2.f, FRUTSUM_H/2.f,0);

        engine = new PooledEngine();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


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
}
