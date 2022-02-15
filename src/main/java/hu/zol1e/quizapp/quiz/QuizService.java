package hu.zol1e.quizapp.quiz;

import com.google.common.base.Strings;
import hu.zol1e.quizapp.model.Question;
import hu.zol1e.quizapp.model.Quiz;
import hu.zol1e.quizapp.quizgame.QuestionUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class QuizService {

    @Inject
    QuizRepo quizRepo;

    public Quiz createQuiz(String name) {
        Quiz newQuiz = new Quiz()
                .id(UUID.randomUUID().toString())
                .name(name);

        quizRepo.persist(newQuiz);
        return newQuiz;
    }

    public Quiz addQuestion(String quizId, Question question) {
        Quiz quiz = quizRepo.findByIdOptional(quizId).orElseThrow();
        quiz = QuizUtil.addQuestion(quiz, question);

        quizRepo.update(quiz);
        return quiz;
    }

}
