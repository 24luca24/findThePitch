package model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;


public class FireBaseConnection {
    public static void main(String[] args) {
        try {
            // Load from classpath (correct for Maven)
            InputStream serviceAccount = FireBaseConnection.class.getClassLoader()
                    .getResourceAsStream("serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new RuntimeException("Firebase service account file not found!");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);

            System.out.println("🔥 Firebase initialized successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
