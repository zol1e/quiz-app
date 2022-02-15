package hu.zol1e.quizapp.player;

import hu.zol1e.quizapp.model.Player;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerRepo implements PanacheMongoRepositoryBase<Player, String> {
}
