package hu.zol1e.quizapp.notify;

import hu.zol1e.quizapp.model.QuizGame;

public interface StatusNotifier {

    public void notifyGameChanged(QuizGame quizGame);

}
