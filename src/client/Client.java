package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        String serverAddress = "127.0.0.1"; // IP-адрес сервера
        int serverPort = 12345; // Порт сервера

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            // Получаем данные о системных ресурсах компьютера
            String cpuLoad = getCpuLoad();
            String ramUsage = getRamUsage();

            // Отправляем данные на сервер
            writer.println(cpuLoad);
            writer.println(ramUsage);
            System.out.println("Данные успешно отправлены на сервер.");
        }
    }

    private static String getRamUsage() {
        return null;
    }

    private static String getCpuLoad() {
        return null;
    }
}
