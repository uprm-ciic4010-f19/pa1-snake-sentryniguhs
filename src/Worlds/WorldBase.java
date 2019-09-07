package Worlds;

import Game.Entities.Dynamic.Player;
import Game.Entities.Dynamic.Tail;
import Game.Entities.Static.Apple;
import Main.Handler;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;


/**
 * Created by AlexVR on 7/2/2018.
 */
public abstract class WorldBase {

    //How many pixels are from left to right
    //How many pixels are from top to bottom
    //Must be equal
    public int GridWidthHeightPixelCount;

    //automatically calculated, depends on previous input.
    //The size of each box, the size of each box will be GridPixelsize x GridPixelsize.
    public int GridPixelsize;

    public Player player;

    protected Handler handler;


    public Boolean appleOnBoard;
    protected Apple apple;
    public Boolean[][] appleLocation;
    

    public Boolean[][] playerLocation;

    public LinkedList<Tail> body = new LinkedList<>();


    public WorldBase(Handler handler){
        this.handler = handler;

        appleOnBoard = false;

    }
    public boolean isGoodCheck() {
    	return apple.isGood();
    }
    public void tick(){



    }

    public void render(Graphics g){
//    	g.setColor(Color.BLACK);
//    	Font currentFont = g.getFont();
//    	Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.0F);
//    	g.setFont(newFont);
//    	
//    	if (player.isJustAte()) {
//    		g.drawString(player.currentScore() + "", 875, 360);
//    	}
    		
        
//    	for (int i = 0; i <= 800; i = i + GridPixelsize) {
        	// Commented out white lines
//            g.setColor(Color.BLACK);
//            g.drawLine(0, i, handler.getWidth() , i);
//            g.drawLine(i,0,i,handler.getHeight());

//        }



    }

}
