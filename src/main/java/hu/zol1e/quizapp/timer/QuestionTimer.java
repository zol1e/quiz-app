package hu.zol1e.quizapp.timer;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

public interface QuestionTimer {

    public void setTimer(String gameId, String questionId, Duration time);

}
