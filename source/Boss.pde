/**
  The Boss Zombie, Hits harder + More Health
*/
class Devil extends Zombie {

  float x, y, moveX, moveY;
  float size  = 30;
  
  float health = 50;
  int maxHealth = (int) health;
  
  color c = color(189, 19, 0);
  float moveSpeed = 0.75;
  float look;
  int score = 125;


  Devil(float moveX, float moveY) {
    super(moveX, moveY);


    this.x = super.x; 
    this.y = super.y;
    this.moveX = super.moveX; 
    this.moveY = super.moveY;
  }

  void display() {
    fill(c);
    look();
    for (int i = 0; i < walls.length; i++) {
      if (walls[i].inWallTop(this.moveX, this.moveY, this.size) == true) {
        wTop = true;
      } else if (walls[i].inWallBottom(this.moveX, this.moveY, this.size) == true) {
        wBottom = true;
      } else if (walls[i].inWallLeft(this.moveX, this.moveY, this.size) == true) {
        wLeft = true;
      } else if (walls[i].inWallRight(this.moveX, this.moveY, this.size) == true) {
        wRight = true;
      }
    }
    move();
    wTop = false; 
    wLeft = false; 
    wRight = false; 
    wBottom =  false;

    strokeWeight(1);
    pushMatrix();
    translate(this.moveX, this.moveY);
    rotate(this.look);
    rectMode(CENTER);
    rect(this.x, this.y, this.size, this.size);
    rectMode(CORNER);
    popMatrix();

    //Health display
    healthBar();

    // Check for projectile hit
    checkHit();
  }
  void look() {
    float angle = atan2(this.moveY - p.moveY, p.moveX - this.moveX); 
    this.look = -angle;
  }
  int score(){
    return this.score;
  }

  void move() {

    float distX = p.moveX - this.moveX;
    float distY = p.moveY - this.moveY;
    float sum = sqrt(sq(distX) + sq(distY));
    float changeY = distY/sum * moveSpeed;
    float changeX = distX/sum * moveSpeed;
    if ( sum > 30) {
      if (wTop == false && wLeft == false && wRight == false && wBottom == false) {    
        this.moveX += changeX;
        this.moveY += changeY;
        // Acts if the top wall is the current face
      } else if (wTop == true) {
        if (changeX <= 0) {
          if (changeX > -0.5) {
            this.moveX += -0.5;
          } else this.moveX += changeX;
        } else {
          if (changeX < 0.5) {
            this.moveX += 0.5;
          } else this.moveX += changeX;
        }
        if (changeY < 0) {
          this.moveY += changeY;
        }
        // Acts if the bottom wall is the current face
      } else if (wBottom == true) {
        if (changeX <= 0) {
          if (changeX > -0.5) {
            this.moveX += -0.5;
          } else this.moveX += changeX;
        } else {
          if (changeX < 0.5) {
            this.moveX += 0.5;
          } else this.moveX += changeX;
        }
        if (changeY > 0) {
          this.moveY += changeY;
        }
        // Acts if the Left wall is current face
      } else if (wLeft == true) {
        if (changeY <= 0) {
          if (changeY > -0.8) {
            this.moveY += -0.8;
          } else this.moveY += changeY;
        } else {
          if (changeY < 0.8) {
            this.moveY += 0.8;
          } else this.moveY += changeY;
        }
        if (changeX < 0) {
          this.moveX += changeX;
        }
        // Acts if the Right wall is the current face
      } else if (wRight == true) {
        if (changeY <= 0) {
          if (changeY > -0.8) {
            this.moveY += -0.8;
          } else this.moveY += changeY;
        } else {
          if (changeY < 0.8) {
            this.moveY += 0.8;
          } else this.moveY += changeY;
        }
        if (changeX > 0) {
          this.moveX += changeX;
        }
      }
    } else {
      attack();
    }
  }

  void healthBar() {
    float greenBar = this.size * health/maxHealth;
    float redBar = this.size * maxHealth/maxHealth;
    fill(255, 0, 0);
    rect(this.moveX - this.size/2, this.moveY + size, redBar, 7.5);
    fill(0, 255, 0);
    rect(this.moveX - this.size/2, this.moveY + size, greenBar, 7.5);
  }

  void attack() {
    p.health -= 2;
  }
  boolean isDead() {
    if (health <= 0) {
      return true;
    } else return false;
  }

  void checkHit() {
    for (int i = 0; i < projectiles.length; i++) {
      if (projectiles[i] != null) {
        float relativeX = projectiles[i].pos.x;
        float relativeY = projectiles[i].pos.y;
        if (relativeX >= this.moveX - this.size/1.5 && relativeX <= this.moveX+ this.size/1.5 && 
          relativeY >= this.moveY - this.size/1.5 && relativeY <= this.moveY + this.size/1.5) {
          this.health -= projectiles[i].damage; 
          projectiles[i] = null;
        }
      }
    }
  }
}
