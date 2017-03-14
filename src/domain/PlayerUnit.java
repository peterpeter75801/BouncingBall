package domain;

import java.awt.Color;

/**
 * Player controlled unit in this game
 */
public class PlayerUnit {
   
    private final int DEFAULT_BALLS_RADIUS = 8;     // default radius is 8
    private final double DEFAULT_PLAYER_UNIT_SPEED = 6.0;    // default speed is 6.0
    private final int PLAYER_UNIT_INITIAL_POSITION_X = 320;  // initial x position is 320
    private final int PLAYER_UNIT_INITIAL_POSITION_Y = 460;  //   initial y position is 460
   
    private int radius;
    private int diameter;
    private double centerX;    // the x position of player unit's center
    private double centerY;    // the y position of player unit's center
    private double speedX;     // the x-axis speed
    private double speedY;     // the y-axis speed
    private int directionX;    // the x-axis direction. -1: left, 0: not move, 1: right
    private int directionY;    // the y-axis direction. -1: up, 0: not move, 1: down
    private Color color1;
    private Color color2;
   
    public PlayerUnit() {
        initialize();
    }
   
    public PlayerUnit(
        int theRadius, int theDiameter, double centerX, double centerY,
        double speedX, double speedY, int directionX, int directionY,
        Color color1, Color color2
    ) {
        radius = theRadius;
        diameter = theDiameter;
        this.centerX = centerX;
        this.centerY = centerY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.directionX = directionX;
        this.directionY = directionY;
        this.color1 = color1;
        this.color2 = color2;
    }
   
    /**
     * initialize player unit
     */
    private void initialize() {
        radius = DEFAULT_BALLS_RADIUS;
        diameter = radius * 2;
        centerX = PLAYER_UNIT_INITIAL_POSITION_X;
        centerY = PLAYER_UNIT_INITIAL_POSITION_Y;
        speedX = 0.0;
        speedY = 0.0;
        directionX = 0;
        directionY = 0;
        color1 = Color.BLUE;
        color2 = Color.YELLOW;
    }
   
    /**
     * set player unit configuration while first launching.
     */
    public void initialLaunch() {
        centerX = (double)PLAYER_UNIT_INITIAL_POSITION_X;
        centerY = (double)PLAYER_UNIT_INITIAL_POSITION_Y;
        speedX = 0.0;
        speedY = 0.0;
        directionX = 0;
        directionY = 0;
    }
   
    /**
     * move player unit
     */
    public void unitMove( int boundaryX, int boundaryY ) {
        // update player unit's speed
        speedX = 0.0;
        speedY = 0.0;
        if( directionX > 0 ) {
            speedX += DEFAULT_PLAYER_UNIT_SPEED;
        } else if( directionX < 0 ) {
            speedX -= DEFAULT_PLAYER_UNIT_SPEED;
        }
        if( directionY > 0 ) {
            speedY += DEFAULT_PLAYER_UNIT_SPEED;
        } else if( directionY < 0 ) {
            speedY -= DEFAULT_PLAYER_UNIT_SPEED;
        }
       
        // move player unit
        centerX += speedX;
        centerY += speedY;
       
        // check if player unit hit the wall
        if( (centerX - (double)radius) < 0.0 ) {
            centerX = (double)radius;
        } else if( (centerX + (double)radius) > (double)boundaryX ) {
            centerX = (double)(boundaryX - radius);
        }
        if( (centerY - (double)radius) < 0.0 ) {
            centerY = (double)radius;
        } else if( (centerY + (double)radius) > (double)boundaryY ) {
            centerY = (double)(boundaryY - radius);
        }
    }
   
    public Color getColor1() {
        return color1;
    }
   
    public Color getColor2() {
        return color2;
    }
   
    public double getCenterX() {
        return centerX;
    }
   
    public double getCenterY() {
        return centerY;
    }
   
    public int getDiameter() {
        return diameter;
    }
   
    public int getPositionX1() {
        return (int)(centerX - (double)radius);
    }
   
    public int getPositionY1() {
        return (int)(centerY - (double)radius);
    }
   
    public int getRadius() {
        return radius;
    }
   
    public void setDirectionX( int directionX ) {
        this.directionX = directionX;
    }
   
    public void setDirectionY( int directionY ) {
        this.directionY = directionY;
    }
}
