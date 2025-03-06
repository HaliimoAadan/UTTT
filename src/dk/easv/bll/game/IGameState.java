package dk.easv.bll.game;

import dk.easv.bll.field.IField;
import dk.easv.bll.move.IMove;

import java.util.List;

/**
 *
 * @author mjl
 */
public interface IGameState {

    IField getField();

    int getMoveNumber();

    void setMoveNumber(int moveNumber);

    int getRoundNumber();

    void setRoundNumber(int roundNumber);

    int getTimePerMove();

    void setTimePerMove(int milliSeconds);
}