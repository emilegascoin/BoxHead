/**
 Walls. Stored in array.
 */
class Wall {
  float x, y;
  // Width and height values
  float w; 
  float h;

  float yTop;
  float yBot;

  float xLeft;
  float xRight;

  Wall(float x, float y) {
    this.x = x;
    this.y = y;

    this.w = 100;
    this.h = 80;


    // Specific for clipping of player and zombies
    this.yTop = this.y - 30;
    this.yBot = this.y + this.h;

    this.xLeft = this.x;
    this.xRight = this.x + this.w;
  }

  void build() {
    fill(255);
    stroke(0);
    strokeWeight(3);
    rect(this.x, this.y, this.w, this.h);
    this.checkHit();
  }

  void top() {
    fill(150);
    strokeWeight(3);
    rect(this.x, this.y, this.w, -this.h/1.5);
  }
  void shadow() {
    fill(50);
    stroke(50);
    strokeWeight(2);
    beginShape();
    vertex(this.x, this.y + this.h);
    vertex(this.x - this.w/4, this.y + this.h/2);
    vertex(this.x - this.w/4, this.y-10);
    vertex(this.x, this.y - 10);
    endShape();
  }
  /**
   Return if object is in Wall
   */
  boolean inWallTop(float x, float y, float size) {
    if (x >= this.xLeft - size*0.5 && x <= this.xRight + size*0.5 && y >= this.yTop - size*0.5 && y <= this.yTop) {
      println("TOP");return true;
    } else return false;
  }
  boolean inWallBottom(float x, float y, float size) {
    if (x >= this.xLeft - size*0.5 && x <= this.xRight + size*0.5 && y <= this.yBot + size*0.5 && y >= this.yBot) {
      println("Bottom");return true;
    } else return false;
  }
  boolean inWallLeft(float x, float y, float size) {
    if (x >= this.xLeft - size*0.5 && x <= this.xLeft && y >= this.yTop - size*0.5 && y <= this.yBot + size*0.5) {
      println("LEFT");return true;
    } else return false;
  }
  boolean inWallRight(float x, float y, float size) {
    if (x <= this.xRight + size*0.5 && x >= this.xRight && y >= this.yTop - size*0.5 && y <= this.yBot + size*0.5) {
      println("RIGHT");return true;
    } else return false;
  }
  /**
   Checks if projectile has hit wall
   */
  void checkHit() {
    for (int i = 0; i < projectiles.length; i++) {
      if (projectiles[i] != null) {
        float relativeX = projectiles[i].pos.x;
        float relativeY = projectiles[i].pos.y;
        if (relativeX >= this.xLeft && relativeX <= this.xRight && 
          relativeY >= this.yTop && relativeY <= this.yBot) {
          projectiles[i] = null;
        }
      }
    }
  }
}
