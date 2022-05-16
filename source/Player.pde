/**
  The Player, Controlled with Standard keyboard controls
*/
class Player {
  float x, y;
  float moveX, moveY;
  float aim;
  float speed;
  int health, maxHealth;

  int playerSize = 25;
  


  // Booleans to see if inside walls
  boolean wTop, wBottom, wLeft, wRight;



  Player() {
    this.x = 0;
    this.y = 0;
    this.moveX = width/2;
    this.moveY = height/2;
    this.health = 100;
    this.speed = 5;
    this.health = 100;
    this.maxHealth = health; 
    
    g = new Gun();
  }

  void display() {
    aim();
        

    strokeWeight(1);
    pushMatrix();
    translate(moveX, moveY);  
    rotate(aim);
    if(g != null){
     g.display();
    }
    // The Player
    rectMode(CENTER);
    fill(150, 75, 0);
    rect(this.x, this.y  - playerSize/2, this.playerSize, this.playerSize);
    popMatrix();
    rectMode(CORNER);

  //  // Check for inside walls
  //  for (int i = 0; i < walls.length; i++) {
  //    if (walls[i].inWallTop(this.moveX, this.moveY, this.playerSize) == true) {
  //      wTop = true;
  //    } else if (walls[i].inWallBottom(this.moveX, this.moveY, this.playerSize) == true) {
  //      wBottom = true;
  //    } else if (walls[i].inWallLeft(this.moveX, this.moveY, this.playerSize) == true) {
  //      wLeft = true;
  //    } else if (walls[i].inWallRight(this.moveX, this.moveY, this.playerSize) == true) {
  //      wRight = true;
  //    }
  //  }
  //}
  
  for (int i = 0; i < walls.length; i++) {
        if (walls[i].inWallTop(this.moveX, this.moveY, this.playerSize) == true) {
          if (wRight == false || wLeft == false) {
            wTop = true;
          }
        } else if (walls[i].inWallBottom(this.moveX, this.moveY, this.playerSize) == true) {
          if (wRight == false || wLeft == false) {
            wBottom = true;
          }
        } 
        if (walls[i].inWallLeft(this.moveX, this.moveY, this.playerSize) == true) {
          if (wTop == false || wBottom == false) {
            wLeft = true;
          }
        } else if (walls[i].inWallRight(this.moveX, this.moveY, this.playerSize) == true) {
          if (wTop == false || wBottom == false) {
            wRight = true;
          }
        }
      }
    }

  /** 
   Player Health Bar
   */
  void playerHealth() {
    strokeWeight(1);
    float greenBar = width/3 * this.health/this.maxHealth;
    float redBar = width/3 * this.maxHealth/this.maxHealth;
    fill(255, 0, 0);
    rect(width / 10 * 1, height / 100 * 1, redBar, redBar/12);
    fill(0, 255, 0);
    rect(width / 10 * 1, height / 100 * 1, greenBar, redBar/12);
  }
  /** 
   Angle the player will aim
   */
  void aim() {
    float angle = atan2(this.moveY - mouseY, mouseX - this.moveX); 
    aim = -angle;
  }

  // Basic movement controls with the use of WSAD
  void moveLeft() {
    if (moveX - playerSize/2 > 0 && wRight == false) {
      this.moveX -= this.speed;
    }  
    wRight = false;
  }
  void moveRight() {
    if (moveX + playerSize/2 < width && wLeft == false) {
      this.moveX += this.speed;
    }
    wLeft = false;
  }
  void moveUp() {
    if (moveY - playerSize/2 > 0 && wBottom == false) {
      this.moveY -= this.speed;
    }
    wBottom = false;
  }
  void moveDown() {
    if (moveY + playerSize/2 < height && wTop == false) {
      this.moveY += this.speed;
    }
    wTop = false;
  }
  //Diagonally right up movement
  void moveUpRight() {
    if (moveY - playerSize/2 > 0 && wBottom == false) {
      this.moveY -= this.speed/1.5;
    }
    if (moveX + playerSize/2 < width && wLeft == false) {
      this.moveX += this.speed/1.5;
    }
    wBottom = false;
    wLeft = false;
  }

  // diagonally down right movement
  void moveDownRight() {
    if (moveY + playerSize/2 < height && wTop == false) {
      this.moveY += this.speed/1.5;
    }
    if (moveX + playerSize/2 < width && wLeft == false) {
      this.moveX += this.speed/1.5;
    }
    wTop = false;
    wLeft = false;
  }

  //diagonaly up left movement
  void moveUpLeft() {
    if (moveY - playerSize/2 > 0 && wBottom == false) {
      this.moveY -= this.speed/1.5;
    }
    if (moveX - playerSize/2 > 0 && wRight == false) {
      this.moveX -= this.speed/1.5;
    }
    wBottom = false;
    wRight = false;
  }

  //diagonal down left movement
  void moveDownLeft() {
    if (moveY + playerSize/2 < height && wTop == false) {
      this.moveY += this.speed/1.5;
    }
    if (moveX - playerSize/2 > 0 && wRight == false) {
      this.moveX -= this.speed/1.5;
    }
    wTop = false;
    wRight = false;
  }
}
