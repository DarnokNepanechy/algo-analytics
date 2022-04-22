package service;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.TruncatedChunkException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

import static Configuration.AppConfiguration.*;

/*
    Класс для загрузки файлов по http запросам на локальный компьюер
*/

public class FilesDownloader {
    HttpClient client = HttpClients.custom().build();

    private String getToken(String URL) throws IOException {
        HttpUriRequest request = RequestBuilder.post()
                .setUri(URL)
                .addParameter("login", ALGO_LOGIN)
                .addParameter("password", ALGO_PASSWORD)
                .build();

        HttpResponse response = client.execute(request);

        // Забираю только токен из всего response.
        return response.toString()
                .substring(
                    response.toString().indexOf("accessToken="),
                    response.toString().indexOf("; expires")
                ).substring(12);
    }

    public void downloadFiles(File folder) throws IOException {
        // Создаёт каталог, если его нет, если есть, то удаляет из него все файлы.
        if (!folder.exists()) {
            folder.mkdir();
//            System.out.println("Папка " + folder.getAbsolutePath() + " была создана.\n");
        } else {
            for(File file: folder.listFiles())
                if (!file.isDirectory())
                    file.delete();
        }

        // Получает с сервера алгоритмики токен доступа
        String accessToken = this.getToken(AUTH_LINK);

        // Скачивает необходимые файлы
        try {
            downloadFile(STUDENT_LINK,"student", ".csv", folder, accessToken);
            downloadFile(GROUP_LINK,"group", ".csv", folder, accessToken);
            downloadFile(SCHEDULE_LINK,"schedule", ".csv", folder, accessToken);
            downloadFile(INVOICES_LINK,"invoices", ".csv", folder, accessToken);
        } catch (TruncatedChunkException exception) {
            exception.printStackTrace();
        }
    }

    private void downloadFile(String URI, String fileName, String suffix, File folder, String accessToken) throws IOException {
        File file = File.createTempFile(fileName, suffix, folder);

        // Запрос на скачивание файла
        HttpUriRequest request = RequestBuilder.get()
                .setUri(URI)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader("accessToken", accessToken)
                .build();

        // Ответ
        HttpResponse response = client.execute(request);

        // Скачивание
        byte[] buffer = new byte[4096];
        int byteRead;
//        System.out.println("Файл по запросу " + URI + " скачивается...");
        try (
                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream inputStream = response.getEntity().getContent();
             ) {
            // Записывает скачаные биты в файл
            while ((byteRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteRead);
            }

//            System.out.println("Файл " + file.getAbsolutePath() + " успешно загружен!\n");
        } catch (TruncatedChunkException exception) {
            // Удаляет не докачанный файл и скачивает его снова
            file.delete();
            downloadFile(URI, fileName, suffix, folder, accessToken);
        }
    }
}
