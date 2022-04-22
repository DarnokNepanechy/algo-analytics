import com.opencsv.exceptions.CsvValidationException;
import service.DBClient;
import service.FilesDownloader;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static service.сonfiguration.AppConfiguration.FOLDER_PATH;

/*
    Основной класс приложения
*/

public class Application {
    // TODO: запускать этот метод по расписанию (каждые 2 часа с 8 до 22 часов).
    public static void main(String[] args) {
        // Скачивает файлы
        FilesDownloader filesDownloader = new FilesDownloader();
        File folder = new File(FOLDER_PATH);
        try {
            filesDownloader.downloadFiles(folder);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка скачивания файлов.");
        }

        // Кладёт данные из файлов в БД
        DBClient client = new DBClient();
        try {
            File[] files = folder.listFiles();
            for (File f: files) {
                if (f.getName().contains("student"))
                    client.putToTable(f, "students");
                if (f.getName().contains("group"))
                    client.putToTable(f, "groups");
                if (f.getName().contains("schedule"))
                    client.putToTable(f, "events");
                if (f.getName().contains("invoices"))
                    client.putToTable(f, "invoices");
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка загрузки в базу данных.");
        }

        System.out.println("Данные обновлены: " + new Date());
    }
}
