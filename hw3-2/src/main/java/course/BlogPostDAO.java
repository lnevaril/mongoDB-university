package course;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import course.bo.Comment;
import course.bo.Post;
import org.bson.Document;

import java.util.*;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {
        // todo  XXX
        return post = postsCollection
                .find(Filters.eq("permalink", permalink))
                .projection(Projections.elemMatch("permalink"))
                .projection(Projections.excludeId())
                .first();
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // todo,  XXX
        // Return a list of Documents, each one a post from the posts collection
        List<Document> posts = postsCollection.find()
                .sort(Sorts.descending("date"))
                .into(new ArrayList<>());

        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();
        permalink = permalink + (new Date()).getTime();

        // todo XXX
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
//        Post post = new Post();
//        post.setTitle(title);
//        post.setAuthor(username);
//        post.setBody(body);
//        post.setPermalink(permalink);
//        post.setDate(new Date());
//        post.setTags(tags);
//        post.setComments(Collections.<Comment>emptyList());

        Document post = new Document("title", title)
                .append("author", username)
                .append("body", body)
                .append("permalink", permalink)
                .append("date", new Date())
                .append("tags", tags)
                .append("comments", Collections.emptyList());

        postsCollection.insertOne(post);

        return permalink;
    }


    // White space to protect the innocent


    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        Document post = findByPermalink(permalink);
        Document comment = new Document("name", name).append("body", body);
        if (email != null) {
            comment = comment.append("email", email);
        }

        postsCollection.findOneAndUpdate(post, new Document("$push", new Document("comments", comment)));
        // todo  XXX
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments

    }
}
