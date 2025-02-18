package com.fl.findthepitch.controller;

import com.fl.findthepitch.view.DescriptionOfThePitch;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class SceneManager {

        private static Stack<Scene> sceneStack = new Stack<>();

        public static void pushScene(Scene scene) {
            sceneStack.push(scene);
        }

        public static Scene popScene() {
            if (!sceneStack.isEmpty()) {
                return sceneStack.pop();
            }
            return null; // No previous scene to pop
        }

        public static Scene peekScene() {
            return sceneStack.isEmpty() ? null : sceneStack.peek();
        }

        public static void switchScene(String fxmlFile, String title, Node source) {
            try {
                if (SceneManager.class.getResource(fxmlFile) == null) {
                    System.err.println("Error: FXML file not found - " + fxmlFile);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
                Parent root = loader.load();

                // Check if the controller needs HostServices
                Object controller = loader.getController();
                if (controller instanceof DescriptionOfThePitch) {
                    ((DescriptionOfThePitch) controller).setHostServices(hostServices);
                }

                if (source == null || source.getScene() == null) {
                    System.err.println("Error: Source node or scene is null!");
                    return;
                }

                Stage stage = (Stage) source.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle(title);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static void switchScene(Stage currentStage, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
            AnchorPane newRoot = loader.load();
            Scene newScene = new Scene(newRoot);

            // Push the current scene to the stack
            SceneManager.pushScene(currentStage.getScene());

            // Switch to new scene
            currentStage.setScene(newScene);
            currentStage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading scene: " + fxmlFile);
        }
    }


    private static HostServices hostServices;

    // Call this from your MainView's start() method.
    public static void setHostServices(HostServices hs) {
        hostServices = hs;
    }

    public static HostServices getHostServices() {
        return hostServices;
    }

}
