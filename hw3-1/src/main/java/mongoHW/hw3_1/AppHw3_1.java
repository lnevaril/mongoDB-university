package mongoHW.hw3_1;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Hello world!
 */
public class AppHw3_1 {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("school");
        MongoCollection<Document> collection = database.getCollection("students");

        List<Document> students = collection.find().into(new ArrayList<>());

        for (Document student : students) {
            processStudent(collection, student);
        }

    }

    @SuppressWarnings("unchecked")
    private static void processStudent(MongoCollection<Document> collection, Document student) {
        List<Document> scores = (List<Document>) student.get("scores");
        List<Document> homeworks = new ArrayList<>();
        for (Document score : scores) {
            if (score.get("type").equals("homework")) {
                homeworks.add(score);
            }
        }

        if (homeworks.isEmpty()) {
            return;
        }
        Document withMinScore = homeworks.get(0);
        for (int i=1; i < homeworks.size(); i++) {
            if ((double) homeworks.get(i).get("score") < (double) withMinScore.get("score")) {
                withMinScore = homeworks.get(i);
            }
        }
        System.out.println(collection.find(student).first().toJson());
        List<Document> newScores = new ArrayList<>(scores);
        newScores.remove(withMinScore);
        collection.updateOne(student, new Document("$set", new Document("scores", newScores)));

    }

}
