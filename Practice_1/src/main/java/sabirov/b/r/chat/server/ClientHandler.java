package sabirov.b.r.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {

    private Socket clientSocket;

    private BufferedReader in;

    private BufferedWriter out;

    private final ChatLog chatLog;

    public ClientHandler(Socket clientSocket, ChatLog chatLog) {
        this.clientSocket = clientSocket;
        this.chatLog = chatLog;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String nickname = in.readLine();

            chatLog.put(nickname + " присоединился к чату", this);

            while (!Thread.currentThread().isInterrupted()){
                String message = in.readLine();
                chatLog.put(nickname + ": " + message, this);

                if (Objects.isNull(message)) {
                    break;
                }
            }

            chatLog.put(nickname + " отключился от чата", this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerListener.removeClient(this);

    }

    public void sendMessageToClient(String msg) throws IOException {
        if (!clientSocket.isOutputShutdown()) {
            out.write(msg);
            out.newLine();
            out.flush();
        }
    }
}
