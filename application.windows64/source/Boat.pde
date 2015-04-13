


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

  void UpdatePos(int x, int y)
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
  void draw()
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

