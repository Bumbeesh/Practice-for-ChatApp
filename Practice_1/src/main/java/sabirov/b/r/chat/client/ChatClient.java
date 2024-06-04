package sabirov.b.r.chat.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String serverAddress;


    public ChatClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void start() throws IOException {

        socket = new Socket(serverAddress,27015);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ваш никнейм: ");

        String nickname = scanner.nextLine();
        out.write(nickname);
        out.newLine();
        out.flush();

        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("выход")) {
                break;
            }
            out.write(message);
            out.newLine();
            out.flush();
        }

        socket.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost");
        client.start();
    }
}
