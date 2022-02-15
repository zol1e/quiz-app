package hu.zol1e.quizapp.quizgame;

import hu.zol1e.quizapp.model.QuizGame;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuizGameRepo implements PanacheMongoRepositoryBase<QuizGame, String> {

}
