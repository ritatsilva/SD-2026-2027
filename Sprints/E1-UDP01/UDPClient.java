package UDPClient;

import java.net.*;
import java.io.*;

public class UDPClient {

    public static void main(String args[]) {

        DatagramSocket aSocket = null;

        try {

            aSocket = new DatagramSocket();

            BufferedReader teclado =
                    new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Escolha o modo de numeração:");
            System.out.println("1 - Automático");
            System.out.println("2 - Manual");
            System.out.print("Modo: ");

            String modo = teclado.readLine();

            int numeroAutomatico = 1;

            while (true) {

                System.out.print("Introduza a mensagem: ");

                String conteudo = teclado.readLine();

                if (conteudo.equalsIgnoreCase("sair")) {
                    break;
                }

                String mensagem;

                if (modo.equals("1")) {

                    mensagem = numeroAutomatico + "," + conteudo;

                } else {

                    System.out.print("Introduza o número de sequência: ");

                    int numeroManual =
                            Integer.parseInt(teclado.readLine());

                    mensagem = numeroManual + "," + conteudo;
                }

                byte[] m = mensagem.getBytes();

                InetAddress aHost =
                        InetAddress.getByName("localhost");

                int serverPort = 6789;

                DatagramPacket request =
                        new DatagramPacket(
                                m,
                                m.length,
                                aHost,
                                serverPort
                        );

                aSocket.send(request);

                byte[] buffer = new byte[1000];

                DatagramPacket reply =
                        new DatagramPacket(
                                buffer,
                                buffer.length
                        );

                aSocket.receive(reply);

                String resposta = new String(
                        reply.getData(),
                        0,
                        reply.getLength()
                );

                if (resposta.startsWith("waitingfor,")) {

                    System.out.println(
                            "Mensagem fora de ordem - servidor " + resposta
                    );

                } else {

                    System.out.println(
                            "Echo recebido: " + resposta
                    );

                }

                if (modo.equals("1")) {
                    numeroAutomatico++;
                }
            }

        } catch (SocketException e) {

            System.out.println("Socket: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("IO: " + e.getMessage());

        } finally {

            if (aSocket != null) {
                aSocket.close();
            }
        }
    }
}