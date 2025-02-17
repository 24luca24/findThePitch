package com.fl.findthepitch.controller;

import javafx.fxml.FXMLLoader;
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

        public static void switchScene(String fxmlFile, String title, Button button) {
            try {
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
                AnchorPane newRoot = loader.load();
                Scene newScene = new Scene(newRoot);

                // Push the current scene to the stack
                Stage currentStage = (Stage) button.getScene().getWindow();
                SceneManager.pushScene(currentStage.getScene());

                // Switch to new scene
                currentStage.setScene(newScene);
                currentStage.setTitle(title);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading scene: " + fxmlFile);
            }
        }
    }
