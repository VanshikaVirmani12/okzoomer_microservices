package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpServer;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class App {
   static int PORT = 8000;

   public static void main(String[] args) throws IOException {

      // This code must be used when you connect this service to MongoDB
      // Failure to use environment variables will result in a 0 for A2 correctness
      // and any remark requests will be denied.

      Dotenv dotenv = Dotenv.load();
      String addr = dotenv.get("MONGODB_ADDR");
      String uriDb = "bolt://" + addr + ":27017";

      HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
      server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());

      server.createContext("/trip/request", new Request());
      server.createContext("/trip/confirm", new Confirm());
      server.createContext("/trip/passenger", new Passenger());
      server.createContext("/trip/driver", new Driver());
      server.createContext("/trip/driverTime", new DriverTime());
      server.createContext("/trip/", new Trip());

      server.start();
      System.out.printf("Server started on port %d...\n", PORT);
   }
}
