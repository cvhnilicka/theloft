package com.cormicopiastudios.theloft.GameEngine.Controllers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetController {
    public final AssetManager manager = new AssetManager();
    public final TmxMapLoader mapLoader = new TmxMapLoader();

    // strings
    public final String backgroundTMX = "background/theloft.tmx";
    public final String garageBackagroundTMX = "background/arcade.tmx";
    public final String loftcharacters = "characters/loftcharacters.atlas";


    // player



    public void queueAddImages() {
        manager.load(loftcharacters, TextureAtlas.class);


    }

    public void queueMenuButtons(){




    }
}
