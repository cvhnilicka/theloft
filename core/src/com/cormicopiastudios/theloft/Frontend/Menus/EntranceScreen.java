package com.cormicopiastudios.theloft.Frontend.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cormicopiastudios.theloft.TheLoft;

public class EntranceScreen implements Screen {

    // here you will be given the preferences before you enter.
    // i.e. nickname and character choice

    private Stage stage;
    private Skin skin; // will temp use a skin
    private TheLoft parent;

    public EntranceScreen(TheLoft parent) {
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/shade/uiskin.json"));
    }

    @Override
    public void show() {
        Gdx.app.log("Main Menu", "Show");
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // overarching table to hold all pref choices.
        Table table = new Table();
        table.setFillParent(true);
        TextButton enter = new TextButton("Enter", skin);
        enter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(TheLoft.GAME);
            }
        });

        table.add(enter).fillX().uniform();
        table.row();
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
