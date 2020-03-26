package com.cormicopiastudios.theloftbackend;

import com.cormicopiastudios.theloftshared.SharedObjects.PlayerObject;
import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class ServerLauncher {


    // will be going for a simple server configuration for the loft as it will only need to
    // record players in one loft

    private final Serializer serializer;
    private final Vertx vertx = Vertx.vertx();
    HttpServer server;
    public final int PORT = 8765;
    protected ArrayList<Thread> handlers;
    protected Map<Long, PlayerObject> map;


    public static void main(final String... args) throws  Exception
    {
        new ServerLauncher().launch();
    }

    public ServerLauncher() {
        serializer = new JsonSerializer();
        handlers = new ArrayList<>();
        map = Collections.synchronizedMap(new HashMap<Long, PlayerObject>());
    }

    private void launch() {
        System.out.println("Launching the web socket server...");
        final HttpServer server = vertx.createHttpServer(); // create the server to listen
        this.server = server;
        server.websocketHandler( websocket -> {
            // here i need to instantiate a new thread-client handler to handle said client

            Thread h = new ClientHandler(websocket,serializer, vertx, this);
            this.handlers.add(h);
            this.map.put(h.getId(), new PlayerObject());
            h.start();


        }).listen(PORT);
    }

}
