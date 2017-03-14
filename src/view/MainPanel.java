package view;

import domain.Balls;
import domain.PlayerUnit;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private Balls balls;
    private PlayerUnit playerUnit;

    /**
     * Define the phases of this game:
     * - INITIAL:     Before player starting this game
     * - DURING_GAME: During player playing this game
     * - GAME_OVER:   After this game ended
     */
    public enum GamePhase{ INITIAL, DURING_GAME, GAME_OVER }
    private GamePhase gamePhase;           // phase of this game

    private JButton startButton;
    private JButton restartButton;
    private JLabel gameTimeLabel;
    private JLabel gameTimeValueLabel;
    private JLabel ballsNumberLabel;
    private JLabel ballsNumberValueLabel;
    private JLabel fpsLabel;
    private JLabel fpsValueLabel;
   
    private boolean startButtonPushedFlag;
    private boolean restartButtonPushedFlag;
   
    // parameters of fps calculating
    private final long DEFAULT_FPS_CALCULATING_TIMER_VALUE = 1000;   // milisecond
    private long fpsCalculatingTimer;
    private long currentSystemTime;
    private long previousSystemTime;
    private int frameCounter;
    private int currentFPS;
   
    public MainPanel() {
        setLayout( null );
        setPreferredSize( new Dimension( 640, 480 ) );
       
        startButton = new JButton( "start" );
        startButton.setBounds( 290, 226, 61, 28 );
        startButton.addActionListener( new ButtonActionListener() );
        add( startButton );
       
        restartButton = new JButton( "restart" );
        restartButton.setBounds( 280, 226, 80, 28 );
        restartButton.addActionListener( new ButtonActionListener() );
        restartButton.setEnabled( false );
        restartButton.setVisible( false );
        add( restartButton );
       
        gameTimeLabel = new JLabel( "time " );
        gameTimeLabel.setBounds( 286, 5, 30, 20 );
        add( gameTimeLabel );
       
        gameTimeValueLabel = new JLabel( "00:00" );
        gameTimeValueLabel.setBounds( 321, 5, 33, 20 );
        add( gameTimeValueLabel );
       
        ballsNumberLabel = new JLabel( "number of balls:" );
        ballsNumberLabel.setBounds( 10, 5, 93, 20 );
        add( ballsNumberLabel );
       
        ballsNumberValueLabel = new JLabel( " 0" );
        ballsNumberValueLabel.setBounds( 108, 5, 16, 20 );
        add( ballsNumberValueLabel );
       
        fpsValueLabel = new JLabel( " 0" );
        fpsValueLabel.setBounds( 590, 5, 16, 20 );
        add( fpsValueLabel );
       
        fpsLabel = new JLabel( "fps" );
        fpsLabel.setBounds( 611, 5, 20, 20 );
        add( fpsLabel );
       
        balls = new Balls();
        playerUnit = new PlayerUnit();
       
        fpsCalculatingTimer = DEFAULT_FPS_CALCULATING_TIMER_VALUE;
        currentSystemTime = 0;
        previousSystemTime = 0;
        frameCounter = 0;
        currentFPS = 0;
       
        startButtonPushedFlag = false;
        restartButtonPushedFlag = false;
       
        gamePhase = GamePhase.INITIAL;
    }
   
    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
       
        Graphics2D g2d = (Graphics2D)g;
       
        // drawing balls
        for( int i = 0; i < balls.getNumberOfBalls(); i++ ) {
            // drawing the balls' body
            g.setColor( balls.getBallsColor( i ) );
            g.fillOval(
                balls.getBallsPositionX1( i ), balls.getBallsPositionY1( i ),
                balls.getDiameter(), balls.getDiameter()
            );
            // drawing the balls' border
            g.setColor( balls.getBallsBorderColor( i ) );
            g.drawOval(
                balls.getBallsPositionX1( i ), balls.getBallsPositionY1( i ),
                balls.getDiameter(), balls.getDiameter()
            );
        }
       
        // drawing the controlled unit
        g2d.setPaint(
            new GradientPaint(
                playerUnit.getPositionX1(),
                playerUnit.getPositionY1(),
                playerUnit.getColor1(),
                playerUnit.getPositionX1() + 8,
                playerUnit.getPositionY1() + 12,
                playerUnit.getColor2()
            )
        );
        g2d.fillOval(
            playerUnit.getPositionX1(),
            playerUnit.getPositionY1(),
            playerUnit.getDiameter(),
            playerUnit.getDiameter()
        );
    }
   
    /**
     * Calculate FPS(frames per second) in this game.
     * Update fps value every 1000 milisecond. 
     * And count total frame number in this game.
     */
    private void calculateFPS() {
        frameCounter++;
        previousSystemTime = currentSystemTime;
        currentSystemTime = System.currentTimeMillis();
        fpsCalculatingTimer = fpsCalculatingTimer - (currentSystemTime - previousSystemTime);
        if( fpsCalculatingTimer <= 0 ) {
            fpsCalculatingTimer = fpsCalculatingTimer + DEFAULT_FPS_CALCULATING_TIMER_VALUE;
            currentFPS = (frameCounter * 1000) / (int)DEFAULT_FPS_CALCULATING_TIMER_VALUE;
            frameCounter = 0;
            fpsValueLabel.setText( String.format( "%d", currentFPS ) );
        }
    }
   
    public boolean isStartButtonPushed() {
        return startButtonPushedFlag;
    }
   
    public boolean isRestartButtonPushed() {
        return restartButtonPushedFlag;
    }
   
    /**
     * Refresh screen and update fps.
     */
    public void refreshScreen() {
        if( gamePhase == GamePhase.DURING_GAME ) {
            calculateFPS();
    }
       
        repaint();
    }
    
    /**
     * Update balls and player unit displaying.
     */
    public void updateBallsAndPlayerUnit( Balls balls, PlayerUnit playerUnit ) {
        this.balls = balls;
        this.playerUnit = playerUnit;
       
        ballsNumberValueLabel.setText(
            String.format( "%d", balls.getNumberOfBalls() )
        );
    }
    
    /**
     * Update game playing time.
     */
    public void updateGamePlayingTime( long gameElapsedTime ) {
        gameTimeValueLabel.setText(
            String.format(
                "%02d:%02d",
                (int)( (gameElapsedTime / 1000) / 60 ),  // minute
                (int)( (gameElapsedTime / 1000) % 60 )   // second
            )
        );
    }
   
    /**
     * executed while game phase is switching from INITIAL to DURING_GAME.
     */
    public void switchInitialPhaseToDuringGame() {
        fpsCalculatingTimer = DEFAULT_FPS_CALCULATING_TIMER_VALUE;
        currentSystemTime = System.currentTimeMillis();
        previousSystemTime = 0;
        frameCounter = 0;
        currentFPS = 0;
   
        startButtonPushedFlag = false;
        startButton.setEnabled( false );
        startButton.setVisible( false );
       
        gamePhase = GamePhase.DURING_GAME;
    }
   
    /**
     * executed while game phase is switching from DURING_GAME to GAME_OVER.
     */
    public void switchDuringGamePhaseToGameOver() {
        restartButton.setEnabled( true );
        restartButton.setVisible( true );
        restartButton.requestFocus();
       
        gamePhase = GamePhase.GAME_OVER;
    }
   
    /**
     * executed while game phase is switching from GAME_OVER to DURING_GAME.
     */
    public void switchGameOverPhaseToDuringGame() {
        fpsCalculatingTimer = DEFAULT_FPS_CALCULATING_TIMER_VALUE;
        currentSystemTime = System.currentTimeMillis();
        previousSystemTime = 0;
        frameCounter = 0;
        currentFPS = 0;
       
        restartButtonPushedFlag = false;
        restartButton.setEnabled( false );
        restartButton.setVisible( false );
       
        gamePhase = GamePhase.DURING_GAME;
    }
   
    /**
     * Handle start button and restart button event.
     */
    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            if( event.getSource() == startButton ) {
                startButtonPushedFlag = true;
            } else if( event.getSource() == restartButton ) {
                restartButtonPushedFlag = true;
            }
        }
    }
}
