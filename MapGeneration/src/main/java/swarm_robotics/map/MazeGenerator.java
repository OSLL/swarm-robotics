package swarm_robotics.map;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import static swarm_robotics.map.Type.*;
import static java.lang.Math.abs;

/**
 * Allows to generate maze of given width and height.
 */
public class MazeGenerator {

    private int m_width = 0, m_height = 0;
    private Cell[][] m_maze;

    /**
     * Constructor initializes maze. Adds unvisited cells and walls in sequence.
     * @param width maze width
     * @param height maze height
     */
    public MazeGenerator(int width, int height) {
        this.m_width = width;
        this.m_height = height;

        m_maze = new Cell[height][width];

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                m_maze[i][j] = new Cell(j,i);

                //if cell's x and y are odd and the cell is inside maze border
                if((i % 2 != 0  && j % 2 != 0) && (i < height-1 && j < width-1))
                    //then it is an unvisited cell
                    m_maze[i][j].setType(UNVISITED);
                //otherwise it is a wall
                else m_maze[i][j].setType(WALL);
            }
        }
    }

    /**
     * Allows to add net if needed.
     * @param netStep
     */
    public void addNet(int netStep){

        int i = 0;
        while(i < m_height){
            for (int j = 0; j < m_width; j ++)
                m_maze[i][j].setType(UNVISITED);
            i+=netStep;
        }

        i = 0;
        while(i < m_width){
            for (int j = 0; j < m_height; j ++)
                m_maze[j][i].setType(UNVISITED);
            i+=netStep;
        }

    }

    /**
     * Allows to print maze to console using pseudo-graphics.
     */
    public void drawMaze(){
        for(int i = 0; i < m_height; i++) {
            for (int j = 0; j < m_width; j++) {
                if (m_maze[i][j].getType() == WALL)
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
        m_maze[1][1].setType(VISITED);
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

                //pick random neighbour
                neighbourCell = unvisitedNeighbourCells.get(index);
                currentCell.setType(VISITED);

                //push current cell to stack
                stack.push(currentCell);
                removeWall(currentCell, neighbourCell);
                currentCell = neighbourCell;
            } else //if no neighbours, back to previous cell
            {
                if (currentCell.getType() == UNVISITED)
                    currentCell.setType(VISITED);
                if (stack.size() > 0)
                    currentCell = stack.pop();
                else //if no neighbours and no cells in stack, but not all the cells are visited, pick random unvisited cell
                {
                    ArrayList<Cell> unvisitedCells = getUnvisitedCells();
                    int index = random.nextInt(unvisitedCells.size() - 1);
                    currentCell = unvisitedCells.get(index);
                }
            }
        }while(getUnvisitedCells().size() > 0);
        return m_maze;
    }

    /**
     * @return list of unvisited cells in general
     */
    private ArrayList<Cell> getUnvisitedCells(){
        ArrayList<Cell> unvisitedCells = new ArrayList<>();
        for(int i = 0; i < m_height; i++){
            for(int j = 0; j < m_width; j++){
                if((m_maze[i][j].getType() != WALL) &&(m_maze[i][j].getType() != VISITED)){
                    unvisitedCells.add(m_maze[i][j]);
                }
            }
        }
        return unvisitedCells;
    }

    /**
     * @param cell the cell whose unvisited neighbours we are looking for
     * @return list of unvisited neighbour cells
     */
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
        for(int i = 0; i < 4; i++){ //for each direction
            //if in maze borders
            if(neighbours[i].getX() > 0 && neighbours[i].getX() < m_width && neighbours[i].getY() > 0 && neighbours[i].getY() < m_height){
                Cell mazeCellCurrent = m_maze[neighbours[i].getY()][neighbours[i].getX()];
                //and not wall or unvisited cell
                if(mazeCellCurrent.getType() != WALL && mazeCellCurrent.getType() != VISITED){
                    //add to list
                    unvisitedNeighbourCells.add(mazeCellCurrent) ;
                }
            }
        }
        return unvisitedNeighbourCells;
    }

    /**
     * Allows to remove wall between firstCell and secondCell
     * @param firstCell
     * @param secondCell
     */
    private void removeWall( Cell firstCell,  Cell secondCell){
        int xDiff = secondCell.getX() - firstCell.getX();
        int yDiff = secondCell.getY() - firstCell.getY();
        int addX = (xDiff != 0) ? (xDiff / abs(xDiff)) : 0;
        int  addY = (yDiff != 0) ? (yDiff / abs(yDiff)) : 0;
        int x = firstCell.getX() + addX;
        int y = firstCell.getY() + addY;
        m_maze[y][x].setType(VISITED);
    }

}
