package ru.geekbrains.java2.client.controller;

import ru.geekbrains.java2.client.Command;
import ru.geekbrains.java2.client.view.AuthDialog;
import ru.geekbrains.java2.client.view.ClientChat;
import ru.geekbrains.java2.client.model.NetworkService;

import javax.swing.*;
import java.io.*;
import java.util.List;

import static ru.geekbrains.java2.client.Command.*;

public class ClientController {

    private final NetworkService networkService;
    private final AuthDialog authDialog;
    private final ClientChat clientChat;
    private String nickname;
    private String historyFile;
    private FileWriter historyWriter;

    public ClientController(String serverHost, int serverPort) throws IOException {
        this.networkService = new NetworkService(serverHost, serverPort);
        this.authDialog = new AuthDialog(this);
        this.clientChat = new ClientChat(this);

    }

    public void runApplication() throws IOException {
        connectToServer();
        runAuthProcess();
    }



    private void runAuthProcess() throws IOException {
        networkService.setSuccessfulAuthEvent(new AuthEvent() {
            @Override
            public void authIsSuccessful(String nickname, String historyFile) throws IOException {
                ClientController.this.setUserName(nickname);
                ClientController.this.setHistoryFile(historyFile);
                clientChat.setTitle(nickname);
                ClientController.this.openChat();
                displayHistory();
            }
        });
        authDialog.setVisible(true);
    }

    private void openChat() {
        authDialog.dispose();
        try {
            historyWriter = new FileWriter(historyFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkService.setMessageHandler(new MessageHandler() {
            @Override
            public void handle(String message) throws IOException {
                clientChat.appendMessage(message, true);
            }
        });
        clientChat.setVisible(true);

    }

    private void setUserName(String nickname) {
        this.nickname = nickname;
    }

    private void displayHistory() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(historyFile));
        String line;
        while ((line = bufferedReader.readLine()) != null){
            clientChat.appendMessage(line, false);
        }
        bufferedReader.close();
    }

    private void connectToServer() throws IOException {
        try {
            networkService.connect(this);
        } catch (IOException e) {
            System.err.println("Failed to establish server connection");
            throw e;
        }
    }

    public void sendAuthMessage(String login, String pass) throws IOException {
        networkService.sendCommand(authCommand(login, pass));
    }

    public void sendMessageToAllUsers(String message) {
        try {
            networkService.sendCommand(broadcastMessageCommand(message));
        } catch (IOException e) {
            clientChat.showError("Failed to send message!");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        networkService.close();
    }

    public String getUsername() {
        return nickname;
    }

    public void sendPrivateMessage(String username, String message) {
        try {
            networkService.sendCommand(privateMessageCommand(username, message));
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

    public void showErrorMessage(String errorMessage) {
        if (clientChat.isActive()) {
            clientChat.showError(errorMessage);
        }
        else if (authDialog.isActive()) {
            authDialog.showError(errorMessage);
        }
        System.err.println(errorMessage);
    }

    public void updateUsersList(List<String> users) {
        users.remove(nickname);
        users.add(0, "All");
        clientChat.updateUsers(users);
    }

    public void updateMessageHistory(String message) throws IOException {
        historyWriter.write(message + "\n");
        historyWriter.flush();
    }

    public String getHistoryFile() {
        return historyFile;
    }

    public void setHistoryFile(String historyFile) {
        this.historyFile = historyFile;
    }
}
