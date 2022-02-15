package hu.zol1e.quizapp.gamesocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.zol1e.quizapp.model.QuizGame;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/game/{gameId}")
@ApplicationScoped
public class QuizGameSocket {

    private static Uni<Map<String, Set<Session>>> sessions = new ConcurrentHashMap<>();

    static {
        sessions = Uni.createFrom(). new ConcurrentHashMap<>();
    }

    @Inject
    private ObjectMapper mapper;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") String gameId) {
        addSession(gameId, session);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("gameId") String gameId) {

    }

    @OnClose
    public void onClose(Session session, @PathParam("gameId") String gameId) {
        removeSession(gameId, session);
    }

    @OnError
    public void onError(Session session, @PathParam("gameId") String gameId, Throwable throwable) {
        removeSession(gameId, session);
    }

    private void addSession(String gameId, Session session) {
        Set<Session> sessions = this.sessions.get(gameId);
        if(sessions == null) {
            sessions = new HashSet<>();
            this.sessions.put(gameId, sessions);
        }
        sessions.add(session);
    }

    private void removeSession(String gameId, Session session) {
        Set<Session> sessions = this.sessions.get(gameId);
        if(sessions != null) {
            sessions.remove(session);
        }
    }

    public void broadcastState(QuizGame quizGame) {
        Set<Session> sessions = this.sessions.get(quizGame.getId());
        if(sessions != null) {
            for(Session session : sessions) {
                session.getAsyncRemote().sendText("asd");
            }
        }
    }

    public int getSessionsSize() {
        return this.sessions.size();
    }

}
