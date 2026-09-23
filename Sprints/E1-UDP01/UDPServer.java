package UDPServer;

import java.net.*;
import java.io.*;

public class UDPServer {

    public static void main(String args[]) {
        DatagramSocket aSocket = null;
        int L = 0;

        try {
            aSocket = new DatagramSocket(6789);
            byte[] buffer = new byte[1000];

            while (true) {
                DatagramPacket request =
                        new DatagramPacket(buffer, buffer.length);

                aSocket.receive(request);
                String mensagem = new String(
                        request.getData(),
                        0,
                        request.getLength()
                );

                System.out.println("Recebido: " + mensagem);

                String[] partes = mensagem.split(",", 2);

                if (partes.length != 2) {
                    System.out.println("Mensagem mal formatada.");
                    continue;
                }

                int N;

                try {
                    N = Integer.parseInt(partes[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Número de sequência inválido.");
                    continue;
                }

                String conteudo = partes[1];

                System.out.println("N = " + N);
                System.out.println("Mensagem = " + conteudo);
                
                String resposta;

                if (N == L + 1) {

                    resposta = conteudo;
                    L = N;

                } else {

                    resposta = "waitingfor," + (L + 1);

                }

                byte[] dadosResposta = resposta.getBytes();

                DatagramPacket reply =
                        new DatagramPacket(
                                dadosResposta,
                                dadosResposta.length,
                                request.getAddress(),
                                request.getPort()
                        );

                aSocket.send(reply);
            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());

        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }
}