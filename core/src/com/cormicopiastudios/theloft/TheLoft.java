package com.cormicopiastudios.theloft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cormicopiastudios.theloft.Frontend.Menus.EntranceScreen;
import com.cormicopiastudios.theloft.Frontend.Menus.LoadingScreen;
import com.cormicopiastudios.theloft.Frontend.Menus.MainMenu;
import com.cormicopiastudios.theloft.GameEngine.GameMaster;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.net.ExtendedNet;

import java.util.HashMap;

public class TheLoft extends Game {
	SpriteBatch batch;

	// screen constants for nav
	private LoadingScreen loadingScreen;
	public final static int LOADING = 1;
	private MainMenu mainMenu;
	public final static int MAINMENU = 2;
	private EntranceScreen entranceScreen;
	public final static int ENTRANCE = 3;
	private GameMaster gameMaster;
	public final static int GAME = 4;
	
	@Override
	public void create () {
		Gdx.graphics.setWindowedMode(900,720);
		batch = new SpriteBatch();
		changeScreen(LOADING);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.gameMaster.dispose();
		batch.dispose();
	}


	public void changeScreen(int screen) {
		switch (screen) {
			// need to add cases
			case LOADING: if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
				this.setScreen(loadingScreen);
				break;
			case MAINMENU: if (mainMenu == null) mainMenu = new MainMenu(this);
				this.setScreen(mainMenu);
				break;
			case ENTRANCE: if (entranceScreen == null) entranceScreen = new EntranceScreen(this);
			this.setScreen(entranceScreen);
			break;
			case GAME: gameMaster = new GameMaster(this);
				break;

		}
	}
}
