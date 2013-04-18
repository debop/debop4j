package example.avro.rpc;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * example.avro.rpc.CalculatorServer
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 17. 오후 9:16
 */
@Slf4j
public class CalculatorServer {

    public static class CalculatorImpl implements Calculator {

        @Override
        public double add(double x, double y) throws AvroRemoteException {
            return x + y;
        }

        @Override
        public double subtract(double x, double y) throws AvroRemoteException {
            return x - y;
        }
    }

    private static Server calcServer;
    private static final int CALC_SERVER_PORT = 65123;

    private static void startServer() throws IOException {
        calcServer = new NettyServer(new SpecificResponder(Calculator.class, new CalculatorImpl()),
                                     new InetSocketAddress(CALC_SERVER_PORT));
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Starting server...");
        startServer();
        System.out.println("Server started");

        NettyTransceiver calcClient = new NettyTransceiver(new InetSocketAddress(CALC_SERVER_PORT));
        Calculator proxy = (Calculator) SpecificRequestor.getClient(Calculator.class, calcClient);

        System.out.println("Client built, get proxy");
        System.out.println("add(2, 3)=" + proxy.add(2, 3));
        System.out.println("subtract(5, 1)=" + proxy.subtract(5, 1));

        calcClient.close();
        calcServer.close();
    }
}
