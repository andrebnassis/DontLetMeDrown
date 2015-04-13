import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.core.*; 
import ddf.minim.*; 
import ddf.minim.signals.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Game extends PApplet {







PImage [] bg; //Backgroung Images (Water squares).
int bg_count = 0; //Counter used to change the background Images (That gives the effect of water).

PImage StartScreen;
PImage MenuScreen;
int stage = 0; //Variable to control what screen will be shown (StartScreen, MenuScreen or bg).

PImage GameOverImg; 


Boat boat; 
Shark shark; 
Victim victim;

int score = -1; //Counts the amount of victims saved.
int bestScore = 0; //Keeps the highest score.

PFont f; //Used to load the font.

boolean[] keys; //Array used to detect the pressed keys.

boolean isSafe = true;  //Used to control the victims creation.
boolean GameOver = false; 

/*------------Audio Minim Library-----------*/
Minim minim;
AudioSample EffectPressEnter; 
AudioSample [] EffectScore;
AudioSample EffectSharkAttack;
AudioSample EffectDrown;

AudioPlayer MusicStartScreen;
AudioPlayer MusicGameScreen;
AudioPlayer MusicGameOver;

/*---------End Audio Minim Library-----------*/

//This function fill the background with the water-squares-images and keeps the water-animation by changing the water-squares.
public void drawBg(PImage[] bg)
{

  //Water effect: Change background images.
  for (int i = 0; i<=width/32; i++)
  {
    for (int j = 0; j<= height/32; j++) {

      image(bg[bg_count/30], i*32, j*32);
    }
  }
  bg_count = (bg_count + 1)%120;
}

//setup() run only in the first time that the game starts.
public void setup() {

  size(800, 640); //ScreenSize.
  frameRate(60);  //Game frame Rate.

  //Array with the water-square-images.
  bg = new PImage[] {
    loadImage("Images/water0.png"), loadImage("Images/water1.png"), loadImage("Images/water2.png"), loadImage("Images/water3.png")
    };

    //Initializing the boat and the shark.
    boat = new Boat((int)random(width/2, width), (int)random(height/2, height));
  shark = new Shark((int)random(0, width/2), (int)random(0, height/2));

  //Array with all the ASC's numbers that could be pressed by the keyboard.
  keys = new boolean[255];

  //Loading the FontStyle.
  f = loadFont("Font/Cambria-Bold-32.vlw");

  //Loading the Screens into its variables.
  StartScreen = loadImage("Images/title.png");
  MenuScreen = loadImage("Images/secondtitle.png");
  GameOverImg = loadImage("Images/gameover.png"); 

  /*----Loading the Audio of the game into its variables-----*/
  minim = new Minim(this);
  EffectPressEnter = minim.loadSample("Sound/dropinwater2.mp3");
  EffectScore = new AudioSample[] {
    minim.loadSample("Sound/thanks_female.mp3"), minim.loadSample("Sound/thanks_male.mp3")
    };
    EffectSharkAttack = minim.loadSample("Sound/sharckattack.mp3");
  EffectDrown = minim.loadSample("Sound/drowning.mp3");

  MusicStartScreen = minim.loadFile("Sound/menu.mp3");
  MusicGameScreen = minim.loadFile("Sound/game.mp3");
  /*----End Loading the Audio of the game-----*/
}



/*-----------------BoatMovement------------------*/
public void keyPressed() {
  keys[keyCode] = true;
}

public void keyReleased() {
  keys[keyCode] = false;
}


public void updatePosition() {
  if (keys[LEFT]) { 
    boat.UpdatePos(-2, 0);
  }
  if (keys[RIGHT]) { 
    boat.UpdatePos(2, 0);
  }
  if (keys[UP]) { 
    boat.UpdatePos(0, -2);
  }
  if (keys[DOWN]) { 
    boat.UpdatePos(0, +2);
  }
}
/*-----------------BoatMovementEnd------------------*/

//draw() function runs the number of frameRate variable per second. 
public void draw() {

  //Sets the color used for the background of the Processing window.
  background(255);

  //If stage == 0, then is StartScreen Mode.
  if (stage == 0) {

    //If the music stops, then start again.
    if (!MusicStartScreen.isPlaying()) 
    {   
      MusicStartScreen.loop();
    }
    //Put the StartScreen background image.
    image(StartScreen, 0, 0);

    //If press ENTER
    if (keys[ENTER])
    {
      EffectPressEnter.trigger(); //Play the Enter button sound effect.
      keys[ENTER] = false; //Put the ENTER key available to be pressed again.
      stage++; //Jump to the next Screen.
    }
  }
  // If stage == 1, then is InstructionScreen Mode. 
  else if (stage == 1) {
    
    //Put the Menu background image.
    image(MenuScreen, 0, 0);
    
    //If press ENTER
    if (keys[ENTER])
    {
      MusicStartScreen.pause(); //Pause the music.
      EffectPressEnter.trigger(); //Play the Enter button sound effect.
      stage++; //Jump to the next Screen.
    }
  } 
  else {
    //If the GameMode Music is not playing
    if (!MusicGameScreen.isPlaying()) {

      MusicGameScreen.setGain(-20.0f); //Put the gain to -20dB.
      MusicGameScreen.loop(); // Starts the GameMode Music.
    }
    //If is not GameOver
    if (!GameOver)
    {
      
      MusicGameScreen.setGain(-20.0f); //Put the gain to -20dB.

      drawBg(bg); //Put the GameMode background image.
      
      
      
      boat.draw(); //Draw the boat.
      updatePosition();  //Function that monitors the boat movements.
      
      shark.draw();//Draw the shark.

      //If the Victim is Safe
      if (isSafe) {
        victim = new Victim(); //So we have to construct a new one.
        isSafe = false; //Set flag to false again.
        score++; //Increase the score of the player.
      }
      
      victim.draw(); //Draw the victim
      
      /*-----Writing Score and LevelGame at the GameMode screen-----*/
      textFont(f, 32);      
      fill(0xffffffff);     
      textAlign(CENTER);
      text("Score: " + score, 70, 40);
      text("Level: " + boat.speed, 730, 40);
      /*-----End Writing Score and LevelGame at the screen-----*/
    } 
    else { //If is GameOver
      
        
      MusicGameScreen.setGain(-35.0f); //Set the music Game to -35dB.
      
      /*----Draw the last scene of the game again----*/
      drawBg(bg);
      boat.draw();
      shark.draw();
      victim.draw();
      /*----End Draw the last scene of the game again----*/

      /*----Put the GameOver background image---*/
      imageMode(CENTER);
      image(GameOverImg, width/2, height/2);
      
      //Update the bestScore if it is.
      if (score > bestScore)
      {
        bestScore = score;
      }
      
      /*-----Writing Score,LevelGame,and bestScore at the GameOver screen-----*/
      textFont(f, 32);      
      fill(0xffffffff);
      textAlign(CENTER);
      text("Your score is: " + score, width/2, height/2);
      text("Your Best Score is: " + bestScore, width/2, height/2 + 32);
      text("Level: " + boat.speed, 730, 40); 
      /*-----End Writing Score,LevelGame,and bestScore at the GameOver screen-----*/
      
      //if press Enter
      if (keys[ENTER])
      {
        EffectPressEnter.trigger();  //Play the Enter button sound effect.
       
       //Initialize setup() variables again with new(random) coordinates. 
        boat = new Boat((int)random(width/2, width), (int)random(height/2, height));
        shark = new Shark((int)random(0, width/2), (int)random(0, height/2));
        
        GameOver = false; //Set false the GameOver flag.
        isSafe = true; //Set true the variable of the victim isSafe.
        score = -1; //Reset the score.
        
        //Stop with the MusicEffects.
        EffectSharkAttack.stop(); 
        EffectDrown.stop(); 
      }
    }
  }
}




class Boat {
  
  PImage[] boat = new PImage[] {
    loadImage("Images/boat0.png"), loadImage("Images/boat1.png"), loadImage("Images/boat2.png"), loadImage("Images/boat3.png")
  };
  int x;
  int y;
  int speed = 1;
  
  Boat(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  int count = 0; //Counter that manage the Buoy sprites drawing.

  public void UpdatePos(int x, int y)
  {
    //This for-loop define the speed-up of the buoy.
    for(int i = 0; i < this.speed; i++){
    this.x = (this.x + x)%width;

    if (this.x + x <= 0)
    {
      this.x = width;
    }

    this.y = (this.y + y)%height;

    if (this.y + y <= 0)
    {
      this.y = height;
    }
    }
  }
  public void draw()
  {
    
    if(score >= 6)
    {
    this.speed = score/6 + 1;
    }
    imageMode(CENTER);       
    count = (count + 1)%40;
    image(this.boat[count/10], this.x, this.y);
  }
}

class Shark {

  PImage[] shark_right = new PImage[] {
    loadImage("Images/shark0r.png"), loadImage("Images/shark1r.png"), loadImage("Images/shark2r.png"), loadImage("Images/shark3r.png")
    };
  PImage[] shark_left = new PImage[] {
    loadImage("Images/shark0l.png"), loadImage("Images/shark1l.png"), loadImage("Images/shark2l.png"), loadImage("Images/shark3l.png")
    };
    int x;
  int y;
  int firstLoop = 0;
  int speed = boat.speed;

  Shark(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void UpdatePos()
  {
    int relative_x = boat.x - this.x; //The relative X position between the shark and the buoy.
    int relative_y = boat.y - this.y; //The relative Y position between the shark and the buoy.

    //If the Shark is close to the buoy.
    if (Math.abs(relative_x) < 100 && Math.abs(relative_y) < 100)
    {
      //Increase its velocity by 1.
      this.speed = boat.speed + 1;
    } else
    {
      //the velocity will be the same as the buoy.
      this.speed = boat.speed;
    }

    //This for-loop define the speed-up of the Shark.
    for (int i = 0; i < this.speed; i++)
    {
      //If relative X position is negative
      if (relative_x < 0)
      {
        //so, move X to negative.
        this.x = this.x - 2;
      } else
      {
        //else, move X to positive.
        this.x = this.x + 2;
      }

      //If relative Y position is negative
      if (relative_y < 0)
      {
        //so, move Y to negative.
        this.y = this.y - 2;
      } else
      {
        //else, move Y to positive.
        this.y = this.y + 2;
      }
    }
  }

  //Detect colision between the Buoy and Shark.
  public void colision()
  {

    if ( Math.abs(boat.x - this.x) < 32 && Math.abs(boat.y - this.y) < 32)
    { 
      if (this.firstLoop == 0)
      {
        EffectSharkAttack.trigger();
        this.firstLoop++;
      }

      GameOver = true;
    }
  }

  int count = 0; //Counter that manage the Shark sprites drawing.
  public void draw()
  {
    colision();

    if (!GameOver) {
      UpdatePos();
    }
    count = (count + 1)%40;

    imageMode(CENTER); 
    if (boat.x >= this.x) {
      image(this.shark_right[count/10], this.x, this.y);
    } else {
      image(this.shark_left[count/10], this.x, this.y);
    }
  }
}

class Victim {

  PImage[][] victim = new PImage[][] {
    {
      loadImage("Images/woman0.png"), loadImage("Images/woman1.png"), loadImage("Images/woman2.png")
      }
      , 
    {
      loadImage("Images/man0.png"), loadImage("Images/man1.png"), loadImage("Images/man2.png")
      }
    };


  PImage[][] drown = new PImage[][] {
    {

      loadImage("Images/drownwoman0.png"), loadImage("Images/drownwoman1.png"), loadImage("Images/drownwoman2.png"), 
      loadImage("Images/drownwoman3.png"), loadImage("Images/drownwoman4.png"), loadImage("Images/drownwoman5.png"), 
      loadImage("Images/drownwoman6.png"), loadImage("Images/drownwoman7.png"), loadImage("Images/drownwoman8.png"), loadImage("Images/drownwoman9.png")
      }
      , 
    {
      loadImage("Images/drownman0.png"), loadImage("Images/drownman1.png"), loadImage("Images/drownman2.png"), 
      loadImage("Images/drownman3.png"), loadImage("Images/drownman4.png"), loadImage("Images/drownman5.png"), 
      loadImage("Images/drownman6.png"), loadImage("Images/drownman7.png"), loadImage("Images/drownman8.png"), loadImage("Images/drownman9.png")
      }
    };

  int count = 0; //Counter that manage the Victms sprites drawing.
  int sprite = (int)random(0, 2); //Random choose between man and woman.

  int x;
  int y;
  int firstLoop = 0;

  Victim()
  {
    double distance = 0;

    //Victims can't born in the middle of the GameScreen.
    while (distance < 61)
    {
      this.x = (int) random(16, 784);
      this.y = (int) random(16, 584);
      distance = Math.sqrt(Math.pow(((double)this.x - (((double)width)/2)), 2)+Math.pow(((double)this.y - (((double)height)/2)), 2));
    }
   
     
  }

  //Detect colision between the Buoy and Victim.
  public void colision() {
    if ( Math.abs(boat.x - this.x) < 32 && Math.abs(boat.y - this.y) < 32)
    {
      isSafe = true; 

      EffectScore[sprite].trigger(); //Play the EffectVictim Sound.
    }
  }

  public void draw()
  {
    if (!GameOver) {
      colision();
      
      count = (count + 1)%30;
      imageMode(CENTER);  
      image(this.victim[sprite][count/10], this.x, this.y);
    }
    else {
      if (this.firstLoop == 0) 
      {  
        EffectDrown.trigger();
        this.firstLoop++;
      }
      
      count = (count + 1)%100;
      imageMode(CENTER);       
      image(this.drown[sprite][count/10], this.x, this.y);
    }
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
