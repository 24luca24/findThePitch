package com.fl.findthepitch.controller;

import javafx.scene.Scene;
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
    }
