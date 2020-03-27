package com.cormicopiastudios.theloft.GameEngine.Controllers;

import com.cormicopiastudios.theloft.GameEngine.GameMaster;
import com.cormicopiastudios.theloftshared.SharedObjects.MessageObject;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerObject;
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

        // new client
        if (packet instanceof PlayerObject) {
            final PlayerObject localPlayer = (PlayerObject) packet;
            // save local TID for the local client : no remote info yet
            gameMaster.setLocalTid(localPlayer.tid);

        }

        // generic message object
        if (packet instanceof MessageObject) {
            // need to hand various generic cases of messageobjects
            // i.e. is joined, is leaving, etc
            final MessageObject messageObject = (MessageObject)packet;
            if (messageObject.isLeaving) {
                // Need to remove
                // update the game master instance to remove the remote client

            } else {
                // A new client has joined and therefor needs to be added to the local mapping

            }
        }

        return FULLY_HANDLED;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
        return FULLY_HANDLED;
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
        return FULLY_HANDLED;
    }
}
