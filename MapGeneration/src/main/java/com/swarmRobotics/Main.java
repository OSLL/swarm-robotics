package com.swarmRobotics;

public class Main {

    public static void main(String[] args) {
//        ShapefileMaker shapefileMaker = new ShapefileMaker();
//        try {
//            shapefileMaker.makeShapefile();
//        }catch (Exception e){}

        MazeGenerator generator = new MazeGenerator(79,39);
        Cell[][] maze = generator.generateMaze();
    }
}