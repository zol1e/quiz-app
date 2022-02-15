package hu.zol1e.quizapp.gamesocket;

import hu.zol1e.quizapp.model.QuizGame;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class QuizGameSocketTest {

    @Inject
    QuizGameSocket gameSocket;

    @TestHTTPResource("/game/1")
    URI uri;

    @Test
    public void testGameSocket() throws Exception {
        Client client1 = new Client();
        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(client1, uri)) {
            assertTrue(client1.isOpen());

            Multi.createFrom().

            Thread.sleep(100);
            gameSocket.broadcastState(new QuizGame().id("1"));
            Thread.sleep(100);

            assertTrue(client1.getMessages().size() > 0);

            session.close();
            assertFalse(client1.isOpen());
        }
    }

    @ClientEndpoint
    public static class Client {

        private List<Object> messages = new ArrayList<>();

        private boolean isOpen = false;

        @OnOpen
        public void open(Session session) {
            isOpen = true;
        }

        @OnMessage
        public void message(String msg) {
            messages.add(msg);
        }

        @OnClose
        public void close(Session session) {
            isOpen = false;
        }

        public List<Object> getMessages() {
            return messages;
        }

        public boolean isOpen() {
            return isOpen;
        }

    }

}
