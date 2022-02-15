package hu.zol1e.quizapp.quiz;

import hu.zol1e.quizapp.model.Question;
import hu.zol1e.quizapp.model.Quiz;
import hu.zol1e.quizapp.quizgame.QuestionUtil;

public class QuizUtil {

    public static Quiz addQuestion(Quiz quiz, Question question) {
        return quiz.addQuestionsItem(QuestionUtil.generateIds(question));
    }

}
