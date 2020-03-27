package com.cormicopiastudios.theloft.GameEngine;

import com.cormicopiastudios.theloft.GameEngine.Controllers.SocketListener;
import com.cormicopiastudios.theloft.GameEngine.Views.PlayScreen;
import com.cormicopiastudios.theloft.TheLoft;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.net.ExtendedNet;

public class GameMaster {

    private TheLoft parent;
    private PlayScreen instance;
    private WebSocket socket;

    private long localTid;

    public GameMaster(TheLoft parent) {
        this.parent = parent;
        this.socket = ExtendedNet.getNet().newWebSocket("127.0.0.1", 8765);
        this.socket.addListener(new SocketListener(this));
        socket.connect();
        instance = new PlayScreen(this);
//        parent.setScreen(instance);

    }

    public void setLocalTid(long tid) { this.localTid = tid; }

}
