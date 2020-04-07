package com.cormicopiastudios.theloft.GameEngine;

import com.badlogic.gdx.math.Vector2;
import com.cormicopiastudios.theloft.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.theloft.GameEngine.Controllers.SocketListener;
import com.cormicopiastudios.theloft.GameEngine.Views.PlayScreen;
import com.cormicopiastudios.theloft.TheLoft;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerObject;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerPos;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.net.ExtendedNet;

import java.util.HashMap;

public class GameMaster {

    private TheLoft parent;
    private PlayScreen instance;
    private WebSocket socket;
    private AssetController assetController;


    public final static int TEMPTID = -99;

    private int localTid;
    HashMap<Integer, PlayerObject> map = new HashMap<>();



    public GameMaster(TheLoft parent) {
        this.parent = parent;
        this.assetController = new AssetController();
        assetController.queueAddImages();
        assetController.manager.finishLoading();
        this.socket = ExtendedNet.getNet().newWebSocket("127.0.0.1", 8765);
        WebSocketListener sl = new SocketListener(this);
        this.socket.addListener(sl);
        localTid = TEMPTID;
        socket.connect();
        instance = new PlayScreen(this);
        parent.setScreen(instance);

    }

    public void setLocalTid(int tid) {
        this.localTid = tid;
//        map.put(localTid,new PlayerObject());
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
            PlayerObject tm = new PlayerObject();
            tm.pos.x = pos.x;
            tm.pos.y = pos.y;
            map.put(Integer.valueOf(localTid), tm);
            socket.send(posUpdate);
        }
    }

    public HashMap<Integer, PlayerObject> getMap() {
        return map;
    }

    public PlayScreen getInstance() {
        return instance;
    }

    public void dispose() {
        WebSockets.closeGracefully(socket); // Null-safe closing method that catches and logs any exceptions.
    }

    public AssetController getAssetController() {
        return assetController;
    }
}
