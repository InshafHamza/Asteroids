/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import javafx.scene.shape.Polygon;

/**
 *
 * @author Keiner
 */
public class Projectile extends Character {
    private boolean alive;
    
    public Projectile(int x, int y){
        super(new Polygon(2,-2,2,2,-2,2,-2,-2), x, y);
        this.alive = true;
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
