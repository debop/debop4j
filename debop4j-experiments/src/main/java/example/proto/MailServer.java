package example.proto;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * example.proto.MailServer
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 17. 오후 8:39
 */
@Slf4j
public class MailServer {

    private static final boolean isDebugEnabled = log.isDebugEnabled();

    /**
     * 서버에서 수행할 내용입니다.
     */
    public static class MailImpl implements Mail {

        @Override
        public Utf8 send(Message message) throws AvroRemoteException {
            System.out.println("Sending message at Server...");
            return new Utf8("Sending message to" + message.getTo().toString()
                                    + " from " + message.getFrom().toString()
                                    + " with body " + message.getBody().toString());
        }
    }

    private static Server server;

    private static void startServer() throws IOException {
        server = new NettyServer(new SpecificResponder(Mail.class, new MailImpl()), new InetSocketAddress(65111));
    }

    public static void main(String[] args) throws IOException {
//        if (args.length != 3) {
//            System.out.println("Usage: <to> <from> <body>");
//            System.exit(1);
//        }

        final String to = "debop";
        final String from = "admin";
        final String body = "Hello World. 안녕하세요^^";

        System.out.println("Starting server...");
        startServer();
        System.out.println("Server started");

        NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(65111));
        Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);
        System.out.println("Client built, get proxy");

        // fill in the message record and send it
        Message message = Message.newBuilder()
                .setTo(new Utf8(to))
                .setFrom(new Utf8(from))
                .setBody(new Utf8(body))
                .build();
        System.out.println("Calling proxy. send with message: " + message.toString());
        System.out.println("Result: " + proxy.send(message));

        client.close();
        server.close();
    }
}
