package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.Timer;


public class Server {
    public static void main(String[] args) {
        int port = 12345; // Порт для прослушивания

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен и ожидает подключения...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключен клиент: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private OperatingSystemMXBean osBean;
    private Timer timer;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        this.timer = new Timer();
    }

    @Override
    public void run() {
        while (true){
            updateAndPrintData();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateAndPrintData() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)))) {
            // Получаем данные о системных ресурсах компьютера
            double cpuLoad = osBean.getSystemCpuLoad() * 100;
            long totalMemory = osBean.getTotalPhysicalMemorySize();
            long freeMemory = osBean.getFreePhysicalMemorySize();
            double ramUsage = (totalMemory - freeMemory) / (double) totalMemory * 100;
            String ipAddress = clientSocket.getInetAddress().getHostAddress();

            // Записываем данные в файл
            writer.println("IP-адрес: " + ipAddress);
            writer.println("Нагрузка на ЦП: " + cpuLoad + "%");
            writer.println("Использование ОЗУ: " + ramUsage + "%");
            writer.println();

            System.out.println("Данные успешно записаны на сервере.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
