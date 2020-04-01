package com.cormicopiastudios.theloft.GameEngine.Components;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component {
    public static final int LOCAL_PLAYER = 0;
    public static final int REMOTE_PLAYER = 1;
    public static final int OTHER = 2;

    public int type = OTHER;
}
