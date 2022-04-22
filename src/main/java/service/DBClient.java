package service;

import com.opencsv.exceptions.CsvValidationException;
import service.сonfiguration.AppConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/*
    Класс для работы с БД
*/

public class DBClient {
    // Добавление в БД
    public void putToTable(File file, String tableName) throws CsvValidationException, IOException {
        try (
                Connection connection = DriverManager.getConnection(
                        AppConfiguration.DB_PATH,
                        AppConfiguration.PSQL_USER,
                        AppConfiguration.PSQL_PASSWORD
                );
                Statement statement = connection.createStatement();
        ) {
            // Отключение auto commit для повышения скорости - запрос применяется только в случае добавления всех данных
            connection.setAutoCommit(false);
            // Парсер .csv файлов
            CSVHandler studentCSV = new CSVHandler(file);

            // Удаление старой таблицы
            statement.execute("DROP TABLE IF EXISTS " + tableName + "_old;");
            System.out.println("Таблица " + tableName + "_old удалена.");

            // Переименование прошлых новых в старые таблицы
            statement.execute("ALTER TABLE IF EXISTS " + tableName + " RENAME TO " + tableName + "_old;");
            System.out.println("Таблица " + tableName + " переименована в " + tableName + "_old.");

            // Создание новой таблицы
            String[] headLine = studentCSV.getReader().readNext();
            int i = 0;
            if (headLine != null) {
                StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");

                for (String head: headLine) {
                    sql
                            .append("\"")
                            .append(head)
                            .append("\"")
                            .append(" text COLLATE pg_catalog.\"default\"");
                    if (++i != headLine.length) {
                        sql.append(", ");
                    }
                }
                sql.append(");");

                System.out.println("Таблица " + tableName + " создана.");
            }

            // Добавление строки в созданную таблицу
            String[] nextLine;
            System.out.println("Добавление данных из " + file.getName() + " в таблицу " + tableName + "...");
            while ((nextLine = studentCSV.getReader().readNext()) != null) {
                StringBuilder sql = new StringBuilder("INSERT INTO " + tableName);
                sql.append("\nVALUES (");

                i = 0;
                for (String line: nextLine) {
                    sql
                            .append("'")
                            .append(line)
                            .append("'");
                    if (++i != headLine.length) {
                        sql.append(", ");
                    }
                }
                sql.append(");");

                statement.execute(sql.toString());
            }

            // Вот, собственно, и commit
            connection.commit();
            System.out.println("Данные в таблицу " + tableName + " успешно добавлены!\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}