package asteroids;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.scene.text.Text;
import java.util.concurrent.atomic.AtomicInteger;

public class AsteroidsApplication extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 400;
    
    // HashMap to store key presses
    Map<KeyCode,Boolean> pressedKeys = new HashMap<>();
    
    // ArrayList to store the projectiles
    List<Projectile> projectiles = new ArrayList<>();

    @Override
    public void start(Stage stage){
        Pane pane = new Pane();
        Text text = new Text(10, 20, "Points: 0");
        pane.getChildren().add(text);
        pane.setPrefSize(WIDTH, HEIGHT);
        
        // A class which is used to add points to the game
        AtomicInteger points = new AtomicInteger();
        
        // Create the ship to fire at asteroids
        Ship ship = new Ship(WIDTH/2, HEIGHT/2);
        
        // Create asteroids and store them in an ArrayList
        List<Asteroid> asteroids = new ArrayList<>();
        for(int i=0;i<5;i++){
            Random rand = new Random();
            Asteroid asteroid = new Asteroid(rand.nextInt(WIDTH/3),rand.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
        
        // Add ship and asteroids to the window pane
        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
              
        Scene scene = new Scene(pane);
        
        // Put key presses in the HashMap and set their status
        scene.setOnKeyPressed((event) -> {
            pressedKeys.put(event.getCode(),true);
        });
        
        scene.setOnKeyReleased((event) -> {
            pressedKeys.put(event.getCode(),false);
        });
        
        new AnimationTimer(){
            
            public void handle(long now){
                
                // Display points
                text.setText("Points: " + points);
                
                if(pressedKeys.getOrDefault(KeyCode.LEFT, Boolean.FALSE)){
                    ship.turnLeft();
                }
                if(pressedKeys.getOrDefault(KeyCode.RIGHT, Boolean.FALSE)){
                    ship.turnRight();
                }
                if(pressedKeys.getOrDefault(KeyCode.DOWN, Boolean.FALSE)){
                    ship.accelerate();
                }
                // Create projectiles and add them to the arraylist and also to the window pane. A maximum of 2 projectiles are fired at a time.
                if(pressedKeys.getOrDefault(KeyCode.SPACE, Boolean.FALSE) && projectiles.size()<3){
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(),(int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);
                    
                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                    
                    pane.getChildren().add(projectile.getCharacter());
                }
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());
                
                // Stop the game if the ship collides with an asteroid
                asteroids.forEach(asteroid -> {
                    if(ship.collide(asteroid)){
                        stop();
                    }
                });
               
                // If a projectile or asteroid collides with the other, set the Alive status of both to false
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                    
                    // Add points for every destroyed asteroid
                    if(!projectile.isAlive()) {
                        text.setText("Points: " + points.addAndGet(1000));
                    }
                });
                
                // Remove all the projectiles whose Alive status is false from the window pane and the arraylist 
                projectiles.stream()
                    .filter(projectile -> !projectile.isAlive())
                    .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));
                
                // Remove all the asteroids whose Alive status is false from the window pane and the arraylist
                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
                asteroids.removeAll(asteroids.stream()
                            .filter(asteroid -> !asteroid.isAlive())
                            .collect(Collectors.toList()));
                
                // Add new asteroids to the window pane at certain intervals
                if(Math.random() < 0.005) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }
            }
        }.start();
        
        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        //System.out.println("Hello, world!");
        launch(args);
    }

    /*public static int partsCompleted() {
        // State how many parts you have completed using the return value of this method
        return 4;
    }*/

}
