package hu.zol1e.quizapp.quizgame;

import hu.zol1e.quizapp.model.Player;
import hu.zol1e.quizapp.model.Question;
import hu.zol1e.quizapp.model.Quiz;
import hu.zol1e.quizapp.model.QuizGame;

import java.util.List;
import java.util.UUID;

public class QuestionUtil {

    public static Question generateIds(Question question) {
        String questionId = UUID.randomUUID().toString();
        question.setId(questionId);
        question.getAnswers().forEach(a -> a.id(UUID.randomUUID().toString()).questionId(questionId));

        return question;
    }

    public static Question getNextQuestion(QuizGame quizGame, Question actualQuestion) {
        Integer questionIndex = getQuestionIndex(quizGame, actualQuestion);
        if(questionIndex == null) {
            throw new IllegalStateException("The actual question does not exists in the quiz!");
        }
        return getNextQuestion(quizGame, questionIndex);
    }

    public static Question getNextQuestion(QuizGame quizGame, Integer questionIndex) {
        List<Question> questions = quizGame.getQuiz().getQuestions();
        if(questions.size() - 1 <= questionIndex) {
            return null;
        } else {
            return questions.get(questionIndex + 1);
        }
    }

    public static Integer getQuestionIndex(QuizGame quizGame, Question actualQuestion) {
        int index = 0;
        for (Question question : quizGame.getQuiz().getQuestions()) {
            if(question.getId().equals(actualQuestion.getId())) {
                return index;
            }
            index++;
        }
        return null;
    }

    public static void checkUniquePlayerName(Player player, List<Player> players) {
        boolean hasPlayerWithName = players.stream().anyMatch(p -> player.getName().equals(p.getName()));
        if(hasPlayerWithName) {
            throw new IllegalArgumentException("Name already exists: " + player.getName());
        }
    }

}
