package sabirov.b.r.chat.server;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerListener {


    private ServerSocket serverSocket;
    private static List<ClientHandler> clients = new ArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();


    public void start() throws IOException {
        serverSocket = new ServerSocket(27015);
        while (true) {
            Socket income = serverSocket.accept();
            ClientHandler client = new ClientHandler(income, new ChatLog());
            clients.add(client);
            executorService.execute(client); // Запускайте клиентские обработчики через пул
        }
    }



    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public static List<ClientHandler> getClients() {
        return clients;
    }

}


