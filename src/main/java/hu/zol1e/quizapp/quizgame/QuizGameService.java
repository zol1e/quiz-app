package hu.zol1e.quizapp.quizgame;

import com.google.common.base.Strings;
import hu.zol1e.quizapp.model.*;
import hu.zol1e.quizapp.notify.StatusNotifier;
import hu.zol1e.quizapp.player.PlayerAnswerRepo;
import hu.zol1e.quizapp.player.PlayerRepo;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static hu.zol1e.quizapp.quizgame.QuestionUtil.checkUniquePlayerName;
import static hu.zol1e.quizapp.quizgame.QuizGameUtil.*;

@ApplicationScoped
public class QuizGameService {

    @Inject
    QuizGameRepo gameRepo;

    @Inject
    PlayerRepo playerRepo;

    @Inject
    PlayerAnswerRepo playerAnswerRepo;

    @Inject
    Instance<StatusNotifier> notifier;

    public QuizGame createQuizGame(Quiz quiz) {
        QuizGame quizGame = new QuizGame()
                .id(UUID.randomUUID().toString())
                .state(QuizGame.StateEnum.CREATED)
                .quiz(quiz);

        return storeAndNotify(quizGame);
    }

    public QuizGame startQuiz(String quizId) {
        QuizGame quizGame = gameRepo.findByIdOptional(quizId).orElseThrow();

        quizGame = setQuizGameStarted(quizGame);

        return storeAndNotify(quizGame);
    }

    public QuizGame nextQuestion(String quizId) {
        QuizGame quizGame = gameRepo.findByIdOptional(quizId).orElseThrow();
        quizGame = setNextQuestion(quizGame);

        return storeAndNotify(quizGame);
    }

    public QuizGame finishQuestion(String quizId, String questionId) {
        QuizGame quizGame = gameRepo.findByIdOptional(quizId)
                .orElseThrow()
                .actualQuestion(null)
                .state(QuizGame.StateEnum.WAITING);

        return storeAndNotify(quizGame);
    }

    public Player join(String gameId, Player player) {
        if (!Strings.isNullOrEmpty(player.getId())) {
            throw new IllegalArgumentException("Only new player can be added to the game! ID is not empty: " + player.getId());
        }
        if (Strings.isNullOrEmpty(player.getName())) {
            throw new IllegalArgumentException("Player name is empty!");
        }

        List<Player> players = playerRepo.list(Player.JSON_PROPERTY_GAME_ID, gameId);
        checkUniquePlayerName(player, players);

        player.id(UUID.randomUUID().toString()).gameId(gameId);

        playerRepo.persist(player);
        return player;
    }

    public PlayerAnswer answer(String playerId, String gameId, Answer answer) {
        if (!isValidAnswer(gameId, answer)) {
            throw new IllegalStateException("Cannot answer question, which is not the actual one." +
                    " Answer: " + answer +
                    " Game ID: " + gameId);
        }

        String questionId = answer.getQuestionId();

        if (queryPlayerAnswer(playerId, gameId, questionId).isPresent()) {
            throw new IllegalStateException("Question is already answered. Question ID: " + questionId);
        }

        PlayerAnswer playerAnswer = new PlayerAnswer()
                .playerId(playerId)
                .gameId(gameId)
                .questionId(questionId)
                .answer(answer);

        playerAnswerRepo.persist(playerAnswer);
        return playerAnswer;
    }

    public Optional<PlayerAnswer> queryPlayerAnswer(String playerId, String gameId, String questionId) {
        return playerAnswerRepo.find(
                        "playerId = :playerId and gameId = :gameId and questionId = :questionId",
                        Parameters.with(
                                PlayerAnswer.JSON_PROPERTY_PLAYER_ID, playerId).and(
                                PlayerAnswer.JSON_PROPERTY_GAME_ID, gameId).and(
                                PlayerAnswer.JSON_PROPERTY_QUESTION_ID, questionId))
                .firstResultOptional();
    }

    public List<PlayerAnswer> queryPlayerAnswers(String playerId, String gameId) {
        return playerAnswerRepo.find(
                "playerId = :playerId and gameId = :gameId and questionId = :questionId",
                Parameters.with(
                        PlayerAnswer.JSON_PROPERTY_PLAYER_ID, playerId).and(
                        PlayerAnswer.JSON_PROPERTY_GAME_ID, gameId)).list();
    }

    private QuizGame storeAndNotify(QuizGame quizGame) {
        gameRepo.persistOrUpdate(quizGame);

        StatusNotifier statusNotifier = notifier.get();
        statusNotifier.notifyGameChanged(quizGame);

        return quizGame;
    }

    private boolean isValidAnswer(String gameId, Answer answer) {
        QuizGame quizGame = gameRepo.findByIdOptional(gameId).orElseThrow();
        Question actualQuestion = quizGame.getActualQuestion();

        return QuizGameUtil.isValidAnswer(answer, actualQuestion);
    }

}
