package com.aikamsoft.testTask.services;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterService {
    public void writeResultsToFile(String nameOfOutputFile, JSONObject resultJsonObject) throws IOException {
        String str = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + nameOfOutputFile;
        String desktopPath = str.replace("\\", "/");
        File outputFile = new File(desktopPath);
        boolean isCreated = outputFile.createNewFile();
        int ind = 1;
        //Если такой файл уже существует, добавить индекс
        while (!isCreated) {
            String[] tokens = nameOfOutputFile.split("\\.");
            str = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + tokens[0] + ind + "." + tokens[1];
            desktopPath = str.replace("\\", "/");
            outputFile = new File(desktopPath);
            isCreated = outputFile.createNewFile();
            ind++;
        }
        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(resultJsonObject.toJSONString());
        fileWriter.close();
    }
}
