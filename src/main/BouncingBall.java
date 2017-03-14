package main;

import core.BouncingBallCore;
import view.MainFrame;

public class BouncingBall {

    public static void main( String args[] ) {
        MainFrame mainFrame = new MainFrame();
        BouncingBallCore bouncingBallCore = new BouncingBallCore( mainFrame );
       
        mainFrame.setVisible( true );
        bouncingBallCore.execute();
    }
}
