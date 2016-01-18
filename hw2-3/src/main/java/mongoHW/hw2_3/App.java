package mongoHW.hw2_3;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("students");
		MongoCollection<Document> collection = database.getCollection("grades");

		List<Document> docs = collection.find(Filters.eq("type", "homework")) //
				.sort(Sorts.ascending("student_id")) //
				.projection(Projections.excludeId()).into(new ArrayList<Document>());

		for (Document d : docs) {
			System.out.println(d.toJson());
		}

//		int lower = 0;
//		int upper = lower + 1;
//		while (upper < docs.size()) {
//			double score1 = docs.get(lower).getDouble("score");
//			double score2 = docs.get(upper).getDouble("score");
//			collection.deleteOne(score1 > score2 ? docs.get(upper) : docs.get(lower));
//
//			lower += 2;
//			upper = lower + 1;
//		}

//		for (Document d : docs) {
//			System.out.println(d.toJson());
//		}
	}
}
