package core;

import view.MainFrame;
import domain.Balls;
import domain.PlayerUnit;

/**
 * Core of this game application
 */
public class BouncingBallCore {
    
    private final int FRAME_PER_SECOND = 60;    // 60 frames per second
    private final long ONE_FRAME_TIME = (long)(1000 / FRAME_PER_SECOND);
    private final long DEFAULT_BALL_LAUNCHING_TIMER = 5000;    // lauch a ball each 5000 mili-seconds (5 sec)
    private final int BOUNDARY_X = 640;         // |<--- 640 --->|
    private final int BOUNDARY_Y = 480;         // +-------------+ -+-
                                                // | game window |  | 480
                                                // +-------------+ -+-

    /**
     * Define the phases of this game:
     * - INITIAL:     Before player starting this game
     * - DURING_GAME: During player playing this game
     * - GAME_OVER:   After this game ended
     */
    public enum GamePhase{ INITIAL, DURING_GAME, GAME_OVER }
    private GamePhase gamePhase;           // phase of this game

    private MainFrame mainFrameRef;             // reference of the main frame
   
    private Balls balls;                        // ball entities in this game
    private PlayerUnit playerUnit;  // player controlled unit in this game
   
    private long frameBeginTime;                // record the time while a frame started
    private long frameEndTime;                  // record the time while a frame ended
    private long gameBeginTime;                 // record the time while a game started
    private long gameElapsedTime;               // record the current elapsed time of this game
   
    private long ballLaunchingTimer;            // the timer for ball launching
    private long timePoint1;                    // for timer computing
    private long timePoint2;                    // for timer computing
   
    public BouncingBallCore( MainFrame theMainFrameRef ) {
        mainFrameRef = theMainFrameRef;
       
        gamePhase = GamePhase.INITIAL;
       
        balls = new Balls();
        playerUnit = new PlayerUnit();
       
        frameBeginTime = 0;
        frameEndTime = 0;
        gameBeginTime = 0;
        gameElapsedTime = 0;
       
        ballLaunchingTimer = DEFAULT_BALL_LAUNCHING_TIMER;
        timePoint1 = 0;
        timePoint2 = 0;
       
        mainFrameRef.updateBallsAndPlayerUnit( balls, playerUnit );
    }
   
    /**
     * main procedure of this game application
     */
    public void execute() {
        while( true ) {
            switch( gamePhase ) {
                case INITIAL:
                    initialPhaseExecuting();
                    break;
                case DURING_GAME:
                    duringGamePhaseExecuting();
                    break;
                case GAME_OVER:
                    gameOverPhaseExecuting();
                    break;
                default:
                    break;
            }
        }
    }
   
    /**
     * executed while game phase is INITIAL.
     */
    private void initialPhaseExecuting() {
        sleep(100);     // polling every 0.1 second
       
        if( mainFrameRef.isStartButtonPushed() ) {
            switchInitialPhaseToDuringGame();
        }
    }
   
    /**
     * executed while game phase is DURING_GAME.
     */
    private void duringGamePhaseExecuting() {
        // record the frame begin time
        frameBeginTime = System.currentTimeMillis();
       
        // update game playing time
        gameElapsedTime = System.currentTimeMillis() - gameBeginTime;
        mainFrameRef.updateGamePlayingTime( gameElapsedTime );
       
        // balls moving
        balls.ballsMove( BOUNDARY_X, BOUNDARY_Y );
       
        // get user keyboard inputs, and set player unit's moving direction
        if( mainFrameRef.isBothUpAndDownArrowKeyPressed() ) {
            playerUnit.setDirectionY( 0 );
        } else if( mainFrameRef.isUpArrowKeyPressed() ) {
            playerUnit.setDirectionY( -1 );
        } else if( mainFrameRef.isDownArrowKeyPressed() ) {
            playerUnit.setDirectionY( 1 );
        } else {
            playerUnit.setDirectionY( 0 );
        }
       
        if( mainFrameRef.isBothLeftAndRightArrowKeyPressed() ) {
            playerUnit.setDirectionX( 0 );
        } else if( mainFrameRef.isLeftArrowKeyPressed() ) {
            playerUnit.setDirectionX( -1 );
        } else if( mainFrameRef.isRightArrowKeyPressed() ) {
            playerUnit.setDirectionX( 1 );
        } else {
            playerUnit.setDirectionX( 0 );
        }
       
        // player unit moving
        playerUnit.unitMove( BOUNDARY_X, BOUNDARY_Y );
       
        // Update balls' & player unit's states to main frame
        mainFrameRef.updateBallsAndPlayerUnit( balls, playerUnit );
       
        // determine if it needs to launch a ball
        timePoint1 = timePoint2;
        timePoint2 = System.currentTimeMillis();
        ballLaunchingTimer -= (timePoint2 - timePoint1);
        if( ballLaunchingTimer < 0 ) {
            balls.launchABall();
            ballLaunchingTimer += DEFAULT_BALL_LAUNCHING_TIMER;
        }
       
        // check if player unit was hit by any ball
        if( isBallCollided() ) {
            switchDuringGamePhaseToGameOver();
        }
       
        // refresh screen
        mainFrameRef.refreshScreen();
       
        // record the frame end time, and waiting for a frame time
        frameEndTime = System.currentTimeMillis();
        if( (frameEndTime - frameBeginTime) < ONE_FRAME_TIME ) {
            sleep( ONE_FRAME_TIME - (frameEndTime - frameBeginTime) );
        }
    }
   
    /**
     * executed while game phase is GAME_OVER.
     */
    private void gameOverPhaseExecuting() {
        sleep( 100 );   // polling every 0.1 second
       
        if( mainFrameRef.isRestartButtonPushed() ) {
            switchGameOverPhaseToDuringGame();
        }
    }
    
    /**
     * executed while game phase is switching from INITIAL to DURING_GAME.
     */
    private void switchInitialPhaseToDuringGame() {
        // notify mainPanel to change game phase
        mainFrameRef.switchInitialPhaseToDuringGame();
       
        // Launch the initial balls & player unit
        balls.ballsInitialLaunch();
        playerUnit.initialLaunch();
       
        // Update the current configuration of all balls and player unit to main frame
        mainFrameRef.updateBallsAndPlayerUnit( balls, playerUnit );
       
        // transfer the focus to mainFrame. (getting key input)
        mainFrameRef.requestFocus();
       
        // refresh screen
        mainFrameRef.refreshScreen();
       
        // waiting for a frame time
        sleep( ONE_FRAME_TIME );
       
        // record game begin time
        gameBeginTime = System.currentTimeMillis();
       
        // initialize the ball launching timer related parameters
        ballLaunchingTimer = DEFAULT_BALL_LAUNCHING_TIMER;
        timePoint1 = gameBeginTime;
        timePoint2 = gameBeginTime;
       
        // switch game phase to DURING_GAME
        gamePhase = GamePhase.DURING_GAME;
    }
   
    /**
     * executed while game phase is switching from DURING_GAME to GAME_OVER.
     */
    private void switchDuringGamePhaseToGameOver() {
        // notify mainPanel to change game phase
        mainFrameRef.switchDuringGamePhaseToGameOver();
       
        // reset key input
        mainFrameRef.resetKeyPressedFlag();
       
        // switch game phase to GAME_OVER
        gamePhase = GamePhase.GAME_OVER;
    }
   
    /**
     * executed while game phase is switching from GAME_OVER to INITIAL.
     */
    private void switchGameOverPhaseToDuringGame() {
        // notify mainPanel to change game phase
        mainFrameRef.switchGameOverPhaseToDuringGame();
       
        // Initially launch balls & player unit
        balls.ballsInitialLaunch();
        playerUnit.initialLaunch();
       
        // Update balls' & player unit's states to main frame
        mainFrameRef.updateBallsAndPlayerUnit( balls, playerUnit );
       
        // transfer focus to mainFrame. (getting key input)
        mainFrameRef.requestFocus();
       
        // refresh screen
        mainFrameRef.refreshScreen();
       
        // waiting for a frame time
        sleep( ONE_FRAME_TIME );
       
        // record game begin time
        gameBeginTime = System.currentTimeMillis();
       
        // initialize the ball launching timer related parameters
        ballLaunchingTimer = DEFAULT_BALL_LAUNCHING_TIMER;
        timePoint1 = gameBeginTime;
        timePoint2 = gameBeginTime;
       
        // switch game phase to DURING_GAME
        gamePhase = GamePhase.DURING_GAME;
    }
   
    /**
     * check if player unit was hit by any ball.
     */
    private boolean isBallCollided() {
        double centerDistence_x;
        double centerDistence_y;
        double centerDistence_min = (double)balls.getRadius() + (double)playerUnit.getRadius();
       
        for( int i = 0; i < balls.getNumberOfBalls(); i++ ) {
            centerDistence_x = balls.getBallsCenterX( i ) - playerUnit.getCenterX();
            centerDistence_y = balls.getballsCenterY( i ) - playerUnit.getCenterY();
           
            if( (centerDistence_x * centerDistence_x + centerDistence_y * centerDistence_y) <
                    (centerDistence_min * centerDistence_min) ) {
                return true;
            }
        }
       
        return false;
    }
   
    /**
     * pause execution for the specified millisecond.
     */
    private void sleep( long milisecond ) {
        try {
            Thread.sleep( milisecond );
        } catch( InterruptedException e ) {
            e.printStackTrace();
        }
    }
}
