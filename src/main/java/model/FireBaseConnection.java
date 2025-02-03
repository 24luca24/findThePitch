package model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FireBaseConnection {

    public static void main(String[] args) throws IOException {
        // Path to your service account key JSON file
        String pathToServiceAccountKey = "../resources/serviceAccountKey.json";

        // Initialize Firebase
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(pathToServiceAccountKey)))
                .setDatabaseUrl("https://your-database-name.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(options);

        // Initialize Cloud Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Create a document reference
        com.google.cloud.firestore.DocumentReference docRef = db.collection("users").document("user1");

        // Write data to the document
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John Doe");
        data.put("age", 30);
        docRef.set(data, SetOptions.merge()); // Use 'set' with 'SetOptions.merge()' for Cloud Firestore

    }
}
