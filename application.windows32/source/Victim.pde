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
  void colision() {
    if ( Math.abs(boat.x - this.x) < 32 && Math.abs(boat.y - this.y) < 32)
    {
      isSafe = true; 

      EffectScore[sprite].trigger(); //Play the EffectVictim Sound.
    }
  }

  void draw()
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

