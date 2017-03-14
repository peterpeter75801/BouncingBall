package domain;

import java.awt.Color;

/**
 * balls in this game.
 */
public class Balls {
    
    private final int DEFAULT_SIZE_OF_BALLS_ARRAY = 50;  // default max ball number is 50
    private final int DEFAULT_BALLS_RADIUS = 8;          // default radius is 8
    private final double LAUNCH_POSITION_X = 320.0;      // x position of balls launching is 320
    private final double LAUNCH_POSITION_Y = 30.0;       // y position of balls launching is 30
    private final double BALLS_MIN_SPEED_IN_ONE_DIRECT = 1.0;  // minimum speed is 1.0
    private final double BALLS_MAX_SPEED_IN_ONE_DIRECT = 3.0;  // max speed is 3.0
    private final double BALLS_SPEED_RANGE = BALLS_MAX_SPEED_IN_ONE_DIRECT - BALLS_MIN_SPEED_IN_ONE_DIRECT;
   
    private int numberOfBalls;          // current balls number in this game
    private int sizeOfBallsArray;       // current max balls number
    private int radius;
    private int diameter;
    private double ballsCenterX[];     // x position of each balls' center
    private double ballsCenterY[];     // y position of each balls' center
    private double ballsSpeedX[];      // the x-axis speed of each balls
    private double ballsSpeedY[];      // the y-axis speed of each balls
    private Color ballsColor[];         // color of each balls
    private Color ballsBorderColor[];   // color of each balls' border
   
    public Balls() {
        initialize();
    }
   
    public Balls(
        int theNumberOfBalls, int theSizeOfBallsArray, int theRadius, int theDiameter,
        double[] ballsCenterX, double[] ballsCenterY,
        double[] ballsSpeedX, double[] ballsSpeedY,
        Color[] theBallsColor, Color[] theBallsBorderColor
    ) {
        initialize();
       
        numberOfBalls = theNumberOfBalls;
        sizeOfBallsArray = theSizeOfBallsArray;
        radius = theRadius;
        diameter = theDiameter;
   
        for( int i = 0; i < theNumberOfBalls; i++ ) {
            this.ballsCenterX[ i ] = ballsCenterX[ i ];
            this.ballsCenterY[ i ] = ballsCenterY[ i ];
            this.ballsSpeedX[ i ] = ballsSpeedX[ i ];
            this.ballsSpeedY[ i ] = ballsSpeedY[ i ];
            ballsColor[ i ] = theBallsColor[ i ];
            ballsBorderColor[ i ] = theBallsBorderColor[ i ];
        }
    }
   
    public Object clone() {
        Balls copy = new Balls(
            numberOfBalls, sizeOfBallsArray, radius, diameter,
            ballsCenterX, ballsCenterY, ballsSpeedX, ballsSpeedY,
            ballsColor, ballsBorderColor
        );
       
        return copy;
    }

    /**
     * initialize balls in this game.
     */
    private void initialize() {
        numberOfBalls = 0;
        sizeOfBallsArray = DEFAULT_SIZE_OF_BALLS_ARRAY;
        radius = DEFAULT_BALLS_RADIUS;
        diameter = radius * 2;
        ballsCenterX = new double[ sizeOfBallsArray ];
        ballsCenterY = new double[ sizeOfBallsArray ];
        ballsSpeedX = new double[ sizeOfBallsArray ];
        ballsSpeedY = new double[ sizeOfBallsArray ];
        ballsColor = new Color[ sizeOfBallsArray ];
        ballsBorderColor = new Color[ sizeOfBallsArray ];
    }
   
    /**
     * set balls configuration while first launching.
     */
    public void ballsInitialLaunch() {
        numberOfBalls = 5;
        for( int i = 0; i < numberOfBalls; i++ ) {
            ballsCenterX[ i ] = LAUNCH_POSITION_X;
            ballsCenterY[ i ] = LAUNCH_POSITION_Y;
            ballsSpeedX[ i ] = Math.random() * BALLS_SPEED_RANGE + BALLS_MIN_SPEED_IN_ONE_DIRECT;
            ballsSpeedY[ i ] = Math.random() * BALLS_SPEED_RANGE + BALLS_MIN_SPEED_IN_ONE_DIRECT;
            ballsColor[ i ] = randomBallsColor();
            ballsBorderColor[ i ] = randomBallsBorderColor();
            randomBallsDirectionAndSpeed( i );
        }
    }
   
    /**
     * move each balls.
     */
    public void ballsMove( int boundaryX, int boundaryY ) {
        for( int i = 0; i < numberOfBalls; i++ ) {
            ballsCenterX[ i ] = ballsCenterX[ i ] + ballsSpeedX[ i ];
            ballsCenterY[ i ] = ballsCenterY[ i ] + ballsSpeedY[ i ];
        }
       
        checkIfTheballsHitTheWallAndAdjust( boundaryX, boundaryY );
    }
   
    /**
     *   Check if there's any ball hit the wall (the boundary of game window),
     *   and adjust the direction of the ball.
     *   For example:
     *   +----------+       +----------+             +----------+
     *   |          |       |          |             |        o |
     *   |          |  -->  |         o|      -->    |          |
     *   |        o |       |          |             |          |
     *   +----------+       +----------+             +----------+
     *   speed: x = 2.0     speed: x = 2.0           speed: x = -2.0
     *          y = -2.0           y = -2.0                 y = -2.0
     *                     (Hit the right boundary)
     */
    private void checkIfTheballsHitTheWallAndAdjust( int boundaryX, int boundaryY ) {
        double reboundDistance;
        double ballsPositionX1;
        double ballsPositionY1;
        double ballsPositionX2;
        double ballsPositionY2;
       
        for( int i = 0; i < numberOfBalls; i++ ) {
            /* check x vector */
            ballsPositionX1 = ballsCenterX[ i ] - (double)radius;
            ballsPositionX2 = ballsCenterX[ i ] + (double)radius;
            if( ballsPositionX1 < 0 ) {
                reboundDistance = 0 - ballsPositionX1;
                ballsCenterX[ i ] = (double)radius + reboundDistance;
                ballsSpeedX[ i ] = ballsSpeedX[ i ] * (-1.0);
            } else if( ballsPositionX2 > boundaryX ) {
                reboundDistance = ballsPositionX2 - boundaryX;
                ballsCenterX[ i ] = boundaryX - (double)radius - reboundDistance;
                ballsSpeedX[ i ] = ballsSpeedX[ i ] * (-1.0);
            }
           
            /* check y vector */
            ballsPositionY1 = ballsCenterY[ i ] - (double)radius;
            ballsPositionY2 = ballsCenterY[ i ] + (double)radius;
            if( ballsPositionY1 < 0 ) {
                reboundDistance = 0 - ballsPositionY1;
                ballsCenterY[ i ] = (double)radius + reboundDistance;
                ballsSpeedY[ i ] = ballsSpeedY[ i ] * (-1.0);
            } else if( ballsPositionY2 > boundaryY ) {
                reboundDistance = ballsPositionY2 - boundaryY;
                ballsCenterY[ i ] = boundaryY - (double)radius - reboundDistance;
                ballsSpeedY[ i ] = ballsSpeedY[ i ] * (-1.0);
            }
        }
    }
    
    /**
     * launch a new ball in this game.
     */
    public void launchABall() {
        if( numberOfBalls >= sizeOfBallsArray ) {
            return;
        }
       
        ballsCenterX[ numberOfBalls ] = LAUNCH_POSITION_X;
        ballsCenterY[ numberOfBalls ] = LAUNCH_POSITION_Y;
        ballsSpeedX[ numberOfBalls ] = Math.random() * BALLS_SPEED_RANGE + BALLS_MIN_SPEED_IN_ONE_DIRECT;
        ballsSpeedY[ numberOfBalls ] = Math.random() * BALLS_SPEED_RANGE + BALLS_MIN_SPEED_IN_ONE_DIRECT;
        ballsColor[ numberOfBalls ] = randomBallsColor();
        ballsBorderColor[ numberOfBalls ] = randomBallsBorderColor();
        randomBallsDirectionAndSpeed( numberOfBalls );
       
        numberOfBalls++;
    }
   
    /**
     * Randomly generate a ball's border color.
     */
    private Color randomBallsBorderColor() {
        int colorSwitch = (int)(Math.random() * 4.0); // values: 0, 1, 2, 3
        Color colorToReturn;
       
        switch( colorSwitch ) {
            case 0:
                colorToReturn = Color.BLACK;
                break;
            case 1:
                colorToReturn = Color.RED;
                break;
            case 2:
                colorToReturn = Color.GREEN;
                break;
            case 3:
                colorToReturn = Color.BLUE;
                break;
            default:
                colorToReturn = Color.BLACK;
                break;
        }
       
        return colorToReturn;
    }
   
    /**
     * Randomly generate a ball's color.
     */
    private Color randomBallsColor() {
        int red = (int)(Math.random() * 256.0); // 0 <= color < 256
        int green = (int)(Math.random() * 256.0); // 0 <= color < 256
        int blue = (int)(Math.random() * 256.0); // 0 <= color < 256
       
        return new Color( red, green, blue );
    }
   
    /**
     * randomly generate ball's x-axis and y-axis speed.
     */
    private void randomBallsDirectionAndSpeed( int ballIndex ) {
        int quadrant = (int)(Math.random() * 2.0 + 1.0);    // values: 1, 2
       
        if( ballsSpeedY[ ballIndex ] < 0.0 )
            ballsSpeedY[ ballIndex ] = ballsSpeedY[ ballIndex ] * (-1.0); // make y vector speed positive
       
        switch( quadrant ) {
            case 1:
                if( ballsSpeedX[ ballIndex ] < 0.0 )
                    ballsSpeedX[ ballIndex ] = ballsSpeedX[ ballIndex ] * (-1.0); // make x vector speed positive
                break;
            case 2:
                if( ballsSpeedX[ ballIndex ] > 0.0 )
                    ballsSpeedX[ ballIndex ] = ballsSpeedX[ ballIndex ] * (-1.0); // make x vector speed negative
                break;
            default:
                break;
        }
    }
   
    public Color getBallsBorderColor( int index ) {
        if( index < 0 || index >= numberOfBalls ) {
            return Color.BLACK;
        } else {
            return ballsBorderColor[ index ];
        }
    }
   
    public Color getBallsColor( int index ) {
        if( index < 0 || index >= numberOfBalls ) {
            return Color.BLACK;
        } else {
            return ballsColor[ index ];
        }
    }
   
    public double getBallsCenterX( int index ) {
        if( index < 0 || index >= numberOfBalls ) {
            return 0.0;
        } else {
            return ballsCenterX[ index ];
        }
    }
   
    public double getballsCenterY( int index ) {
        if( index < 0 || index >= numberOfBalls ) {
            return 0.0;
        } else {
            return ballsCenterY[ index ];
        }
    }

    public int getBallsPositionX1( int index ) {
        if( index < 0 || index >= numberOfBalls ) {
            return 0;
        }
       
        return (int)(ballsCenterX[ index ] - (double)radius);
    }
   
    public int getBallsPositionY1( int index ) {
        if( index < 0 || index >= numberOfBalls )
            return 0;
       
        return (int)(ballsCenterY[ index ] - (double)radius);
    }
   
    public int getDiameter() {
        return diameter;
    }
   
    public int getNumberOfBalls() {
        return numberOfBalls;
    }
   
    public int getRadius() {
        return radius;
    }
}
