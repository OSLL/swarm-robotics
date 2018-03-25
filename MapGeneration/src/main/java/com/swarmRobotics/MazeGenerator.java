package com.swarmRobotics;



import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static com.swarmRobotics.Type.*;
import static java.lang.Math.abs;

public class MazeGenerator {
    private int width = 0, height = 0;
    private Cell[][] maze;

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        maze = new Cell[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                maze[i][j] = new Cell(j,i);//запишем координаты ячейки
                if((i % 2 != 0  && j % 2 != 0) && //если ячейка нечетная по x и y,
                        (i < height-1 && j < width-1))   //и при этом находится в пределах стен лабиринта
                    maze[i][j].setType(UNVISITED);       //то это КЛЕТКА(непосещенная)
                else maze[i][j].setType(WALL);           //в остальных случаях это СТЕНА.
            }
        }
    }

    public void drawMaze(){
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j].getType() == WALL)
                    System.out.print("▓");
                else System.out.print("░");
            }
            System.out.println();
        }
        System.out.println();
    }
    public Cell[][] generateMaze(){
        Random random = new Random();
        Stack<Cell> stack = new Stack<>();
        Cell startCell = new Cell(1,1);
        maze[1][1].setType(VISITED);
        startCell.setType(VISITED);
        Cell currentCell = startCell;
        Cell neighbourCell;
        do {
            ArrayList<Cell> unvisitedNeighbourCells = getUnvisitedNeighbours(currentCell);
            if ((unvisitedNeighbourCells != null) && (!unvisitedNeighbourCells.isEmpty())) {
                int index;
                int rnd;
                if (unvisitedNeighbourCells.size() > 1){
                    rnd = random.nextInt(unvisitedNeighbourCells.size());
                    index = (rnd == unvisitedNeighbourCells.size())?unvisitedNeighbourCells.size()-1:rnd;}
                else index = 0;
                neighbourCell = unvisitedNeighbourCells.get(index); //выбираем случайного соседа
                currentCell.setType(VISITED);
                stack.push(currentCell); //заносим текущую точку в стек
                removeWall(currentCell, neighbourCell);
                currentCell = neighbourCell;
            } else //если нет соседей, возвращаемся на предыдущую точку
            {
                if (currentCell.getType() == UNVISITED)
                    currentCell.setType(VISITED);
                if (stack.size() > 0)
                    currentCell = stack.pop();
                else //если нет соседей и точек в стеке, но не все точки посещены, выбираем случайную из непосещенных
                {
                    ArrayList<Cell> unvisitedCells = getUnvisitedCells();
                    int index = random.nextInt(unvisitedCells.size() - 1);
                    currentCell = unvisitedCells.get(index);
                }
            }
        }while(getUnvisitedCells().size() > 0);
        return maze;
    }

    private ArrayList<Cell> getUnvisitedCells(){
        ArrayList<Cell> unvisitedCells = new ArrayList<>();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if((maze[i][j].getType() != WALL) &&(maze[i][j].getType() != VISITED)){
                    unvisitedCells.add(maze[i][j]);
                }
            }
        }
        return unvisitedCells;
    }

    private ArrayList<Cell> getUnvisitedNeighbours( Cell cell){
        ArrayList<Cell> unvisitedNeighbourCells = new ArrayList<>();
        int distance = 2;
        int x = cell.getX();
        int y = cell.getY();
        Cell up = new Cell(x, y - distance);
        Cell rt = new Cell(x + distance, y);
        Cell dw = new Cell(x, y + distance);
        Cell lt = new Cell(x - distance, y);
        Cell[] neighbours = {dw, rt, up, lt};
        for(int i = 0; i < 4; i++){ //для каждого направдения
            if(neighbours[i].getX() > 0 && neighbours[i].getX() < width && neighbours[i].getY() > 0 && neighbours[i].getY() < height){ //если не выходит за границы лабиринта
                Cell mazeCellCurrent = maze[neighbours[i].getY()][neighbours[i].getX()];
                if(mazeCellCurrent.getType() != WALL && mazeCellCurrent.getType() != VISITED){ //и не посещена\является стеной
                    unvisitedNeighbourCells.add(mazeCellCurrent) ; //записать в массив;
                }
            }
        }
        return unvisitedNeighbourCells;
    }

    private void removeWall( Cell firstCell,  Cell secondCell){
        int xDiff = secondCell.getX() - firstCell.getX();
        int yDiff = secondCell.getY() - firstCell.getY();
        int addX = (xDiff != 0) ? (xDiff / abs(xDiff)) : 0;
        int  addY = (yDiff != 0) ? (yDiff / abs(yDiff)) : 0;
        int x = firstCell.getX() + addX; //координаты стенки
        int y = firstCell.getY() + addY;
        maze[y][x].setType(VISITED);
    }

}
