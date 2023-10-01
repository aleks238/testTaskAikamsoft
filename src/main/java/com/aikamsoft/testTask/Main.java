package com.aikamsoft.testTask;


import com.aikamsoft.testTask.services.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;

import java.io.IOException;


@Slf4j
public class Main {
    public static void main(String[] args) {
        Main object = new Main();

        try {
            object.parseInputData(object, args);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void parseInputData(Main object, String[] args) throws IOException, ParseException {
        if (args.length < 3){
            throw new RuntimeException("Неправильное количество входных параметров");
            //write to file
        }
        String operation = args[0];
        String outputFile = args[1];
        String inputFile = args[2];
        if (operation.equals("search")){
            object.findCustomers(outputFile,inputFile);
        }else if (operation.equals("stat")){
            object.getStatistics(outputFile,inputFile);
        }else {
            throw new RuntimeException("Введен некорректный тип операции");
        }
    }

    private void findCustomers(String outputFile, String inputFile) throws IOException, ParseException {
        SearchService searchService = new SearchService();
        //JSONObject outputJsonObject = customerDao.findCustomers("a","b");
        //customerDao.writeResultsToFile("output.json", outputJsonObject);

    }

    private void getStatistics(String outputFile, String inputFile){

    }
}
