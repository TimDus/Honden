module communicatorclient {
    requires gson;
    requires communicatorshared;

    // Do not require javax.websocket.api.
    // Only a specification, needs an implementation like javax.websocket.client.api

    requires javax.websocket.client.api;
    requires java.sql;

    exports communicatorclient;
}