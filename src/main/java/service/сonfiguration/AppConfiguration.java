package service.сonfiguration;

/*
    Конфигурация приложения
 */

public abstract class AppConfiguration {
    // Туда скачиваются временные файлы из БО
//    public static final String FOLDER_PATH = "C:/temporary-files";
    public static final String FOLDER_PATH = "/home/temporary-files";

    // Ссылка в БО Алгоритмики на аутентификацию
    public static final String AUTH_LINK = "https://lms.algoritmika.org/s/auth/api/e/user/auth";
    // Ссылки в БО Алгоритмики на скачивание файлов
    public static final String STUDENT_LINK = "https://lms.algoritmika.org/student?export=true&name=default&exportType=csv";
    public static final String GROUP_LINK = "https://lms.algoritmika.org/group?export=true&name=default&exportType=csv";
    public static final String SCHEDULE_LINK = "https://lms.algoritmika.org/group/default/schedule?export=true&name=default&exportType=csv";
    public static final String INVOICES_LINK = "https://lms.algoritmika.org/payment/manage/invoices?export=true&name=default&exportType=csv";
    // Логин и пароль от БО Алгоритмики
    public static final String ALGO_LOGIN = "Bot_KRD";
    public static final String ALGO_PASSWORD = "HAHd;5qk";

    // jdbc:postgresql://host:port/database
//    public static final String DB_PATH = "jdbc:postgresql://194.163.184.47:5432/algo_krd";
    public static final String DB_PATH = "jdbc:postgresql://localhost:5432/algo_krd";
    // Пользователь и его пароль от БД
    public static final String PSQL_USER = "postgres";
    public static final String PSQL_PASSWORD = "jSONmain";
}
