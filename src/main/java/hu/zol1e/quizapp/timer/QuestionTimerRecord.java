package hu.zol1e.quizapp.timer;

import java.time.Duration;
import java.time.LocalDateTime;

public class QuestionTimerRecord {

    private String gameId;
    private String questionId;
    private LocalDateTime time;

    public QuestionTimerRecord(String gameId, String questionId, LocalDateTime time) {
        this.gameId = gameId;
        this.questionId = questionId;
        this.time = time;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}
