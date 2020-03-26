package com.cormicopiastudios.theloftbackend;

import com.github.czyzby.websocket.serialization.Serializer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

public class ClientHandler extends Thread {
    final ServerWebSocket wbs;
//    final AtomicInteger idCounter;
    final Serializer serializer;
    final Vertx vertx;
    long timerId;
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
            // 4) Cancel the periodic update timer in vertex for this thread
        });
    }

    private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        // here i can do the deserializing and the 'instanceof' switching

        final Object request = serializer.deserialize(frame.binaryData().getBytes());


        // if (request instanceof object) etc.......
    }

}