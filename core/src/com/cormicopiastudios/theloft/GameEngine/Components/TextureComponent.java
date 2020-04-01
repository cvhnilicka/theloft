package com.cormicopiastudios.theloft.GameEngine.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {
    // Stores texture information
    public TextureRegion region = null;
    public Texture texture = null;
}
