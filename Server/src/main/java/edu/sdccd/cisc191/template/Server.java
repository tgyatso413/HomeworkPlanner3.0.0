package edu.sdccd.cisc191.template;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        Quote quote = new Quote();
        quote.setQuote("Sofia says hi.");
        quote.setQuote("DO YOUR HOMEWORK NOW NOW NOW");
        quote.setQuote("There are snakes on the plane.");
        quote.setQuote("Teddy says hi.");

        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                Socket clientSocket = serverSocket.accept();

                String response = quote.getQuote();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(response);
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}