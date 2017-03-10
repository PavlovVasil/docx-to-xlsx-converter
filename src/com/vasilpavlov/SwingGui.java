package com.vasilpavlov;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;

public class SwingGui extends JFrame {
    JButton openButton;
    JLabel fileOpenedLabel;
    JCheckBox chk1;
    JLabel wrongLimitLabel;
    JTextField symbolLimit;
    JButton convertButton;
    JLabel convertedLabel;
    JFileChooser openChooser;
    JFileChooser saveChooser;
    FileHandler fh;

    public SwingGui() {
        super();
        fh = new FileHandler();
        openChooser = new JFileChooser();
        openChooser.setCurrentDirectory(new File("."));
        openChooser.setDialogTitle("Open a file...");
        openChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        openChooser.setFileFilter(new FileNameExtensionFilter("Docx file", "docx"));
        saveChooser = new JFileChooser();
        saveChooser.setCurrentDirectory(new File("."));
        saveChooser.setDialogTitle("Save a file...");
        saveChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        saveChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        saveChooser.setSelectedFile(new File("Default"));
        saveChooser.setFileFilter(new FileNameExtensionFilter("xlsx file", "xlsx"));
        openButton = new JButton("Open a text file");
        openButton.setBounds(125, 80, 150, 40); //x axis, y axis, width, height
        fileOpenedLabel = new JLabel("No file selected");
        fileOpenedLabel.setBounds(155, 125, 100, 20);
        chk1 = new JCheckBox("Symbols per row : ");
        chk1.setBounds(95, 180, 130, 40);
        symbolLimit = new JTextField(15);
        symbolLimit.setBounds(225, 192, 70, 20);
        wrongLimitLabel = new JLabel("");
        wrongLimitLabel.setBounds(115, 220, 170, 20);
        convertButton = new JButton("Convert to xlsx");
        convertButton.setBounds(125, 310, 150, 40);
        convertedLabel = new JLabel("No file saved");
        convertedLabel.setBounds(162, 355, 100, 20);
        add(openButton);
        add(fileOpenedLabel);
        add(chk1);
        add(symbolLimit);
        add(wrongLimitLabel);
        add(convertButton);
        add(convertedLabel);
        setSize(400, 500); //width, height
        setLayout(null);

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //the action for the "Open" button

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (openChooser.showOpenDialog(openButton) == JFileChooser.APPROVE_OPTION){
                    File file = openChooser.getSelectedFile();
                    fh.setInputFile(openChooser.getSelectedFile());
                    try {
                        fh.openWordFile();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                fileOpenedLabel.setForeground(java.awt.Color.decode("#0d660d"));
                                fileOpenedLabel.setText("File successfully loaded!");
                                fileOpenedLabel.setBounds(130, 125, 200, 20);
                            }
                        });
                    }
                    catch(IOException ex){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                fileOpenedLabel.setForeground(java.awt.Color.decode("#b31919"));
                                fileOpenedLabel.setText("File not found!");
                                fileOpenedLabel.setBounds(160, 125, 200, 20);
                            }
                        });
                    }
                }
            }
        });

        //the action for the "Convert" button

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        wrongLimitLabel.setText("");
                    }
                });
                if(saveChooser.showSaveDialog(convertButton) == JFileChooser.APPROVE_OPTION){
                    fh.setOutputFile(saveChooser.getSelectedFile());
                    try{
                        if(fh.hasLimit){
                            fh.userInput = symbolLimit.getText();
                            fh.checkInput();
                        }
                        fh.convertFile();
                        fh.saveFile();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                convertedLabel.setForeground(java.awt.Color.decode("#0d660d"));
                                convertedLabel.setText("File successfully written!");
                                convertedLabel.setBounds(130, 355, 200, 20);
                            }
                        });
                    }
                    catch (NumberFormatException ex){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                wrongLimitLabel.setForeground(java.awt.Color.decode("#b31919"));
                                wrongLimitLabel.setText("Please enter another number!");
                            }
                        });
                    }
                    catch (IOException ex){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                convertedLabel.setForeground(java.awt.Color.decode("#b31919"));
                                convertedLabel.setText("Error while saving the file!");
                                convertedLabel.setBounds(126, 355, 200, 20);
                            }
                        });
                    }
                }
            }
        });

        chk1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chk1.isSelected()){
                    fh.hasLimit = true;
                }
                else fh.hasLimit = false;
            }
        });

    }
}