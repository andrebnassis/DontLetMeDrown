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

  void UpdatePos()
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
  void colision()
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
  void draw()
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

