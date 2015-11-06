package red.moccasins.nonogramgame.nono;

import java.util.ArrayList;

/**
 * Автор: Левшин Николай, 707 группа.
 * Дата создания: 03.11.2015.
 */
public class SolutionProcess {
    private int width;
    private int height;

    // 0 - пусто
    // 1 - закрашено
    // 2 - крестик
    private int[][] currentSolution;
    private ArrayList<int[][]> solutionHistory;
    private int currentStepInHistory = 0;

    public SolutionProcess( int width, int height ) {
        solutionHistory = new ArrayList<>();
        currentSolution = new int[width][];

        for (int i = 0; i < width; ++i) {
            currentSolution[i] = new int[height];
        }

        this.width = width;
        this.height = height;

        solutionHistory.add(copy(currentSolution));
    }

    public SolutionProcess( SolutionProcess sample ) {
        this.height = sample.GetHeight();
        this.width = sample.GetWidth();

        solutionHistory = new ArrayList<>();
        currentSolution = new int[this.width][];
        for ( int i = 0; i < this.width; i++ ) {
            currentSolution[i] = new int[height];
            for ( int j = 0; j < this.height; ++j ) {
                currentSolution[i][j] = sample.GetCell(i,j);
            }
        }

        solutionHistory.add(copy(currentSolution));
    }

    public void AddMove(int startX, int startY, int endX, int endY, int type, boolean save) {

        if (startX == endX) {
            int minY = Math.min(startY, endY);
            int maxY = Math.max(startY, endY);

            for ( int i = minY; i <= maxY; ++i ) {
                currentSolution[startX][i] = type;
            }
        } else if (startY == endY) {
            int minX = Math.min(startX, endX);
            int maxX = Math.max(startX, endX);

            for ( int i = minX; i <= maxX; ++i ) {
                currentSolution[i][startY] = type;
            }
        } else {
            int divX = endX - startX;
            int divY = endY - startY;

            if ( divY > Math.abs(divX) || divY < -Math.abs(divX) ) {
                int minY = Math.min(startY, endY);
                int maxY = Math.max(startY, endY);

                for ( int i = minY; i <= maxY; ++i ) {
                    currentSolution[startX][i] = type;
                }
            } else {
                int minX = Math.min(startX, endX);
                int maxX = Math.max(startX, endX);

                for ( int i = minX; i <= maxX; ++i ) {
                    currentSolution[i][startY] = type;
                }
            }

        }

        if (save) {
            solutionHistory.add(copy(currentSolution));
            currentStepInHistory = solutionHistory.size() - 1;
        }
    }

    public int GetCell( int i, int j ) {
        return currentSolution[i][j];
    }

    public void FillSolution( SolutionProcess sample ) {
        for ( int i = 0; i < width; ++i ) {
            for ( int j = 0; j < height; ++j ) {
                currentSolution[i][j] = sample.GetCell(i,j);
            }
        }
    }

    public int GetWidth() {
        return width;
    }

    public int GetHeight() {
        return height;
    }

    public void Undo() {
        if (currentStepInHistory == 0) return;
        currentStepInHistory--;
        currentSolution = copy(solutionHistory.get(currentStepInHistory));
    }

    public void Redo() {
        if (currentStepInHistory == solutionHistory.size() - 1) return;
        currentStepInHistory++;
        currentSolution = solutionHistory.get(currentStepInHistory);
    }

    private int[][] copy(int[][] sample) {
        int [][] result = new int[width][height];
        for ( int i = 0 ; i < width; ++i ) {
            for ( int j = 0 ;j < height; ++j ) {
                result[i][j] = sample[i][j];
            }
        }
        return result;
    }


}
