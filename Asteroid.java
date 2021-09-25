/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import javafx.scene.shape.Polygon;
import java.util.Random;

/**
 *
 * @author Keiner
 */
public class Asteroid extends Character {
    private double rotationalMovement;
    private boolean alive;
    
    public Asteroid(int x, int y){
        super(new PolygonFactory().createPolygon(), x, y);
        
        Random rand = new Random();
        super.getCharacter().setRotate(rand.nextInt(360));
        
        int accelerationAmount = 1 + rand.nextInt(10);
        for(int i=0;i<accelerationAmount;i++){
            accelerate();
        }
        this.alive = true;
        this.rotationalMovement = 0.5 + rand.nextDouble();
    }
    
    @Override
    public void move(){
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement);
    }
    
    @Override
    public void setAlive(boolean value){
        this.alive = value;
    }
    
    @Override
    public boolean isAlive(){
        return this.alive;
    }
}
