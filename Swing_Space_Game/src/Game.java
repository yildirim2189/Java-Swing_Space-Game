
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author YILDIRIM
 */
class GameObjects{
    private int x,y; // coordinates of fire

    public GameObjects(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

public class Game extends JPanel implements KeyListener,ActionListener{
    
    Random random = new Random();
    Queue<Rectangle> queueOfEx = new LinkedList<>(); //
    private final Set<Integer> pressed = new HashSet<>();//
    private ArrayList<GameObjects> fires = new ArrayList<GameObjects>();//
    private BufferedImage spaceship,spaceback,spaceback2,spacefire, enemy , explosionImg, stars; 
    private int back1Y = -600; //
    private int fireDirY =2;
    private int ballX = 2; //
    private int ballDirX =2;//
    private int spaceshipX = 0;
    private int spaceshipDirX = 3;
    private boolean paused = false;
    private boolean started = false;
    private int optionSelected = 0;
    private int hit;

    private Rectangle explosion;
    File fontFile = new File("ARCADECLASSIC.TTF");
    private Font font;

    
    ActionListener myListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(pressed.size()>=1){
                for(Integer key : pressed){                  //   if(null != key) //  contains method may come here.
                    switch (key) {
                        case KeyEvent.VK_LEFT:
                            if(spaceshipX <= 0)
                                spaceshipX = 0;
                            else{
                                spaceshipX -= spaceshipDirX;
                            }   break;
                        case KeyEvent.VK_RIGHT:
                            if(spaceshipX >= 720)
                                spaceshipX = 720;
                            else{
                                spaceshipX += spaceshipDirX;
                            }   break;
                        
                        default:
                            break;
                    }
                }
            }  
        }
    };
    ActionListener fireListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(pressed.size()>=1){
                for(Integer key : pressed){
                    if(key == KeyEvent.VK_SPACE){
                        fires.add(new GameObjects(spaceshipX+3,513));
                       
                                     
                    }    
                }     
            }
            
            
           queueOfEx.poll();
        }
    };
    
    Timer timer = new Timer(10, this);             // General timer
    Timer keyTimer = new Timer(20, myListener);    // left-right key timer
    Timer fireTimer = new Timer(200,fireListener); // firetimer
   
    
    

    public Game() {
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            
        } catch (FontFormatException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        try { 
            
            spaceship = ImageIO.read(new FileImageInputStream(new File("spaceship1.png")));
            //spaceback = ImageIO.read(new FileImageInputStream(new File("spaceback.png")));
            spacefire = ImageIO.read(new FileImageInputStream(new File("spacefire.png")));
            enemy = ImageIO.read(new FileImageInputStream(new File("enemy.png")));
            explosionImg = ImageIO.read(new FileImageInputStream(new File("explosion.png")));
            stars = ImageIO.read(new FileImageInputStream(new File("stars2.jpg")));
            
            

         
            
            
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        timer.start();  
    }

   
    
    
    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs); 
        
         /* GAME FONT */
        Font FontArcade = font.deriveFont(Font.PLAIN, 30); // settings for Font - Arcade Classic
        grphcs.setFont(FontArcade);
       
        setBackground(Color.BLACK);
        
        
        
        if(!started){ // Game is not started. Main menu is showed.
            grphcs.setColor(Color.darkGray);
            if(optionSelected == 0)
            grphcs.fillRect(306, 264, 141, 30);  /* Highlight Option "New Game" */
            else 
            grphcs.fillRect(306, 315, 141, 30);  /* Highlight Option "Exit"     */
            
            grphcs.setColor(Color.GRAY);
            
            grphcs.drawString("NEW    GAME",309 , 290);
            grphcs.drawString("EXIT", 347 , 340);
        }  
        else{
            // grphcs.drawImage(spaceback, 0, back1Y, 800,2400,this);   // Background Image 1
            grphcs.drawImage(stars,0, back1Y,800,1200, this);
            grphcs.drawString("POINTS " + hit, 30, 30);
            grphcs.drawImage(spaceship, spaceshipX, 525, spaceship.getWidth()/10,spaceship.getHeight()/10,this); // draw Spaceship
            //grphcs.setColor(Color.RED);
            //grphcs.fillOval(ballX, 0, 20, 20); // draw Ball
            if(hit <= 100)
            grphcs.drawImage(enemy, ballX, 0,enemy.getWidth()/3,enemy.getHeight()/3, this);

            grphcs.setColor(Color.WHITE);
        
           

            if(paused){ // if pause draw "Pause" String 
                
                grphcs.drawString("GAME PAUSED",300, 290);
            }

            for(GameObjects fire : fires){
                if(hit<=100){
                Rectangle fireRec = new Rectangle(fire.getX(),fire.getY(),(spacefire.getWidth()/10),(spacefire.getHeight()/10));
                Rectangle enemyRec = new Rectangle(ballX,0,enemy.getWidth()/3,enemy.getHeight()/5);
                
                
                
                explosion = fireRec.intersection(enemyRec);
                
                if(fireRec.intersects(enemyRec)){   
                    fires.remove(fire);
                    
                    queueOfEx.offer(explosion);
                    hit++;
                }
                else if(fire.getY()<-15)
                    fires.remove(fire);
                //if(new Rectangle(fire.getX(),fire.getY(),4,4).intersects(new Rectangle(ballX,0,20,20)))
                }
            }
            
            if(!queueOfEx.isEmpty())
                for(Rectangle q : queueOfEx)
                    grphcs.drawImage(explosionImg,q.x, q.y, explosionImg.getWidth()/17,explosionImg.getHeight()/17,this);
                    //grphcs.fillOval(q.x, q.y, 30, 30);
          
            
            grphcs.setColor(Color.YELLOW);

            for (GameObjects fire : fires){  /* draw each fire */
                grphcs.drawImage(spacefire, fire.getX(), fire.getY(), spacefire.getWidth()/10, spacefire.getHeight()/10, this);
            }
            

            

            if(hit >= 100){
                if(hit<=160 && hit>= 100){
                    grphcs.drawImage(explosionImg, ballX, 0,100,100, this);
                    hit ++;
                }
                //timer.stop();
                //keyTimer.stop();
                //fireTimer.stop();
                String message = "Congratulations.. You won. \n";
                grphcs.drawString("GAME   OVER",310, 290);
                      
                //JOptionPane.showMessageDialog(this, message);
                //System.exit(0);

            }

        } 
    }

    @Override
    public void repaint() {
        super.repaint(); 
    }
    

    
   
    
    
    @Override
    public void keyTyped(KeyEvent ke) {
        // Not used method.
    }
    
    @Override
    public synchronized void keyPressed(KeyEvent ke) {
        keyTimer.start(); 
        fireTimer.start();
        if(!started){  // need to be modified.
            if(ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_DOWN)
                if(optionSelected == 0)
                    optionSelected = 1;
                else
                    optionSelected = 0;
            else if (ke.getKeyCode() == KeyEvent.VK_ENTER)
                if(optionSelected == 0)
                    started = true;
                else
                    System.exit(0);
        } 

        if(ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_RIGHT 
                || ke.getKeyCode() == KeyEvent.VK_SPACE)  // if LEFT/RIGHT/SPACE pressed.
            pressed.add(ke.getKeyCode());                 // add to pressed(key Set)
        else if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){   // if ESC pressed
            paused = !paused;
            if(paused){                                   // pause the game by stopping timers.
                repaint();                                // repaint once more, to draw String msg when paused.  
                timer.stop();
                keyTimer.stop();
            }
            else{                                         // if pressed again start timers.
                timer.start();
                keyTimer.start();
            }     
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent ke) {
       pressed.remove(ke.getKeyCode());                   // if a key release removes from the pressed(Key Set)
       if(pressed.isEmpty())                              // if no key pressed at the moment(if Key Set empty)
       keyTimer.stop();                                   // no need to work timer.
        
    }
    

    @Override
    public void actionPerformed(ActionEvent ae) {
       /* Ball move configuration */
       //ballDirX = random.nextInt(3);
       
        ballX += ballDirX;
       if(ballX >= 750){
           ballDirX = -ballDirX;
       }
       if(ballX <= 0){
           ballDirX = -ballDirX;
       }
       
       /* Fire configuration */
       for(GameObjects fire : fires){
           fire.setY(fire.getY() - fireDirY);
       }
       
       /* Background configuration */
       back1Y += 1;
       if(back1Y == 0)
           back1Y = -600;
       
      
      repaint(); // repaint each timer loop.

    }
   
}

