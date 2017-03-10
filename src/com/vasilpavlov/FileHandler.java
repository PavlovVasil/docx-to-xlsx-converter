package com.vasilpavlov;

import java.util.List;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

class FileHandler {

    public boolean hasLimit;
    public String userInput;
    private int theLimit;

    //those are for reading the text file
    private File inputFile;
    private List<XWPFParagraph> paragraphList;
    private FileInputStream fis;

    //those are for creating the output excel file
    private File outputFile;
    private FileOutputStream fos;
    private XWPFDocument wordDoc;
    private XSSFWorkbook wb;
    private XSSFSheet sheet;
    private Row row;
    private Cell cell;

    public FileHandler(){
        hasLimit = false;
        userInput = "";
    }

    public void setInputFile(File file) {
        inputFile = file.getAbsoluteFile();
    }

    public void setOutputFile(File file) {

        outputFile = file.getAbsoluteFile();
    }

    //loading the text file

    public void openWordFile() throws IOException {
        try{
            fis = new FileInputStream(inputFile);
            wordDoc = new XWPFDocument(fis);
            paragraphList = wordDoc.getParagraphs();
        }
        catch (IOException e){
            //this exception should be propagated to the calling method
            throw e;
        }
        finally{
            fis.close();
        }
    }

    public void checkInput() throws NumberFormatException {
        try {
            theLimit = Integer.parseInt(userInput);
            if (theLimit < 0){
                throw new NumberFormatException();
            }
            if (theLimit == 0){
                hasLimit = false;
            }
        }
        catch (NumberFormatException e){
            //this exception should be propagated to the calling method
            throw e;
        }
    }

    public void convertFile(){
        wb = new XSSFWorkbook();
        sheet = wb.createSheet("Output sheet");

        // populating the cells with text if there is a length limit
        if (hasLimit) {
            for (int i = 0; i < paragraphList.size(); i++) {
                row = sheet.createRow(i);
                cell = row.createCell(0);
                String curStr = paragraphList.get(i).getText();

                //slicing the string if it's longer than the limit

                if (curStr.length() > theLimit) {
                    curStr = curStr.substring(0, theLimit);
                }
                cell.setCellValue(curStr);
            }
        } else {
            for (int i = 0; i < paragraphList.size(); i++) {
                row = sheet.createRow(i);
                cell = row.createCell(0);
                cell.setCellValue(paragraphList.get(i).getText());
            }
        }

        //this removes the empty rows from the table

        for(int i = 0; i < sheet.getLastRowNum(); i++){
            boolean isRowEmpty = false;
            if(sheet.getRow(i)==null){
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                i--;
                continue;
            }
            for(int j =0; j<sheet.getRow(i).getLastCellNum();j++){
                if(sheet.getRow(i).getCell(j).toString().trim().equals("")){
                    isRowEmpty=true;
                }else {
                    isRowEmpty=false;
                    break;
                }
            }
            if(isRowEmpty){
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                i--;
            }
        }

        //auto-sizing the columns to accommodate the text content

        for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++){
            sheet.autoSizeColumn(i);
        }
    }

    public void saveFile()throws IOException {
        try{
            // removing the file extension (if the user typed any) and saving the file as ".xlsx"

            fos = new FileOutputStream(new File(FilenameUtils.removeExtension(outputFile.toString()) + ".xlsx"));
            wb.write(fos);
        }
        catch (FileNotFoundException e){
            //this exception should be propagated to the calling method
            throw e;
        }
        catch (IOException e) {
            //this exception should be propagated to the calling method
            throw e;
        }
        finally {
            fos.close();
        }
    }
}
