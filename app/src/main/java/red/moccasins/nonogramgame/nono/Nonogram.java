package red.moccasins.nonogramgame.nono;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Автор: Левшин Николай, 707 группа.
 * Дата создания: 02.11.2015.
 */


public class Nonogram {

    public String Name;

    private int width;
    private int height;

    private int paramsWidth;
    private int paramsHeight;

    private float tableWidth;
    private float tableHeight;

    private float startX;
    private float startY;
    private float cellSize;

    private int tableColor;
    private int selectedCellColor;

    private Paint tablePen;

    private int selectedX;
    private int selectedY;

    private static final float margin = 5.0f;
    private static final float tinyLineSize = 1.0f;
    private static final float boldLineSize = 3.0f;

    // цифорки, которые расположены сверху
    public ArrayList<ArrayList<Integer>> verticalParams;

    // цифорки, которые расположены сбоку
    public ArrayList<ArrayList<Integer>> horizontalParams;

    private ArrayList<ArrayList<Boolean>> solution;

    private SolutionProcess solutionProcess;

    private SolutionProcess temporarySolution;
    private int startMoveX;
    private int startMoveY;

    public Nonogram(int width, int height) {
        initSize(width, height);
    }

    public Nonogram( String task ) {
        String[] parts = task.split("&\r\n");
        String[] vertParams = parts[0].split("\r\n");
        String[] horParams = parts[1].split("\r\n");

        initSize(horParams.length, vertParams.length);

        ArrayList<Integer> nums;

        for (String param : vertParams) {
            nums = new ArrayList<>();
            String[] numbers = param.split(",");
            for (String i : numbers) {
                nums.add(Integer.valueOf(i));
            }
            verticalParams.add(nums);
        }

        for (String param : horParams) {
            nums = new ArrayList<>();
            String[] numbers = param.split(",");
            for (String i : numbers) {
                nums.add(Integer.valueOf(i));
            }
            horizontalParams.add(nums);
        }

        for (ArrayList<Integer> vParams : horizontalParams) {
            if (vParams.size() > paramsHeight) {
                paramsHeight = vParams.size();
            }
        }

        for (ArrayList<Integer> hParams : verticalParams) {
            if (hParams.size() > paramsWidth) {
                paramsWidth = hParams.size();
            }
        }
    }

    private void initSize(int width, int height) {
        this.width = width;
        this.height = height;

        paramsWidth = 0;
        paramsHeight = 0;

        startX = 0;
        startY = 0;

        cellSize = 0;

        selectedX = -1;
        selectedY = -1;

        verticalParams = new ArrayList<>(width);
        horizontalParams = new ArrayList<>(height);

        solutionProcess = new SolutionProcess(width, height);
    }

    public void initializeTable(float measureWidth, float measureHeight, Context context){
        tableWidth = measureWidth;
        tableHeight = measureHeight;
        tableColor = context.getResources().getColor(android.R.color.black);
        selectedCellColor = context.getResources().getColor(android.R.color.darker_gray);

        tablePen = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    public void draw(Canvas canvas) {

        float cellWidth = ( tableWidth - 2 * margin ) / (paramsWidth + width);
        float cellHeight = ( tableHeight - 2 * margin ) / (paramsHeight + height);

        cellSize = Math.min(cellWidth, cellHeight);

        startX = (tableWidth - cellSize * (paramsWidth + width)) / 2;
        startY = (tableHeight - cellSize * (paramsHeight + height)) / 2;

        // Закрашиваем выделенный столбец и строку
        tablePen.setStyle(Paint.Style.FILL);
        if (selectedX >= 0 && selectedY >= 0) {

            tablePen.setStrokeWidth(tinyLineSize);
            tablePen.setColor(selectedCellColor);

            canvas.drawRect(startX + (selectedX + paramsWidth) * cellSize,
                    startY,
                    startX + (selectedX + paramsWidth) * cellSize + cellSize,
                    startY + (paramsHeight + height) * cellSize, tablePen);

            canvas.drawRect(startX,
                    startY + (selectedY + paramsHeight) * cellSize,
                    startX + (paramsWidth + width) * cellSize,
                    startY + (selectedY + paramsHeight) * cellSize + cellSize, tablePen);
        }

        // Рисуем линии
        tablePen.setColor(tableColor);

        // Рисуем горизонтальные линии
        for (int i = 0; i < height + paramsHeight; ++i) {
            if (i >= paramsHeight && (i - paramsHeight) % 5 == 0) {
                tablePen.setStrokeWidth(boldLineSize);
            } else {
                tablePen.setStrokeWidth(tinyLineSize);
            }

            if ( i >= paramsHeight ) {
                canvas.drawLine(startX,
                        startY + i * cellSize,
                        startX + cellSize * (paramsWidth + width),
                        startY + i * cellSize,
                        tablePen);
            } else {
                canvas.drawLine(startX + cellSize * paramsWidth,
                        startY + i * cellSize,
                        startX + cellSize * (paramsWidth + width),
                        startY + i * cellSize,
                        tablePen);
            }

        }

        // Рисуем вертикальные линии
        for (int i = 0; i < width + paramsWidth; ++i) {
            if (i >= paramsWidth && (i - paramsWidth) % 5 == 0) {
                tablePen.setStrokeWidth(boldLineSize);
            } else {
                tablePen.setStrokeWidth(tinyLineSize);
            }

            if ( i >= paramsWidth ) {
                canvas.drawLine(startX + i * cellSize,
                        startY,
                        startX + i * cellSize,
                        startY + cellSize * (paramsHeight + height),
                        tablePen);
            } else {
                canvas.drawLine(startX + i * cellSize,
                        startY + cellSize * paramsHeight,
                        startX + i * cellSize,
                        startY + cellSize * (paramsHeight + height),
                        tablePen);
            }
        }


        // Рисуем завершающую рамку
        tablePen.setStrokeWidth(boldLineSize);
        tablePen.setStyle(Paint.Style.STROKE);

        canvas.drawRect(startX,
                startY,
                startX + cellSize * (paramsWidth + width),
                startY + cellSize * (paramsHeight + height),
                tablePen);

        // Рисуем цифры
        tablePen.setStrokeWidth(0.8f);
        tablePen.setTextSize(cellSize * 0.6f);
        float textMiddle = tablePen.descent() + tablePen.ascent();

        // По вертикали
        for (int i = 0; i < height; ++i) {
            ArrayList<Integer> params = verticalParams.get(i);
            int offset = paramsWidth - params.size();
            for ( int j = 0; j < params.size(); ++j ) {
                String text = String.valueOf(params.get(j));
                canvas.drawText(text,
                        startX + (j + offset) * cellSize + (cellSize - tablePen.measureText(text)) / 2,
                        startY + (i + paramsHeight) * cellSize + (cellSize - textMiddle) / 2,
                        tablePen);
            }
        }

        // По горизонтали
        // По вертикали
        for (int i = 0; i < width; ++i) {
            ArrayList<Integer> params = horizontalParams.get(i);
            int offset = paramsHeight - params.size();
            for ( int j = 0; j < params.size(); ++j ) {
                String text = String.valueOf(params.get(j));
                canvas.drawText(text,
                        startX + (i + paramsWidth) * cellSize + (cellSize - tablePen.measureText(text)) / 2,
                        startY + (j + offset) * cellSize + (cellSize - textMiddle) / 2,
                        tablePen);
            }
        }


        // Закрашиваем клетки согласно решению
        tablePen.setColor(tableColor);
        tablePen.setStyle(Paint.Style.FILL);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                switch (solutionProcess.GetCell(i,j)) {
                    case 0:
                        break;
                    case 1:
                        canvas.drawRect(startX + (i + paramsWidth) * cellSize,
                                startY + (j + paramsHeight) * cellSize,
                                startX + (i + paramsWidth) * cellSize + cellSize,
                                startY + (j + paramsHeight) * cellSize + cellSize,
                                tablePen);
                        break;
                    case 2:
                        canvas.drawLine(startX + (i + paramsWidth) * cellSize,
                                startY + (j + paramsHeight) * cellSize,
                                startX + (i + paramsWidth) * cellSize + cellSize,
                                startY + (j + paramsHeight) * cellSize + cellSize,
                                tablePen);
                        canvas.drawLine(startX + (i + paramsWidth) * cellSize + cellSize,
                                startY + (j + paramsHeight) * cellSize,
                                startX + (i + paramsWidth) * cellSize,
                                startY + (j + paramsHeight) * cellSize + cellSize,
                                tablePen);
                        break;
                }
            }
        }
        


    }

    public boolean StartSelectCell(float x, float y, int type) {
        boolean result = updateSelected(x, y);
        if (result) {
            StartMove(selectedX, selectedY);
        }
        return result;
    }

    public boolean KeepSelectCell(float x, float y, int type) {
        boolean result = updateSelected(x, y);
        if (result) {
            solutionProcess.FillSolution( temporarySolution );
            solutionProcess.AddMove(startMoveX, startMoveY,
                    selectedX, selectedY, type, false);
        }
        return result;
    }

    public boolean EndSelectCell(float x, float y, int type) {
        boolean result = updateSelected(x, y);
        if (result) {
            endMove(selectedX, selectedY, type);
        }
        return result;
    }

    private boolean updateSelected(float x, float y) {
        int tmpSelectedX = (int)((x - startX) / cellSize) - paramsWidth;
        int tmpSelectedY = (int)((y - startY) / cellSize) - paramsHeight;
        if (tmpSelectedX < 0 || tmpSelectedX >= width
                || tmpSelectedY  < 0 || tmpSelectedY >= height) {
            return false;
        } else {
            selectedX = tmpSelectedX;
            selectedY = tmpSelectedY;
            return true;
        }
    }

    private void StartMove(int x, int y) {
        temporarySolution = new SolutionProcess(solutionProcess);
        startMoveX = x;
        startMoveY = y;
    }

    private void endMove(int x, int y, int type) {
        solutionProcess.FillSolution( temporarySolution );
        solutionProcess.AddMove(startMoveX, startMoveY, x, y, type, true);
        startMoveX = -1;
        startMoveY = -1;
    }

    public void Undo() {
        solutionProcess.Undo();
    }

    public void  Redo() {
        solutionProcess.Redo();
    }

}
