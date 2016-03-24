package com.emb.tictactoe.control;

/**
 *
 * @author bu_000
 */
import com.emb.tictactoe.util.LOG;
import com.emb.tictactoe.util.JsfUtil;
import com.emb.tictactoe.entity.Cell;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author bu_000
 */
@ViewScoped
@Named("game")
public class Game implements Serializable {

    private TicTacToe ticTacToe;
    private String player;
    private List<Cell> screen;
    private List<Cell> tempList;
    private String state;

    @PostConstruct
    public void init() {
        player = null;
        state = null;
        setupCells();
        ticTacToe = new TicTacToe();
        ticTacToe.resetScreen();
    }

    private void reset() {
        ticTacToe.resetScreen();
        player = null;
        setupCells();
    }

    public void start() {
        reset();
        state = JsfUtil.PLAYING;
        player = JsfUtil.getRequestParameter("player");
        if (JsfUtil.Empty(player)) {
            JsfUtil.addWarningMessage("!Sorry Player not found ");
            return;
        }

        if (!player.equals("X") && !player.equals("O")) {
            JsfUtil.addWarningMessage(
                    "Sorry! Invalid Player Found. Player must be X or O");
        } else {
            ticTacToe.setFirstPlayer(player);
            if (player.equals("X")) {
                playForSystem();
            }
        }
    }

    private void playForSystem() {
        if (state != null && state.equals(JsfUtil.WIN)) {
            return;
        }
        Cell cell = getRandomCellFromScreen(tempList);
        markCell(cell);
    }

    private void markCell(Cell cell) {
        if (cell.getMark() != null || state != null && state.equals(JsfUtil.WIN)) {
            //JsfUtil.addErrorMessage("Cell already Marked");
        } else {
            if (!ticTacToe.cellIsNotAvailable()) {
                if (ticTacToe.appendMark(cell.getRow(), cell.getCol())) {
                    StringBuilder sb = new StringBuilder();
                    //LOG.info("Current Player", player);
                    cell.setMark(sb.append(ticTacToe.getPlayer())
                            .append(".png").toString());
                    player = ticTacToe.switchPlayer();
                    //LOG.info("Next Player", player);
                }
            }
            updateScreen(cell);
        }
    }

    public void play() {
        if (player == null) {
            JsfUtil.addErrorMessage("Please Specify the first Player");
            return;
        }

        Cell cell = findCellById(getCellId("cellId"));
        if (isAvailable(cell)) {
            markCell(cell);
            if (!state.equals(JsfUtil.WIN) || !state.equals(JsfUtil.DRAW)) {
                playForSystem();
            }

        }
    }

    private void updateScreen(Cell cell) {
        for (Iterator it = screen.iterator(); it.hasNext();) {
            Cell currentCell = (Cell) it.next();
            if (currentCell.equals(cell)) {
                int cellIndex = screen.indexOf(currentCell);
                tempList.remove(currentCell);
                screen.remove(currentCell);
                screen.add(cellIndex, cell);
                break;
            }
        }

        if (ticTacToe.isGameOver()) {
            state = JsfUtil.WIN;
            LOG.info("state", state);
            announceTheWinner();
            ticTacToe.displayScreen();
            return;
        } else if (!ticTacToe.isGameOver() && ticTacToe.cellIsNotAvailable()) {
            state = JsfUtil.DRAW;
            LOG.info("state", state);
        } else {
            state = JsfUtil.PLAYING;
        }
        ticTacToe.displayScreen();
    }

    private void announceTheWinner() {
        //if diagonal Marks Are Equal 1,1 must be in a cell of the winner
        if (ticTacToe.diagonalMarksAreEqual()) {
            announceWinner();
            return;
        }
        if (ticTacToe.rowMarksAreEqual()) {
            //if row Marks Are Equal 0,1, 1,1 and 2,1 must be cells of the winner
            announceWinner(new Cell(0, 1), new Cell(0, 1), new Cell(0, 1));
            return;
        }
        if (ticTacToe.columnMarksAreEqual()) {
            //if column Marks Are Equal 1,0, 1,1 and 1,2 must be in cells of the winner
            announceWinner(new Cell(1, 0), new Cell(1, 1), new Cell(1, 2));
        }
    }

    private Cell findCellById(Integer id) {
        for (Cell cell : tempList) {
            if (cell.getId().equals(id)) {
                return cell;
            }
        }
        return null;
    }

    private int getCellId(String param) throws NumberFormatException {
        String id = JsfUtil.getRequestParameter(param);
        if (JsfUtil.Empty(id)) {
            return 0;
        } else {
            return Integer.parseInt(id);
        }
    }

    //TODO implement algorithm for efficient/winning system play here
    public Cell getRandomCellFromScreen(List<Cell> list) {
        Cell cell;
        int index = 0;
        int count = 0;
        index = ThreadLocalRandom.current().nextInt(list.size());
        cell = list.get(index);
        LOG.info("Entering y loop with index ", index);
        LOG.info("ticTacToe.cellIsNotAvailable()", ticTacToe.cellIsNotAvailable());
        while (!isAvailable(cell) && ticTacToe.cellIsNotAvailable()
                && count < 50) {
            LOG.info("Index " + index, " is not available");
            count++;
            index = ThreadLocalRandom.current().nextInt(list.size());
        }
        LOG.info("Index " + index, " on loop exit");
        return cell;
    }

    private boolean isAvailable(Cell cell) {
        return cell != null && cell.getMark() == null;
    }

    private void announceWinner(Cell cell1, Cell cell2, Cell cell3) {
        Cell cell1_ = findCellByRowAndColumn(cell1);
        Cell cell2_ = findCellByRowAndColumn(cell2);
        Cell cell3_ = findCellByRowAndColumn(cell2);
        if (cell1_ != null && cell2_ != null && cell3_ != null) {

            if (cell1_.getMark() != null && cell1_.getMark().equals(cell2_.getMark())) {
                if (cell1_.getMark().equals("X")) {
                    JsfUtil.addSuccessMessage("GAME OVER!!! SYSTEM PLAYER WON");
                } else {
                    JsfUtil.addSuccessMessage("GAME OVER!!! YOU WON");
                }

            } else if (cell1_.getMark() != null && cell1_.getMark().equals(cell3_.getMark())) {
                if (cell1_.getMark().equals("X")) {
                    JsfUtil.addSuccessMessage("GAME OVER!!! SYSTEM PLAYER WON");
                } else {
                    JsfUtil.addSuccessMessage("GAME OVER!!! YOU WON");
                    return;
                }
            } else if (cell2_.getMark() != null && cell2_.getMark().equals(cell3_.getMark())) {
                if (cell2_.getMark().equals("X")) {
                    JsfUtil.addSuccessMessage("GAME OVER!!! SYSTEM PLAYER WON");
                } else {
                    JsfUtil.addSuccessMessage("GAME OVER!!! YOU WON");
                }
            }
        } else {
            JsfUtil.addSuccessMessage("GAME OVER!");
        }

    }

    private Cell findCellByRowAndColumn(Cell cell) {
        for (Cell currentCell : screen) {
            if (currentCell.equals(cell, "")) {
                return currentCell;
            }
        }
        return null;
    }

    private void announceWinner() {
        //if diagonal is equal 1,1 must be of a char of the winner
        Cell winnerCell = findCellById(5);
        if (winnerCell.getMark().equals("X")) {
            JsfUtil.addSuccessMessage("GAME OVER!!! SYSTEM PLAYER WON");
        } else {
            JsfUtil.addSuccessMessage("GAME OVER!!! YOU WON");
        }
    }

    private List<Cell> setupCells() {
        tempList = new ArrayList<>();
        screen = new ArrayList<>();
        screen.add(new Cell(1, 0, 0, null));
        screen.add(new Cell(2, 0, 1, null));
        screen.add(new Cell(3, 0, 2, null));
        screen.add(new Cell(4, 1, 0, null));
        screen.add(new Cell(5, 1, 1, null));
        screen.add(new Cell(6, 1, 2, null));
        screen.add(new Cell(7, 2, 0, null));
        screen.add(new Cell(8, 2, 1, null));
        screen.add(new Cell(9, 2, 2, null));
        tempList.addAll(screen);
        return screen;
    }

    public String getPlayer() {
        player = ticTacToe.getPlayer();
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getState() {
        if (state == null) {
            state = JsfUtil.START;
        }
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Cell> getScreen() {
        return screen;
    }

    public void setScreen(List<Cell> screen) {
        this.screen = screen;
    }

}
