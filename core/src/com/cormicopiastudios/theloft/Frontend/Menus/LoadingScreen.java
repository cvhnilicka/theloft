package com.cormicopiastudios.theloft.Frontend.Menus;

import com.badlogic.gdx.Screen;
import com.cormicopiastudios.theloft.TheLoft;

public class LoadingScreen implements Screen {

    private TheLoft parent;

    public LoadingScreen(TheLoft parent) {
        this.parent = parent;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.parent.changeScreen(TheLoft.MAINMENU);
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
