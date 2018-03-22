package com.swarmRobotics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame{

    public Main(){
        super("Map generation");
        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS)); // установка менеджера размещения

        SpinnerModel model1 = new SpinnerNumberModel(19, //initial value
                        5, //min
                        10000, //max
                        1);
        SpinnerModel model2 = new SpinnerNumberModel(19, //initial value
                5, //min
                10000, //max
                1);
        SpinnerModel blockModel = new SpinnerNumberModel(10, //initial value
                        1, //min
                        10000, //max
                        1);  //step
        //-----width-----------
        JPanel widthPanel = new JPanel();
        widthPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,15));
        widthPanel.setLayout(new BoxLayout(widthPanel, BoxLayout.X_AXIS));
        Label widthLabel = new Label("Width: ");
        widthLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        JSpinner widthSpinner = new JSpinner(model1);
        widthSpinner.setMaximumSize(new Dimension(200, 35));
        widthSpinner.setFont(new Font("Dialog", Font.PLAIN, 16));
        widthPanel.add(widthLabel);
        widthPanel.add(widthSpinner);
        c.add(widthPanel);
        //-------height-------------
        JPanel heightPanel = new JPanel();
        heightPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,15));
        heightPanel.setLayout(new BoxLayout(heightPanel, BoxLayout.X_AXIS));
        Label heightLabel = new Label("Height: ");
        heightLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        JSpinner heightSpinner = new JSpinner(model2);
        heightSpinner.setMaximumSize(new Dimension(200, 35));
        heightSpinner.setFont(new Font("Dialog", Font.PLAIN, 16));
        heightPanel.add(heightLabel);
        heightPanel.add(heightSpinner);
        c.add(heightPanel);
        //--------block size--------------
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,15));
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
        Label sizeLabel = new Label("Block size: ");
        sizeLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        JSpinner sizeSpinner = new JSpinner(blockModel);
        sizeSpinner.setMaximumSize(new Dimension(200, 35));
        sizeSpinner.setFont(new Font("Dialog", Font.PLAIN, 16));
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeSpinner);
        c.add(sizePanel);
        //----button--------------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        Button button = new Button("Generate");
        button.setBackground(new Color(0xD7D9D8));
        button.setFont(new Font("Dialog", Font.PLAIN, 16));
        buttonPanel.add(button);

        c.add(buttonPanel);
        setSize(300,230); // задание размеров
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);


        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {   int width = (int)widthSpinner.getValue();
                int height = (int)heightSpinner.getValue();
                int blockSize = (int)sizeSpinner.getValue();
                MazeGenerator generator = new MazeGenerator(width,height);
                ShapefileMaker shapefileMaker = new ShapefileMaker();
                Cell[][] maze = generator.generateMaze();
                try {
                    shapefileMaker.makeShapefile(maze, width, height, blockSize);

                }catch (Exception e){}
            }
        };
        button.addActionListener(listener);
    }

    public static void main(String[] args) {
        new Main();
    }
}