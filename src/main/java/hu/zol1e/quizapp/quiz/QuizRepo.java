package hu.zol1e.quizapp.quiz;

import hu.zol1e.quizapp.model.Quiz;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuizRepo implements PanacheMongoRepositoryBase<Quiz, String> {
}
