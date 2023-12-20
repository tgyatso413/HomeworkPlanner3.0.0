package edu.sdccd.cisc191;


import edu.sdccd.cisc191.template.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {
    @Test
    void testQuoteArrayList() {
        //test if quotes are properly added to and retrieved from arraylist
        Quote quote = new Quote();
        quote.setQuote("Can't wait for vacation!");
        String quoteLine = quote.getQuote();
        assertEquals(quoteLine, "Can't wait for vacation!");
    }

    @Test
    void testNetwork() {
        //setup for testNetwork in client
        String response;
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                response = "Sleep is for the weak";
                out.writeObject(response);
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}