package com.cormicopiastudios.theloft.GameEngine;

import com.badlogic.gdx.math.Vector2;
import com.cormicopiastudios.theloft.GameEngine.Controllers.SocketListener;
import com.cormicopiastudios.theloft.GameEngine.Views.PlayScreen;
import com.cormicopiastudios.theloft.TheLoft;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerPos;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.net.ExtendedNet;

public class GameMaster {

    private TheLoft parent;
    private PlayScreen instance;
    private WebSocket socket;

    public final static int TEMPTID = -99;

    private int localTid;

    public GameMaster(TheLoft parent) {
        this.parent = parent;
        this.socket = ExtendedNet.getNet().newWebSocket("127.0.0.1", 8765);
        this.socket.addListener(new SocketListener(this));
        localTid = TEMPTID;
        socket.connect();
        instance = new PlayScreen(this);
        parent.setScreen(instance);

    }

    public void setLocalTid(int tid) {
        this.localTid = tid;
        this.instance.setPlayerTID(tid);
    }

    public int getLocalTid() {
        return localTid;
    }

    public void sendPosUpdate(Vector2 pos) {
        if (socket.isOpen()) {
            final PlayerPos posUpdate = new PlayerPos();
            posUpdate.x = pos.x;
            posUpdate.y = pos.y;
//            parent.m.put(Integer.valueOf(parent.tid), posUpdate);
            socket.send(posUpdate);
        }
    }
}
