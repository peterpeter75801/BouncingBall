package view;

import domain.Balls;
import domain.PlayerUnit;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;

    private final static String FRAME_TITLE = new String( "Bouncing Ball" );
   
    private MainPanel mainPanel;
   
    private boolean upArrowKeyPressedFlag;
    private boolean downArrowKeyPressedFlag;
    private boolean leftArrowKeyPressedFlag;
    private boolean rightArrowKeyPressedFlag;
    
    public MainFrame() {
        super( FRAME_TITLE );
       
        mainPanel = new MainPanel();
        add( mainPanel );
       
        // initialize key event flags
        upArrowKeyPressedFlag = false;
        downArrowKeyPressedFlag = false;
        leftArrowKeyPressedFlag = false;
        rightArrowKeyPressedFlag = false;
       
        // add key listener
        addKeyListener( new MainFrameKeyListener() );
       
        // frame parameters configuration
        pack();
        setDefaultCloseOperation( EXIT_ON_CLOSE );
    }
   
    public boolean isStartButtonPushed() {
        return mainPanel.isStartButtonPushed();
    }
   
    public boolean isRestartButtonPushed() {
        return mainPanel.isRestartButtonPushed();
    }
   
    public boolean isUpArrowKeyPressed() {
        return upArrowKeyPressedFlag;
    }
   
    public boolean isDownArrowKeyPressed() {
        return downArrowKeyPressedFlag;
    }
   
    public boolean isLeftArrowKeyPressed() {
        return leftArrowKeyPressedFlag;
    }
   
    public boolean isRightArrowKeyPressed() {
        return rightArrowKeyPressedFlag;
    }
   
    public boolean isBothUpAndDownArrowKeyPressed() {
        return (upArrowKeyPressedFlag && downArrowKeyPressedFlag);
    }
   
    public boolean isBothLeftAndRightArrowKeyPressed() {
        return (leftArrowKeyPressedFlag && rightArrowKeyPressedFlag);
    }
   
    public void resetKeyPressedFlag() {
        upArrowKeyPressedFlag = false;
        downArrowKeyPressedFlag = false;
        leftArrowKeyPressedFlag = false;
        rightArrowKeyPressedFlag = false;
    }
   
    public void refreshScreen() {
        mainPanel.refreshScreen();
    }
   
    public void updateBallsAndPlayerUnit( Balls balls, PlayerUnit playerUnit ) {
        mainPanel.updateBallsAndPlayerUnit( balls, playerUnit );
    }
   
    public void updateGamePlayingTime( long gameElapsedTime ) {
        mainPanel.updateGamePlayingTime( gameElapsedTime );
    }
   
    public void switchInitialPhaseToDuringGame() {
        mainPanel.switchInitialPhaseToDuringGame();
    }
   
    public void switchDuringGamePhaseToGameOver() {
        mainPanel.switchDuringGamePhaseToGameOver();
    }
   
    public void switchGameOverPhaseToDuringGame() {
        mainPanel.switchGameOverPhaseToDuringGame();
    }
   
    /**
     * Listen the key event of user input, and set the corresponding flags.
     */
    private class MainFrameKeyListener implements KeyListener {

        @Override
        public void keyTyped( KeyEvent event ) {
        }
       
        @Override
        public void keyPressed( KeyEvent event ) {
            if( event.getKeyCode() == KeyEvent.VK_UP ) {
                upArrowKeyPressedFlag = true;
            } else if( event.getKeyCode() == KeyEvent.VK_DOWN ) {
                downArrowKeyPressedFlag = true;
            } else if( event.getKeyCode() == KeyEvent.VK_LEFT ) {
                leftArrowKeyPressedFlag = true;
            } else if( event.getKeyCode() == KeyEvent.VK_RIGHT ) {
                rightArrowKeyPressedFlag = true;
            }
        }
       
        @Override
        public void keyReleased( KeyEvent event ) {
            if( event.getKeyCode() == KeyEvent.VK_UP ) {
                upArrowKeyPressedFlag = false;
            } else if( event.getKeyCode() == KeyEvent.VK_DOWN ) {
                downArrowKeyPressedFlag = false;
            } else if( event.getKeyCode() == KeyEvent.VK_LEFT ) {
                leftArrowKeyPressedFlag = false;
            } else if( event.getKeyCode() == KeyEvent.VK_RIGHT ) {
                rightArrowKeyPressedFlag = false;
            }
        }
    }
}
