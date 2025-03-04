package dk.easv.bll.bot;

import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;

public class RookieBot implements IBot {
    private static final String BOTNAME = "Rookie Bot";

    @Override
    public IMove doMove(IGameState state) {
        return null;
    }

    @Override
    public String getBotName() {
        return "Rookie Bot";
    }
}

