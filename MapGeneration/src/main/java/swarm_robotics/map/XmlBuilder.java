package swarm_robotics.map;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Allows to form xml file out of maze passed as cell matrix.
 */

public class XmlBuilder {

    /**
     * Makes single xml node from every maze block and adds it to document.
     * Then writes everything to the file.
     * @param file file to write in.
     * @param maze cells source.
     * @param width maze width.
     * @param height maze height.
     * @param blockSize
     */
    private static void saveToXML(File file, Cell[][] maze, int width, int height, int blockSize){
        Document dom;
        Element eBody = null;
        Element eBox = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element rootEle = dom.createElement("boxes");

            // create data elements and place them under root
            long id = 0;
            for(int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (maze[i][j].getType() == Type.WALL) {
                        int leftTopX = j * blockSize, leftTopY = i * blockSize;
                        eBox = dom.createElement("box");
                        eBox.setAttribute("id", String.valueOf(id++));
                        eBox.setAttribute("size", blockSize + "," + blockSize + "," + blockSize);
                        eBox.setAttribute("movable", "false");
                        eBody = dom.createElement("body");
                        eBody.setAttribute("position", leftTopX + "," + leftTopY + ",0");
                        eBody.setAttribute("orientation", "0,0,0");
                        eBox.appendChild(eBody);
                        rootEle.appendChild(eBox);
                    }
                }
            }

            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(file)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }


        }catch (ParserConfigurationException pce) {
            System.out.println("Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    public static void buildXML(Cell[][] maze, int width, int height, int blockSize){
        File file = getNewFile();
        if (file != null){
            saveToXML(file, maze, width, height, blockSize);
            JOptionPane.showMessageDialog(null, "Files have been saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // success!
        }
    }

    /**
     * Method that calls file dialog to choose new file path.
     * @return returns file.
     */
    private static File getNewFile() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Save file");
        chooser.setSelectedFile(new File("maze.xml"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.xml", "xml"));
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "Error while saving the file!", "Error", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        File newFile = chooser.getSelectedFile();
        String[] string = newFile.getPath().split("\\.");
        if (!string[string.length - 1].equals("xml"))
            newFile = new File(newFile.getPath() + ".xml");

        return newFile;
    }
}
