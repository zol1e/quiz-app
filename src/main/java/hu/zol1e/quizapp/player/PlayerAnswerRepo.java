package hu.zol1e.quizapp.player;

import hu.zol1e.quizapp.model.Player;
import hu.zol1e.quizapp.model.PlayerAnswer;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerAnswerRepo implements PanacheMongoRepositoryBase<PlayerAnswer, String> {
}
