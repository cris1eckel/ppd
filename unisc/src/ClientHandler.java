import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ClientHandler class
class ClientHandler extends Thread {
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true) {
            try {

                // Ask user what he wants
                dos.writeUTF("Adicione uma nova linha ao arquivo: (escreva Exit, para encerrar a sessão) ");

                // receive the answer from client
                received = dis.readUTF();

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // creating Date object
                Date date = new Date();

                // write on output stream based on the
                // answer from the client

                try {

                    Path path = Paths.get("C:\\Users\\cristiano.eckel\\Documents\\GitHub\\ppd\\unisc\\documento.txt");
                    Stream<String> lines = Files.lines(path);

                    String content = lines.collect(Collectors.joining(System.lineSeparator()));
                    toreturn = content;
                    dos.writeUTF(toreturn);
                    lines.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (FileWriter f = new FileWriter("C:\\Users\\cristiano.eckel\\Documents\\GitHub\\ppd\\unisc\\documento.txt", true);
                     BufferedWriter b = new BufferedWriter(f);
                     PrintWriter p = new PrintWriter(b);) {
                    p.println(received);
                }

//                switch (received) {
//
//                    case "Digitar" :
//                        dos.writeUTF("Digite seu texto: ");
//                        try (FileWriter f = new FileWriter("C:\\Users\\cristiano.eckel\\Documents\\GitHub\\ppd\\unisc\\documento.txt", true);
//                             BufferedWriter b = new BufferedWriter(f);
//                             PrintWriter p = new PrintWriter(b);) {
//                            p.println(received);
//                            //ABRIR ARQUIVO
//                            try {
//                                Path path = Paths.get("C:\\Users\\cristiano.eckel\\Documents\\GitHub\\ppd\\unisc\\documento.txt");
//                                Stream<String> lines = Files.lines(path);
//
//                                String content = lines.collect(Collectors.joining(System.lineSeparator()));
//                                toreturn = content;
//                                dos.writeUTF(toreturn);
//                                lines.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        break;
//
//                    case "Editar" :
//                        toreturn = fortime.format(date);
//                        dos.writeUTF(toreturn);
//                        break;
//
//                    case "Excluir" :
//                        toreturn = fortime.format(date);
//                        dos.writeUTF(toreturn);
//                        break;
//
//
//                    default:
//                        dos.writeUTF("Invalid input");
//                        break;
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
