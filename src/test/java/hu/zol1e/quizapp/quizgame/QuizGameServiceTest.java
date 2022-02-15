package hu.zol1e.quizapp.quizgame;

import hu.zol1e.quizapp.model.*;
import hu.zol1e.quizapp.notify.StatusNotifier;
import hu.zol1e.quizapp.player.PlayerRepo;
import hu.zol1e.quizapp.quiz.QuizRepo;
import hu.zol1e.quizapp.quiz.QuizService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class QuizGameServiceTest {

    @Inject
    QuizGameService gameService;

    @Inject
    QuizService quizService;

    @Inject
    QuizGameRepo gameRepo;

    @Inject
    QuizRepo quizRepo;

    @Inject
    PlayerRepo playerRepo;

    @ApplicationScoped
    public static class StatusNotifierMock implements StatusNotifier {
        @Override
        public void notifyGameChanged(QuizGame quizGame) {
        }
    }

    @Test
    public void simpleQuizTest() {
        Quiz testQuiz = quizService.createQuiz("Test quiz");
        String quizId = testQuiz.getId();

        assertEquals(0, quizRepo.findById(quizId).getQuestions().size());
        quizService.addQuestion(quizId, new Question().text("First question")
                .addAnswersItem(new Answer().text("Incorrect answer").correct(false))
                .addAnswersItem(new Answer().text("Correct answer").correct(true)));

        testQuiz = quizRepo.findById(quizId);
        assertEquals(1, testQuiz.getQuestions().size());

        quizService.addQuestion(quizId, new Question().text("Second question")
                .addAnswersItem(new Answer().text("Incorrect answer").correct(false))
                .addAnswersItem(new Answer().text("Correct answer").correct(true)));

        testQuiz = quizRepo.findById(quizId);
        assertEquals(2, testQuiz.getQuestions().size());

        QuizGame quizGame = gameService.createQuizGame(testQuiz);
        String gameId = quizGame.getId();
        assertEquals("Test quiz", quizGame.getQuiz().getName());

        assertEquals(0, playerRepo.list(Player.JSON_PROPERTY_GAME_ID, gameId).size());

        Player player1 = new Player().name("Player1");
        player1 = gameService.join(gameId, player1);
        assertEquals(1, playerRepo.list(Player.JSON_PROPERTY_GAME_ID, gameId).size());

        Player player2 = new Player().name("Player2");
        player2 = gameService.join(gameId, player2);
        assertEquals(2, playerRepo.list(Player.JSON_PROPERTY_GAME_ID, gameId).size());

        gameService.startQuiz(gameId);
        assertEquals(QuizGame.StateEnum.QUESTION, gameRepo.findById(gameId).getState());

        Answer answer = testQuiz.getQuestions().get(0).getAnswers().get(0);
        String player1Id = player1.getId();

        assertNotNull(gameService.answer(player1Id, gameId, answer));
        assertThrows(IllegalStateException.class,
                () -> gameService.answer(player1Id, gameId, answer));

        gameService.nextQuestion(gameId);
        assertEquals(QuizGame.StateEnum.QUESTION, gameRepo.findById(gameId).getState());

        gameService.nextQuestion(gameId);
        assertEquals(QuizGame.StateEnum.FINISHED, gameRepo.findById(gameId).getState());
    }

}
