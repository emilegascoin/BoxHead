/**
 Default Zombie.
 */
class Zombie {

  float x, y, moveX, moveY;
  float size  = 25;

  float health = 10;
  int maxHealth = (int) health;

  color c = color(105, 131, 98);

  float moveSpeed = 1;
  float look;
  int score = 25;

  boolean wTop, wBottom, wLeft, wRight;

  Zombie(float moveX, float moveY) {
    this.x = 0;

    this.moveX = moveX;
    this.moveY = moveY;
  }
  void display() {

    fill(c);
    look();
    // Boolean of if it is touching a wall
    for (int i = 0; i < walls.length; i++) {
        if (walls[i].inWallTop(this.moveX, this.moveY, this.size) == true) {
          if (wRight == false || wLeft == false) {
            wTop = true;
          }
        } 
        if (walls[i].inWallBottom(this.moveX, this.moveY, this.size) == true) {
          if (wRight == false || wLeft == false) {
            wBottom = true;
          }
        } 
        if (walls[i].inWallLeft(this.moveX, this.moveY, this.size) == true) {
          if (wTop == false || wBottom == false) {
            wLeft = true;
          }
        } 
        if (walls[i].inWallRight(this.moveX, this.moveY, this.size) == true) {
          if (wTop == false || wBottom == false) {
            wRight = true;
          }
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
  // Aims The Zombie
  void look() {
    float angle = atan2(this.moveY - p.moveY, p.moveX - this.moveX); 
    this.look = -angle;
  }
  // Returning score of zombie ( 25 )
  int score() {
    return this.score;
  }

  // Moves this Zombie
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
        if (changeX < 0) {
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
        if (changeX < 0) {
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
        if (changeY < 0) {
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
        if (changeY < 0) {
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

  // Health Bar Display
  void healthBar() {
    float greenBar = this.size * health/maxHealth;
    float redBar = this.size * maxHealth/maxHealth;
    fill(255, 0, 0);
    rect(this.moveX - this.size/2, this.moveY + size, redBar, 7.5);
    fill(0, 255, 0);
    rect(this.moveX - this.size/2, this.moveY + size, greenBar, 7.5);
  }
  // Damages Player
  void attack() {
    p.health--;
  }
  // Boolean returns state of Zombie
  boolean isDead() {
    if (health <= 0) {
      return true;
    } else return false;
  }
  // Checks the Projectiles array to see if there is collision
  void checkHit() {
    for (int i = 0; i < projectiles.length; i++) {
      if (projectiles[i] != null) {
        float relativeX = projectiles[i].pos.x;
        float relativeY = projectiles[i].pos.y;
        if (relativeX >= this.moveX - this.size/1.5 && relativeX <= this.moveX+ this.size/1.5 && 
          relativeY >= this.moveY - this.size/1.5 && relativeY <= this.moveY + this.size/1.5) {
          health -= projectiles[i].damage; 
          projectiles[i] = null;
        }
      }
    }
  }
}
