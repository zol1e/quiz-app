package hu.zol1e.quizapp.timer;

import hu.zol1e.quizapp.quizgame.QuizGameService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SimpleQuestionTimer implements QuestionTimer {

    private static final Map<String, QuestionTimerRecord> records = new ConcurrentHashMap<>();

    @Inject
    private QuizGameService gameService;

    @Scheduled(every="1s")
    void checkTimeouts() {
        LocalDateTime now = LocalDateTime.now();
        for (QuestionTimerRecord record : records.values()) {
            if(now.isAfter(record.getTime())) {
                gameService.finishQuestion(record.getGameId(), record.getQuestionId());
                records.remove(record.getGameId());
            }
        }
    }

    @Override
    public void setTimer(String gameId, String questionId, Duration time) {
        records.put(gameId, new QuestionTimerRecord(gameId, questionId, LocalDateTime.now().plus(time)));
    }

}
