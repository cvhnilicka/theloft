package com.cormicopiastudios.theloftbackend;

import com.cormicopiastudios.theloftshared.SharedObjects.MessageObject;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerObject;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerPos;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerReq;
import com.cormicopiastudios.theloftshared.SharedObjects.ServerState;
import com.cormicopiastudios.theloftshared.SharedUtils;
import com.github.czyzby.websocket.serialization.Serializer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

public class ClientHandler extends Thread {
    final ServerWebSocket wbs;
//    final AtomicInteger idCounter;
    final Serializer serializer;
    final Vertx vertx;
    long timerId; // timer id used to send/cancel updates
    ServerLauncher server;
    Logger logger;

    public ClientHandler(ServerWebSocket websocket, Serializer serializer, Vertx vertx, ServerLauncher server) {
        this.wbs = websocket;
//        this.idCounter = idCounter;
        this.serializer = serializer;
        this.vertx = vertx;
        this.server = server;
        logger = Logger.getLogger("Server Side - Client Handler");
        logger.log(Level.INFO, "New Client Handler created");
//        ServerLauncher.updateNumClient(1);
    }

    public void run() {
        wbs.frameHandler(frame -> handleFrame(wbs, frame));

        // here i can also start to mess around with sending updates

        // delay updates for 1 sec to establish connection
        vertx.setTimer(1000, id -> {
            // here i can set the periodic timer to send out updates on X interval
            timerId = vertx.setPeriodic(15, i -> {
//                System.out.println("Sending Update");
                ServerState state = new ServerState();
//                state.numClients = ServerLauncher.numClients;
                String toSend = SharedUtils.serverMapToString(this.server.map);
                state.state = toSend;

                try {
                    byte[] sending = serializer.serialize(state);
                    wbs.writeBinaryMessage(Buffer.buffer(sending));
                } catch (Exception e) {
                    logger.log(Level.WARNING, e.toString());
                }
            });
        });


        // closing the handler
        wbs.closeHandler( v -> {
            logger.log(Level.INFO, "The socket has been closed");
            // here i need to:
            // 1) Update the number of clients/server mapping
            server.map.remove((Long)this.getId());
            // 2) remove thread from pool of active handlers
            server.handlers.remove((Thread)this);
            // 3) send message out to all other handlers that there was a DC
            this.server.clientLeft(this.getId());
            // 4) Cancel the periodic update timer in vertex for this thread
            vertx.cancelTimer(timerId);

        });
    }

    private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        // here i can do the deserializing and the 'instanceof' switching

        final Object request = serializer.deserialize(frame.binaryData().getBytes());




        // if (request instanceof object) etc.......
        if (request instanceof MessageObject) {
            // New client joined, passing along their specific server informatiom
            logger.log(Level.INFO, "Received MESSAGE: " + ((MessageObject)request).message);
            final PlayerReq nc = new PlayerReq();
            nc.tid = (int)this.getId();
            logger.log(Level.INFO, "Sending ID Update");
            System.out.println(nc);
//            final PlayerObject newClient = new PlayerObject();
//            newClient.name = "";
//            newClient.tid = (int)this.getId();
            webSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(nc)));
        }


        if (request instanceof PlayerPos) {
            PlayerPos temp = (PlayerPos)request;
//            System.out.println("Received Position: " + ((PlayerPos) request).x + "," + ((PlayerPos) request).y);
            this.server.updateMap(this.getId(),temp.x, temp.y);
        }

    }

    public void newClientMessage(long tid) {
        MessageObject newClient = new MessageObject();
        newClient.message = "New Client has joined with id " + tid;
        newClient.isLeaving = false;
        newClient.id = (int)tid;
        wbs.writeBinaryMessage(Buffer.buffer(serializer.serialize(newClient)));
    }
    public void clientHasLeft(long tid) {
        MessageObject clientLeft = new MessageObject();
        clientLeft.message = "Client has left: " + tid;
        clientLeft.isLeaving = true;
        clientLeft.id = (int)tid;
        wbs.writeBinaryMessage(Buffer.buffer(serializer.serialize(clientLeft)));
    }

}
