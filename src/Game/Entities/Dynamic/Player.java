package Game.Entities.Dynamic;

import Main.GameSetUp;
import Main.Handler;
import UI.UIImageButton;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Display.DisplayScreen;
import Game.Entities.Static.Apple;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {
  
  
 //created new instance variable N for speed counter stuff
    private static int n;
	public int lenght;
    public boolean justAte;
    private Handler handler;

    public Boolean goodApple;
    
    public int xCoord;
    public int yCoord;

    public int moveCounter;
    public int stepAmount;

    public String direction;//is your first name one?
    
    private int currentScore;
    
    private Player firstTail;
    private Tail tailIteration;

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght= 1;
       currentScore = 0;
       stepAmount = 0;
       goodApple = true;
       n = 0;
    }

    public void tick(){
    	//created variable n witch will increase or decrease when + or - is pressed and then will be added to the 
    	//original speed to end up with a higher or lower speed
    	if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ADD)){
    		n++;
    	}
    	if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SUBTRACT)){
    		n--;
    	}
    	
        moveCounter += 3 + n;
        // altered the move speed here to triple the usual amount
        if(moveCounter>=5) {
            checkCollisionAndMove();
            moveCounter=0;
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
            direction="Up";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
            direction="Down";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)){
            direction="Left";            
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)){
            direction="Right";            
        }
        // if N is ever pressed, the program will act like it just ate a fruit (check modifications to the eat() method below
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
        	Eat();
        }

    }

    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        if (stepAmount >=1000 && goodApple) {
        	goodApple = !goodApple;
        	stepAmount = 0;
        }
        switch (direction){
            case "Left":
                if(xCoord==0){
                    kill();
                }else{
                    xCoord--;
                    stepAmount++;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    kill();
                }else{
                    xCoord++;
                    stepAmount++;
                }
                break;
            case "Up":
                if(yCoord==0){
                    kill();
                }else{
                    yCoord--;
                    stepAmount++;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    kill();
                }else{
                    yCoord++;
                    stepAmount++;
                }
                break;
        }
        firstTail = handler.getWorld().player;
        for (int i = 0; i < handler.getWorld().body.size(); i++) {
        	tailIteration = handler.getWorld().body.get(i);
        	if (firstTail.xCoord == tailIteration.x && firstTail.yCoord == tailIteration.y)
        		GameOver();
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;
        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));
        }
    }
    public void render(Graphics g,Boolean[][] playeLocation){
        Random r = new Random();
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                g.setColor(Color.GREEN);
   
                if(playeLocation[i][j]){
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
                if (handler.getWorld().appleLocation[i][j] && goodApple) {
                	g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
                else if(handler.getWorld().appleLocation[i][j] && !goodApple) {
                	Color badAppl = new Color(165,42,42);
                	g.setColor(badAppl);
                	g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }

            }
        }
        g.setColor(Color.BLACK);
    	Font currentFont = g.getFont();
    	Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.0F);
    	g.setFont(newFont);
        g.drawString(currentScore + "", 885, 360);

    }

    public void Eat(){
        lenght++;
        Tail tail= null;
        // 
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N) == false) {
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        }
        switch (direction){
            case "Left":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail =new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }

                }
                break;
            case "Right":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail=new Tail(this.xCoord-1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail=new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                    }

                }
                break;
            case "Up":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
            case "Down":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        } System.out.println("Tu biscochito");
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }
        //Updates score depending on whether the apple is 'good' or 'bad' and deletes tail if apple is 'bad'
        if(goodApple)
        	currentScore = currentScore + (int)Math.sqrt((2 * currentScore) + 1);
        else {
        	currentScore = currentScore - (int)(Math.sqrt(2) + Math.sqrt(2 *(currentScore + Math.sqrt((2 * currentScore) + 1))));
        	handler.getWorld().body.removeLast();
        	handler.getWorld().playerLocation[tail.x][tail.y] = false;
        }
        //If the score reaches a number less than zero, a Game Over screen is displayed
        if (currentScore < 0)
        	GameOver();
        handler.getWorld().body.addLast(tail);
        handler.getWorld().playerLocation[tail.x][tail.y] = true;
        goodApple = handler.getWorld().isGoodCheck();
    }

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;

            }
        }  
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
    public void GameOver() {
    	//Creates new visible frame 
    	JFrame frame = new JFrame("Game Over");
    	frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        //Creating the canvas.
        Canvas canvas = new Canvas();
        canvas.setSize(1000, 800);
        canvas.setBackground(Color.RED);
        canvas.setVisible(true);
        canvas.setFocusable(false);

        //Putting it all together.
        frame.add(canvas);
        canvas.createBufferStrategy(3);
        boolean running = true;

        BufferStrategy bufferStrategy;
        Graphics graphics;
        //Chooses random failure text 
        String[] texts = {"You really suck", "Wow, that was pretty bad", "Did your dad leave you and your mom alone?",
        				"How old were you when you became a failure?", "God really made a mistake when you came along",
        				"Certainly looks like the birth control pills didn't work", "You monkey...", "You're such a dissapointment",
        				"You play like a Nazi", "You just justified Vietnam War's casualties"};
        Random randomText = new Random();
        int randNum = randomText.nextInt(9);
        
        while (running) {
            bufferStrategy = canvas.getBufferStrategy();
            graphics = bufferStrategy.getDrawGraphics();
            graphics.clearRect(0, 0, 1000, 800);
            
            //Draws RoundRect
            graphics.setColor(Color.WHITE);
            graphics.fillRoundRect(0, 0, 1000, 350, 100, 100);
            
            //Writes "GAME OVER"
            graphics.setColor(Color.BLACK);
            Font currentFont = graphics.getFont();
        	Font newFont = currentFont.deriveFont(currentFont.getSize() * 7.0F);
        	graphics.setFont(newFont);
            graphics.drawString("GAME OVER", 230, 200);
            
            //Writes mean text
        	newFont = currentFont.deriveFont(currentFont.getSize() * 2.5F);
        	graphics.setFont(newFont);
            graphics.drawString(texts[randNum], 125, 345);

            bufferStrategy.show();
            graphics.dispose();
        }
    }
}
