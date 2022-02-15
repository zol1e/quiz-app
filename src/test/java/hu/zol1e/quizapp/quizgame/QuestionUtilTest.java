package hu.zol1e.quizapp.quizgame;

import hu.zol1e.quizapp.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuestionUtilTest {

    @Test
    public void getNextQuestionOneQuestionTest() {
        Question question1 = QuestionUtil.generateIds(new Question().text("First question")
                .addAnswersItem(new Answer().text("Incorrect answer").correct(false))
                .addAnswersItem(new Answer().text("Correct answer").correct(true)));

        Quiz quiz = new Quiz().addQuestionsItem(question1);

        QuizGame quizGame = new QuizGame().quiz(quiz);
        quizGame = QuizGameUtil.setQuizGameStarted(quizGame);

        Question actualQuestion = quizGame.getActualQuestion();
        assertEquals(question1.getId(), actualQuestion.getId());

        quizGame = QuizGameUtil.setNextQuestion(quizGame);
        assertNull(quizGame.getActualQuestion());
    }

    @Test
    public void getNextQuestionWithTwoQuestionsTest() {
        Question question1 = QuestionUtil.generateIds(new Question().text("First question")
                .addAnswersItem(new Answer().text("Incorrect answer").correct(false))
                .addAnswersItem(new Answer().text("Correct answer").correct(true)));

        Question question2 = QuestionUtil.generateIds(new Question().text("Second question")
                .addAnswersItem(new Answer().text("Incorrect answer").correct(false))
                .addAnswersItem(new Answer().text("Correct answer").correct(true)));

        Quiz quiz = new Quiz().addQuestionsItem(question1).addQuestionsItem(question2);

        QuizGame quizGame = new QuizGame().quiz(quiz);
        quizGame = QuizGameUtil.setQuizGameStarted(quizGame);

        Question actualQuestion = quizGame.getActualQuestion();
        assertEquals(question1.getId(), actualQuestion.getId());

        Question nextQuestion = QuestionUtil.getNextQuestion(quizGame, actualQuestion);
        quizGame.setActualQuestion(nextQuestion);
        assertEquals(question2.getId(), nextQuestion.getId());

        assertNull(QuestionUtil.getNextQuestion(quizGame, nextQuestion));
    }

}
