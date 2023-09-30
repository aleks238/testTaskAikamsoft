package com.aikamsoft.testTask;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Main {
    public static void main(String[] args) {
        Main object = new Main();
        object.parseInputData(object, args);
    }

    private void parseInputData(Main object, String[] args) {
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

    private void findCustomers(String outputFile, String inputFile){

    }

    private void getStatistics(String outputFile, String inputFile){

    }
}
