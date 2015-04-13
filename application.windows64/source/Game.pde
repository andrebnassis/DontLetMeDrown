import processing.core.*;
import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

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
void drawBg(PImage[] bg)
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
void setup() {

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
void keyPressed() {
  keys[keyCode] = true;
}

void keyReleased() {
  keys[keyCode] = false;
}


void updatePosition() {
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
void draw() {

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

      MusicGameScreen.setGain(-20.0); //Put the gain to -20dB.
      MusicGameScreen.loop(); // Starts the GameMode Music.
    }
    //If is not GameOver
    if (!GameOver)
    {
      
      MusicGameScreen.setGain(-20.0); //Put the gain to -20dB.

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
      fill(#ffffff);     
      textAlign(CENTER);
      text("Score: " + score, 70, 40);
      text("Level: " + boat.speed, 730, 40);
      /*-----End Writing Score and LevelGame at the screen-----*/
    } 
    else { //If is GameOver
      
        
      MusicGameScreen.setGain(-35.0); //Set the music Game to -35dB.
      
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
      fill(#ffffff);
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

