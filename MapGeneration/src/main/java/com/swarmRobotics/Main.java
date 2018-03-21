package com.swarmRobotics;

public class Main {

    public static void main(String[] args) {
        int width = 39, height = 39;
        MazeGenerator generator = new MazeGenerator(width,height);
        Cell[][] maze = generator.generateMaze();
        ShapefileMaker shapefileMaker = new ShapefileMaker();
        try {
            shapefileMaker.makeShapefile(maze, width, height);
        }catch (Exception e){}
    }
}