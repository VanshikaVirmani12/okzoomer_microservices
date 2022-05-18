package ca.utoronto.utm.mcs;

import com.mongodb.client.MongoClients;
import com.mongodb.client.model.*;
import io.github.cdimascio.dotenv.Dotenv;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.result.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class MongoDAO {

    private MongoCollection<Document> collection;

    private final String username = "root";
    private final String password = "123456";
    private final int PORT = 27017;
    private String uriDb;
    private final String dbName = "trip";
    private final String collectionName = "trips";

    public MongoDAO() {
        Dotenv dotenv = Dotenv.load();
        String addr = dotenv.get("MONGODB_ADDR");
        this.uriDb = String.format("mongodb://%s:%s@%s:%d", username, password, addr, PORT);

        MongoClient mongoClient = (MongoClient) MongoClients.create(this.uriDb);
        MongoDatabase database = mongoClient.getDatabase(this.dbName);
        this.collection = database.getCollection(this.collectionName);
    }

    public String addTrip(String driverUID, String passengerUID, int startTime) {
        Document trip = new Document("_id", new ObjectId());
        trip.append("driver", driverUID)
                .append("passenger", passengerUID)
                .append("startTime", startTime);

        collection.insertOne(trip);
        return trip.get("_id").toString();
    }

    public void modifyTrip(String _id, int distance, int endTime, int timeElapsed, float discount,
                           float totalCost, float driverPayout) {
        Document updated = new Document("distance", distance)
                .append("endTime", endTime)
                        .append("timeElapsed", timeElapsed)
                                .append("discount", discount)
                                        .append("totalCost", totalCost)
                                                .append("driverPayout", driverPayout);
        collection.updateOne(Filters.eq("_id", new ObjectId(_id)), updated);
    }

    public List<JSONObject> passengerTrips(String uid) {

        List<JSONObject> list = new ArrayList<>();



    }




}
