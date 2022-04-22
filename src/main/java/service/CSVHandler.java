package service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/*
    Класс для работы с .csv файлами
*/

public class CSVHandler {
    private CSVReader reader;

    public CSVHandler (File file) {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
        try {
            this.reader = new CSVReaderBuilder(new FileReader(file))
                    .withCSVParser(parser)
                    .build();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public CSVReader getReader() {
        return reader;
    }
}
