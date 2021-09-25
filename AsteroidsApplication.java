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
    Map<KeyCode,Boolean> pressedKeys = new HashMap<>();
    List<Projectile> projectiles = new ArrayList<>();

    @Override
    public void start(Stage stage){
        Pane pane = new Pane();
        Text text = new Text(10, 20, "Points: 0");
        pane.getChildren().add(text);
        pane.setPrefSize(WIDTH, HEIGHT);
        
        AtomicInteger points = new AtomicInteger();
        
        Ship ship = new Ship(WIDTH/2, HEIGHT/2);
        List<Asteroid> asteroids = new ArrayList<>();
        for(int i=0;i<5;i++){
            Random rand = new Random();
            Asteroid asteroid = new Asteroid(rand.nextInt(WIDTH/3),rand.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
        
        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
              
        Scene scene = new Scene(pane);
       
        scene.setOnKeyPressed((event) -> {
            pressedKeys.put(event.getCode(),true);
        });
        
        scene.setOnKeyReleased((event) -> {
            pressedKeys.put(event.getCode(),false);
        });
        
        new AnimationTimer(){
            
            public void handle(long now){
                
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
                
                asteroids.forEach(asteroid -> {
                    if(ship.collide(asteroid)){
                        stop();
                    }
                });
                
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                });

                projectiles.stream()
                    .filter(projectile -> !projectile.isAlive())
                    .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));

                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
                asteroids.removeAll(asteroids.stream()
                            .filter(asteroid -> !asteroid.isAlive())
                            .collect(Collectors.toList()));
                /*List<Projectile> projectilesToRemove = projectiles.stream().filter(projectile -> {
                    List<Asteroid> collisions = asteroids.stream()
                                                .filter(asteroid -> asteroid.collide(projectile))
                                                .collect(Collectors.toList());

                    if(collisions.isEmpty()) {
                        return false;
                    }

                    collisions.stream().forEach(collided -> {
                        asteroids.remove(collided);
                        pane.getChildren().remove(collided.getCharacter());
                    });

                    return true;
                }).collect(Collectors.toList());

                projectilesToRemove.forEach(projectile -> {
                    pane.getChildren().remove(projectile.getCharacter());
                    projectiles.remove(projectile);
                });*/
                
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });

                    if(!projectile.isAlive()) {
                        text.setText("Points: " + points.addAndGet(1000));
                    }
                });
                
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

    public static int partsCompleted() {
        // State how many parts you have completed using the return value of this method
        return 4;
    }

}
