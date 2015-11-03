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
    private ArrayList<ArrayList<Integer>> currentSolution;

    public SolutionProcess( int width, int height ) {
        for (int i = 0; i < height; ++i) {
            currentSolution.add(new ArrayList<Integer>(width));
        }
    }

    public void AddMove(int startX, int startY, int endX, int endY, int type) {
        if (startX == endX) {
            int minY = Math.min(startY, endY);
            int maxY = Math.max(startY, endY);

            for ( int i = minY; i <= maxY; ++i ) {
                currentSolution.get(i).set(startX, type);
            }
        } else if (startY == endY) {
            int minX = Math.min(startX, endX);
            int maxX = Math.max(startX, endX);

            for ( int i = minX; i <= maxX; ++i ) {
                currentSolution.get(startY).set(i, type);
            }
        }
    }
}
