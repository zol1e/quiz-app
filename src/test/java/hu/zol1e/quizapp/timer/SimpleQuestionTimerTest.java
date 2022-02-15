package hu.zol1e.quizapp.timer;

import hu.zol1e.quizapp.player.PlayerRepo;
import hu.zol1e.quizapp.quiz.QuizRepo;
import hu.zol1e.quizapp.quiz.QuizService;
import hu.zol1e.quizapp.quizgame.QuizGameRepo;
import hu.zol1e.quizapp.quizgame.QuizGameService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.Duration;

@QuarkusTest
public class SimpleQuestionTimerTest {

    @Inject
    QuestionTimer questionTimer;

    @InjectMock
    QuizGameService personRepository;

    @Test
    void timoutTest() throws InterruptedException {
        String gameId = "gameId";
        String questionId = "questionId";

        questionTimer.setTimer(gameId, questionId, Duration.ofSeconds(1));

        Thread.sleep(2000);

        Mockito.verify(personRepository, Mockito.times(1)).finishQuestion(gameId, questionId);
        Mockito.verifyNoMoreInteractions(personRepository);
    }

}
