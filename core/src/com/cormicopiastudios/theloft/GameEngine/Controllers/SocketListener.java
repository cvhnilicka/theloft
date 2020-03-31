package com.cormicopiastudios.theloft.GameEngine.Controllers;

import com.cormicopiastudios.theloft.GameEngine.GameMaster;
import com.cormicopiastudios.theloftshared.SharedObjects.MessageObject;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerObject;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerReq;
import com.cormicopiastudios.theloftshared.SharedObjects.ServerState;
import com.cormicopiastudios.theloftshared.SharedUtils;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketCloseCode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketListener extends AbstractWebSocketListener {
    Logger socketLogger = Logger.getLogger("SocketLogger");

    private GameMaster gameMaster;

    public  SocketListener(GameMaster gameMaster) {
        this.gameMaster = gameMaster;

    }

    @Override
    public boolean onOpen(WebSocket webSocket) {
        socketLogger.log(Level.INFO, "Connected");
        final MessageObject message = new MessageObject();
        message.message = "new client requesting connection";
        webSocket.send(message);
        return FULLY_HANDLED;
    }

    @Override
    public boolean onClose(WebSocket webSocket, WebSocketCloseCode code, String reason) {
        return FULLY_HANDLED;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, Object packet) {
//        socketLogger.log(Level.INFO, "OnMessage");

        // new client
        if (packet instanceof PlayerObject) {
            final PlayerObject localPlayer = (PlayerObject) packet;
            // save local TID for the local client : no remote info yet
            socketLogger.log(Level.INFO, "Set the local TID in GameMaster");
            gameMaster.setLocalTid(localPlayer.tid);

        }


        if (packet instanceof PlayerReq) {
            socketLogger.log(Level.INFO, "Set the local TID in GameMaster");
            gameMaster.setLocalTid(((PlayerReq)packet).tid);
        }

        // generic message object
        if (packet instanceof MessageObject) {
            // need to hand various generic cases of messageobjects
            // i.e. is joined, is leaving, etc
            final MessageObject messageObject = (MessageObject)packet;
            if (messageObject.isLeaving) {
                // Need to remove
                // update the game master instance to remove the remote client
                socketLogger.log(Level.INFO, "Remote Client has left");
                gameMaster.getInstance().removeRemotePlayer(messageObject.id);

            } else {
                // A new client has joined and therefor needs to be added to the local mapping
                gameMaster.getInstance().createRemotePlayerInstance(messageObject.id);

            }
        }

        // server state
        if (packet instanceof ServerState) {
            ServerState tmp = (ServerState)packet;
            SharedUtils.stringToMap(gameMaster.getMap(), tmp.state);
        }

        return FULLY_HANDLED;
    }

}
