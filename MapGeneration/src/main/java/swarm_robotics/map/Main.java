package swarm_robotics.map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Main extends JFrame{

    public Main(){
        super("Map generation");
        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

        //width switcher with label
        JPanel widthPanel = new JPanel();
        widthPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,15));
        widthPanel.setLayout(new BoxLayout(widthPanel, BoxLayout.X_AXIS));

        Label widthLabel = new Label("Width: ");
        widthLabel.setFont(new Font("Dialog", Font.PLAIN, 16));

        SpinnerModel model1 = new SpinnerNumberModel(19, //initial value
                5, //min
                10000, //max
                1);
        JSpinner widthSpinner = new JSpinner(model1);
        widthSpinner.setMaximumSize(new Dimension(200, 35));
        widthSpinner.setFont(new Font("Dialog", Font.PLAIN, 16));

        widthPanel.add(widthLabel);
        widthPanel.add(widthSpinner);
        c.add(widthPanel);

        //height switcher with label
        JPanel heightPanel = new JPanel();
        heightPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,15));
        heightPanel.setLayout(new BoxLayout(heightPanel, BoxLayout.X_AXIS));

        Label heightLabel = new Label("Height: ");
        heightLabel.setFont(new Font("Dialog", Font.PLAIN, 16));

        SpinnerModel model2 = new SpinnerNumberModel(19, //initial value
                5, //min
                10000, //max
                1);
        JSpinner heightSpinner = new JSpinner(model2);
        heightSpinner.setMaximumSize(new Dimension(200, 35));
        heightSpinner.setFont(new Font("Dialog", Font.PLAIN, 16));

        heightPanel.add(heightLabel);
        heightPanel.add(heightSpinner);
        c.add(heightPanel);

        //block size switcher with label
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,15));
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));

        Label sizeLabel = new Label("Block size: ");
        sizeLabel.setFont(new Font("Dialog", Font.PLAIN, 16));

        SpinnerModel blockModel = new SpinnerNumberModel(10, //initial value
                1, //min
                10000, //max
                1);  //step
        JSpinner sizeSpinner = new JSpinner(blockModel);
        sizeSpinner.setMaximumSize(new Dimension(200, 35));
        sizeSpinner.setFont(new Font("Dialog", Font.PLAIN, 16));

        sizePanel.add(sizeLabel);
        sizePanel.add(sizeSpinner);
        c.add(sizePanel);

        //net parameters
        JPanel netPanel = new JPanel();
        netPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,15));
        netPanel.setLayout(new BoxLayout(netPanel, BoxLayout.X_AXIS));

        JCheckBox netCheckBox = new JCheckBox("Add net ");
        netCheckBox.setFont(new Font("Dialog", Font.PLAIN, 16));

        SpinnerModel netModel = new SpinnerNumberModel(3, //initial value
                3, //min
                10000, //max
                1);  //step
        JSpinner netSpinner = new JSpinner(netModel);
        netSpinner.setEnabled(false);
        netSpinner.setMaximumSize(new Dimension(100, 35));
        netSpinner.setFont(new Font("Dialog", Font.PLAIN, 16));

        netPanel.add(netCheckBox);
        netPanel.add(netSpinner);
        c.add(netPanel);
        //add check listener
        netCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                netSpinner.setEnabled(!netSpinner.isEnabled());
            }
        });

        //button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        Button button = new Button("Generate");
        button.setBackground(new Color(0xD7D9D8));
        button.setFont(new Font("Dialog", Font.PLAIN, 16));
        buttonPanel.add(button);

        c.add(buttonPanel);
        setSize(300,250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        //on button click
        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {   int width = (int)widthSpinner.getValue();
                int height = (int)heightSpinner.getValue();
                int blockSize = (int)sizeSpinner.getValue();
                MazeGenerator generator = new MazeGenerator(width,height);
                Cell[][] maze = generator.generateMaze();

                //check if net needed
                if (netCheckBox.isSelected()){
                    generator.addNet((int)netSpinner.getValue());
                }
                try {
                    XmlBuilder.buildXML(maze, width, height, blockSize);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        button.addActionListener(listener);
    }

    public static void main(String[] args) {
        new Main();
    }
}