package hu.zol1e.quizapp.quizgame;

import com.google.common.base.Strings;
import hu.zol1e.quizapp.model.Answer;
import hu.zol1e.quizapp.model.Question;
import hu.zol1e.quizapp.model.QuizGame;

import java.util.List;

import static hu.zol1e.quizapp.quizgame.QuestionUtil.getNextQuestion;

public class QuizGameUtil {

    public static QuizGame setQuizGameStarted(QuizGame quizGame) {
        List<Question> questions = quizGame.getQuiz().getQuestions();
        if(questions.isEmpty()) {
            throw new IllegalStateException("Cannot start QuizGame without question!");
        }
        quizGame.state(QuizGame.StateEnum.QUESTION).actualQuestion(questions.get(0));

        return quizGame;
    }

    public static QuizGame setNextQuestion(QuizGame quizGame) {
        Question actualQuestion = quizGame.getActualQuestion();

        Question nextQuestion = getNextQuestion(quizGame, actualQuestion);
        quizGame.actualQuestion(nextQuestion);

        if(nextQuestion == null) {
            quizGame.state(QuizGame.StateEnum.FINISHED);
        }

        return quizGame;
    }

    public static boolean isValidAnswer(Answer answer, Question question) {
        if(question == null) {
            throw new IllegalArgumentException("Question is NULL");
        }
        if(answer == null) {
            throw new IllegalArgumentException("Answer is NULL");
        }

        String questionId = answer.getQuestionId();
        if (Strings.isNullOrEmpty(questionId)) {
            throw new IllegalArgumentException("Missing questionId from answer!");
        }

        if(!hasAnswerWithSameId(question, answer)) {
            throw new IllegalArgumentException("Answer is NULL");
        }

        return question.getId().equals(questionId);
    }

    private static boolean hasAnswerWithSameId(Question question, Answer answer) {
        return question.getAnswers().stream().anyMatch(a -> {

            return a.getId() != null && a.getId().equals(answer.getId());

        });
    }

}
