import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Boxhead extends PApplet {

Player p;
Gun g;

boolean up, down, left, right;

int round = 0;
int score = 0;

// Lootbox
Lootbox l;

boolean game = true;
boolean pause = false;

// Entitys
Projectile[] projectiles = new Projectile[1000];

int bossTotal = 0;
int zTotal = 1000;
Zombie[] z = new Zombie[zTotal];

// Total zombies left 100 - zCheck
int zCheck = 0;

Wall[] walls  = new Wall[12];
public void setup() {
  //size(1500, 900);
  

  p = new Player();
  g = new Gun();

  // Creating the walls around the centre of the room
  
  float wX = width/5; 
  float wY = height/4;
  for (int i = 0; i < walls.length; i++) {
    walls[i] = new Wall(wX - 50, wY);
    wX += width/5;
    if (wX + walls[i].w >= width) {
      wX = width/5;
      wY += height/4;
    }
  }
}

public void draw() {
  background(200);

  if (p.health <= 0) {
    game = false;
  }
  if (game == true) {

    //Wall shadows - placed here so entities will be ontop
    for (int i = 0; i < walls.length; i++) {
      walls[i].shadow();
    }
    // The Projectiles
    for (int i = 0; i < projectiles.length; i++) {
      if (projectiles[i] != null) {
        projectiles[i].display();
        if (projectiles[i].pos.x >= width) {
          projectiles[i] = null;
        } else if (projectiles[i].pos.y >= height) {
          projectiles[i] = null;
        } else if (projectiles[i].pos.x <= 0) {
          projectiles[i] = null;
        } else if (projectiles[i].pos.y <= 0) {
          projectiles[i] = null;
        }
      }
    }
    // State Of Lootbox
    if (l != null) {
      l.display();
      if (l.collected) {
        l.rollTheDice(); 
        l = null;
      }
    }

    // Displaying the player
    p.display();


    //Zombie Checker
    for (int i = 0; i < z.length; i++) {
      if (z[i] != null) {
        z[i].display();
        if (z[i].isDead()) {
          score += z[i].score();
          z[i] = null;
        }
      } else {
        zCheck++;
      }
    }
    // Wall block and top
    for (int i = 0; i < walls.length; i++) {
      walls[i].build();
      walls[i].top();
    }

    // Lootbox Spawner
    if (frameCount%1000 == 0) {
      // Starts Round 5, More Common as The Rounds Go on
      if (random(0, round) > 5) {
        l = new Lootbox();
      }
    }
    // Back Frame
    strokeWeight(1);
    fill(240);
    rect(0,0,width, height /14);
    // Player health bar
    p.playerHealth();

    // FrameRate Bottom Right
    textSize(25);
    fill(0);
    text(String.format("%.0f", frameRate), width - width * 0.025f, height - height /100);

    // Round indicator
    textSize(50);
    fill(255, 0, 0);
    text(round, 10, 50);

    // Score Indicator
    textSize(35);
    fill(255, 0, 0);
    text("SCORE: " + score, width/2, height /100 * 4.5f);

    // Total Ammo
    textSize(35);
    fill(0);
    if (g.ammo() == 1111) {
      text("AMMO: " + "Inf.", width/1.4f, height /100 * 4.5f);
    } else text("AMMO: " + g.ammo(), width/1.4f, height /100 * 4.5f);

    // Pause button
    pause();


    // Checks if round is over and spawns a zombie for each round there is
    if (zCheck == zTotal) {
      round++; 

      bossTotal = (int) round / 10;
      for (int i = 0; i < round; i++) {

        // Randomises which side and points the zombies will come from
        int side = (int) random(1, 4.9f);
        float x1= 0;
        float x2 = 0;
        float y1= 0;
        float y2 = 0;

        if (side == 1) {
          x1 = 0; 
          x2 = width; 
          y1 = 0; 
          y2 = 0;
        }
        if (side == 2) {
          x1 = width; 
          x2 = width; 
          y1 = 0; 
          y2 = height;
        }
        if (side == 3) {
          x1 = 0; 
          x2 = width; 
          y1 = height; 
          y2 = height;
        }
        if (side == 4) {
          x1 = 0; 
          x2 = 0; 
          y1 = 0; 
          y2 = height;
        }

        if (round <= 1000) {
          if (bossTotal > 0) {
            z[i] = new Devil(random(x1, x2), random(y1, y2));
            bossTotal--;
          } else z[i] = new Zombie(random(x1, x2), random(y1, y2));
        }
      }
      zCheck = 0;
    } else {
      zCheck = 0;
    }

    ////Gun shooting by rate of fire  && frameCount%g.rateOfFire == 0 
    if (mousePressed) {
      g.shoot();
    }

    if (up == true && right == true) {
      p.moveUpRight();
    } else if (down == true && right == true) {
      p.moveDownRight();
    } else if ( up == true && left == true) {
      p.moveUpLeft();
    } else if (down == true && left == true) {
      p.moveDownLeft();
    } else if (up == true) {
      p.moveUp();
    } else if (down == true) {
      p.moveDown();
    } else if (left == true) {
      p.moveLeft();
    } else if (right == true) {
      p.moveRight();
    }
  } else if (game == false) {
    gameOver();
  }
}


/** 
 Game Over Menu
 */
public void gameOver() {
  background(50);
  float x = (width/10) * 4;
  float y = (height/10) * 2;
  float w = width/5;
  float h = height/2;

  strokeWeight(5);
  fill(220);
  rect(x, y, w, h);

  textSize(w/6.5f);
  fill(0);
  text(" GAME OVER ", x + w * 0.015f, y + h/6);

  fill(200, 5, 10);
  textSize(w/12);
  text("SCORE: " + score, x + w * 0.2f, y + h/4); 

  fill(200, 5, 10);
  textSize(w/12);
  text("ROUND: " + round, x + w * 0.2f, y + h/3); 

  int c = color(240);

  float frameWidth = w/1.3f;
  float frameHeight = h/8;
  float frameX = x + w * 0.115f;
  float frameY = y + h * 0.4f;
  if (mouseX > frameX && mouseX < frameX + frameWidth && mouseY > frameY && mouseY < frameY + frameHeight) {
    c = color(180);
    if (mousePressed) {
      newGame();
    }
  }
  fill(c);
  rect(frameX, frameY, frameWidth, frameHeight); 
  textSize(w/9);
  fill(0);
  text(" NEW GAME ", frameX, frameY * 1.11f);

  frameY = y + h * 0.6f;
  c = color(240);
  if (mouseX > x + 50 && mouseX < x + w - 100 && mouseY > frameY && mouseY < frameY + frameHeight) {
    c = color(180);
    if (mousePressed) {
      newGamePlus();
    }
  }
  fill(c);
  rect(frameX, frameY, frameWidth, frameHeight); 
  textSize(w/9);
  fill(0);
  text(" NEW GAME+ ", frameX, frameY * 1.09f);

  frameY = y + h * 0.8f;
  c = color(240);
  if (mouseX > x + 50 && mouseX < x + w - 100 && mouseY > frameY && mouseY < frameY + frameHeight) {
    c = color(180);
    if (mousePressed) {
      exit();
    }
  }
  fill(c);
  rect(frameX, frameY, frameWidth, frameHeight); 
  textSize(w/12);
  fill(0);
  text(" EXIT TO DESKTOP ", frameX, frameY * 1.07f);
}
/** 
 Start New Game
 */
public void newGame() {
  game = true;
  score = 0;
  round = 0;
  z = new Zombie[zTotal];
  projectiles = new Projectile[1000];
  setup();
}
/** 
 Start New Game at Round 20
 */
public void newGamePlus() {
  game = true;
  score = 0;
  round = 19;
  z = new Zombie[zTotal];
  projectiles = new Projectile[1000];
  setup();
}
/** 
 Pause Game button
 */
public void pause() {
  int c = color(200);
  float x = width - 75; 
  float y = 5; 
  float size = 50;
  if (mouseX > x && mouseX < x + size && mouseY > y && mouseY < y + size) {
    c = color(100);
  }
  fill(c);
  strokeWeight(2);
  rect(x, y, size, size);
  fill(0);
  rect(x + 10, y + 5, 10, size - 10);
  rect(x + 30, y + 5, 10, size - 10);
  if (pause == true) {
    textSize(50);
    fill(25);
    text("PAUSED", x - width/10, y + height / 8);
  }
}
/**
 Clicking the pause button stops the loop
 */
public void mouseClicked() {
  if (mouseX > width - 75 && mouseX < width - 75 + 50 && mouseY > 5 && mouseY < 5 + 50) {
    if (pause == false) {
      pause = true; 
      pause(); 
      noLoop();
    } else {
      pause = false; 
      loop();
    }
  }
}
/**
 MousePressed shoots gun
 */
public void mousePressed() {
  g.shoot();
}
/**
 keyPressed and Released for key inputs 'WSAD'
 */
public void keyPressed() {
  if (key == 'w' || key == 'W') {
    up = true;
  }
  if (key == 's' || key == 'S') {
    down = true;
  }
  if (key == 'a' || key == 'A') {
    left = true;
  }
  if (key == 'd' || key == 'D') {
    right = true;
  }
}

public void keyReleased() {
  if (key == 'w' || key == 'W') {
    up = false;
  }
  if (key == 's' || key == 'S') {
    down = false;
  }
  if (key == 'a' || key == 'A') {
    left = false;
  }
  if (key == 'd' || key == 'D') {
    right = false;
  }
}
/**
  Lootbox AR Gun
*/
class AR extends Gun {
  // Gun set variables
  float gunLength = 30;
  float gunWidth = 5;
  float rateOfFire = 10;
  float recoil = 5;
  int damage = 2;

  int ammo = 100;
  int bullet;
  
  int c = color(58, 61, 59);
  AR() {
  }

  public void display() {
    fill(c);
    rect(p.x + 5, p.y, gunLength, gunWidth);
    fill(0);
  }

  public int ammo(){
    return ammo;
  }

  public void shoot() {
    if(frameCount%this.rateOfFire == 0){
    if (bullet == 1000) {
      bullet = 0;
    }
    
    // random values attatched to mouseX and mouseY are for the recoil
    projectiles[bullet] = new Projectile(p.moveX+2, p.moveY, mouseX + random(-recoil, recoil), mouseY + random(-recoil, recoil), p.aim, gunLength, p.playerSize, damage);
    bullet++;
    ammo--;
    if(ammo == 0){
      g = null;
      g = new Gun();
    }
  }
  }
}
/**
  The Boss Zombie, Hits harder + More Health
*/
class Devil extends Zombie {

  float x, y, moveX, moveY;
  float size  = 30;
  
  float health = 50;
  int maxHealth = (int) health;
  
  int c = color(189, 19, 0);
  float moveSpeed = 0.75f;
  float look;
  int score = 125;


  Devil(float moveX, float moveY) {
    super(moveX, moveY);


    this.x = super.x; 
    this.y = super.y;
    this.moveX = super.moveX; 
    this.moveY = super.moveY;
  }

  public void display() {
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
  public void look() {
    float angle = atan2(this.moveY - p.moveY, p.moveX - this.moveX); 
    this.look = -angle;
  }
  public int score(){
    return this.score;
  }

  public void move() {

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
          if (changeX > -0.5f) {
            this.moveX += -0.5f;
          } else this.moveX += changeX;
        } else {
          if (changeX < 0.5f) {
            this.moveX += 0.5f;
          } else this.moveX += changeX;
        }
        if (changeY < 0) {
          this.moveY += changeY;
        }
        // Acts if the bottom wall is the current face
      } else if (wBottom == true) {
        if (changeX <= 0) {
          if (changeX > -0.5f) {
            this.moveX += -0.5f;
          } else this.moveX += changeX;
        } else {
          if (changeX < 0.5f) {
            this.moveX += 0.5f;
          } else this.moveX += changeX;
        }
        if (changeY > 0) {
          this.moveY += changeY;
        }
        // Acts if the Left wall is current face
      } else if (wLeft == true) {
        if (changeY <= 0) {
          if (changeY > -0.8f) {
            this.moveY += -0.8f;
          } else this.moveY += changeY;
        } else {
          if (changeY < 0.8f) {
            this.moveY += 0.8f;
          } else this.moveY += changeY;
        }
        if (changeX < 0) {
          this.moveX += changeX;
        }
        // Acts if the Right wall is the current face
      } else if (wRight == true) {
        if (changeY <= 0) {
          if (changeY > -0.8f) {
            this.moveY += -0.8f;
          } else this.moveY += changeY;
        } else {
          if (changeY < 0.8f) {
            this.moveY += 0.8f;
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

  public void healthBar() {
    float greenBar = this.size * health/maxHealth;
    float redBar = this.size * maxHealth/maxHealth;
    fill(255, 0, 0);
    rect(this.moveX - this.size/2, this.moveY + size, redBar, 7.5f);
    fill(0, 255, 0);
    rect(this.moveX - this.size/2, this.moveY + size, greenBar, 7.5f);
  }

  public void attack() {
    p.health -= 2;
  }
  public boolean isDead() {
    if (health <= 0) {
      return true;
    } else return false;
  }

  public void checkHit() {
    for (int i = 0; i < projectiles.length; i++) {
      if (projectiles[i] != null) {
        float relativeX = projectiles[i].pos.x;
        float relativeY = projectiles[i].pos.y;
        if (relativeX >= this.moveX - this.size/1.5f && relativeX <= this.moveX+ this.size/1.5f && 
          relativeY >= this.moveY - this.size/1.5f && relativeY <= this.moveY + this.size/1.5f) {
          this.health -= projectiles[i].damage; 
          projectiles[i] = null;
        }
      }
    }
  }
}
/**
  Default Gun All Others Extend This.
*/
class Gun {
  // Gun set variables
  float gunLength = 20;
  float gunWidth = 5;
  float rateOfFire = 15;
  float recoil = 15;
  
  int ammo = 1111;
  
  int damage = 1;

  int bullet;
  Gun(){
  }

  public void display() {
    fill(0);
    rect(p.x + 5, p.y, gunLength, gunWidth);
  }
  // Returns ammo total
  public int ammo(){
    return ammo;
  }

// Method to shoot the gun
  public void shoot() {
    if(frameCount%this.rateOfFire == 0){
    if (bullet == 1000) {
      bullet = 0;
    }
    // random values attatched to mouseX and mouseY are for the recoil
    projectiles[bullet] = new Projectile(p.moveX+2, p.moveY, mouseX + random(-recoil, recoil), mouseY + random(-recoil, recoil), p.aim, gunLength, p.playerSize, damage);
    bullet++;
  }
  }
}
/**
 Lootbox, Spawns randomly every 1000 frames. Contains Weapons
 */
class Lootbox {
  float x, y, size;
  boolean goodPos = false; 
  boolean collected = false;
  int c = color(255, 153, 51);
  Lootbox() {
    goodPos = inWall();
    this.size = 10;
  }

  public void display() {
    if (goodPos == true && collected == false) {
      fill(c);
      rect(this.x, this.y, this.size, this.size);
      collect();
    }
  }
  /**
   Checks If Box Is Collected, Goes to rollTheDice table if so.
   */
  public void collect() {
    if (p.moveX + p.playerSize/2 > this.x && p.moveX - p.playerSize/2 < this.x +this.size && p.moveY + p.playerSize/2 > this.y && p.moveY - p.playerSize/2 < this.y + this.size) {
      rollTheDice();
      collected = true;
    }
  }
  /**
   Will switch gun to whats in the box.
   Uzi - 30%
   Sniper - 30%
   AR - 40%
   */
  public void rollTheDice() {

    int number = (int)random(0, 100);

    if (number < 30) {
      g = new Uzi();
    }
    if (number > 30 && number < 60) { 
      g = new Sniper();
    }
    if (number > 60) { 
      g = new AR();
    }
  }
  public float randomX() {
    return random(25, width- 25);
  }
  public float randomY() {
    return random(height/14, height - 25 );
  }
  /**
   Prevents box from spawning in a wall
   */
  public boolean inWall() {
    this.x = this.randomX();
    this.y = this.randomY();
    for (int i = 0; i < walls.length; i++) {
      if (this.x >= walls[i].xLeft - 5 && this.x <= walls[i].xRight + 5 && 
        this.y >= walls[i].yTop - 10  && this.y <= walls[i].yBot + 5) {
        inWall();
      }
    }
    return true;
  }
}
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

  public void display() {
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
  public void playerHealth() {
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
  public void aim() {
    float angle = atan2(this.moveY - mouseY, mouseX - this.moveX); 
    aim = -angle;
  }

  // Basic movement controls with the use of WSAD
  public void moveLeft() {
    if (moveX - playerSize/2 > 0 && wRight == false) {
      this.moveX -= this.speed;
    }  
    wRight = false;
  }
  public void moveRight() {
    if (moveX + playerSize/2 < width && wLeft == false) {
      this.moveX += this.speed;
    }
    wLeft = false;
  }
  public void moveUp() {
    if (moveY - playerSize/2 > 0 && wBottom == false) {
      this.moveY -= this.speed;
    }
    wBottom = false;
  }
  public void moveDown() {
    if (moveY + playerSize/2 < height && wTop == false) {
      this.moveY += this.speed;
    }
    wTop = false;
  }
  //Diagonally right up movement
  public void moveUpRight() {
    if (moveY - playerSize/2 > 0 && wBottom == false) {
      this.moveY -= this.speed/1.5f;
    }
    if (moveX + playerSize/2 < width && wLeft == false) {
      this.moveX += this.speed/1.5f;
    }
    wBottom = false;
    wLeft = false;
  }

  // diagonally down right movement
  public void moveDownRight() {
    if (moveY + playerSize/2 < height && wTop == false) {
      this.moveY += this.speed/1.5f;
    }
    if (moveX + playerSize/2 < width && wLeft == false) {
      this.moveX += this.speed/1.5f;
    }
    wTop = false;
    wLeft = false;
  }

  //diagonaly up left movement
  public void moveUpLeft() {
    if (moveY - playerSize/2 > 0 && wBottom == false) {
      this.moveY -= this.speed/1.5f;
    }
    if (moveX - playerSize/2 > 0 && wRight == false) {
      this.moveX -= this.speed/1.5f;
    }
    wBottom = false;
    wRight = false;
  }

  //diagonal down left movement
  public void moveDownLeft() {
    if (moveY + playerSize/2 < height && wTop == false) {
      this.moveY += this.speed/1.5f;
    }
    if (moveX - playerSize/2 > 0 && wRight == false) {
      this.moveX -= this.speed/1.5f;
    }
    wTop = false;
    wRight = false;
  }
}
/**
  The Projectiles fired from the guns
*/
class Projectile {

  PVector start, target, dir, pos; 
  float changeX, changeY;
  float angle;
  float gunLength, playerSize;
  float moveSpeed = 20;
  
  float damage;

  Projectile(float x, float y, float x1, float y1, float aim, float gunLength, float playerSize, float damage) {
    
    this.gunLength = gunLength;
    this.playerSize = playerSize;

    this.damage = damage;
    
    this.start = new PVector(x, y);
    this.target = new PVector(x1, y1);

    this.pos = this.start.copy();

    this.dir = start.copy().sub(target).normalize();

    this.angle = aim;
  }
  public void display() {

    strokeWeight(2);
    pushMatrix();
    translate(this.pos.x, this.pos.y);
    rotate(this.angle);
    // the bullet
    rectMode(CENTER);
    rect(gunLength, 0, 10, 1);
    rectMode(CORNER);
    travel();
    popMatrix();
  }
  public void travel() {
    this.pos = this.pos.add(this.dir.copy().mult( -moveSpeed ));
  }
}
/**
  Lootbox Sniper Gun. One Hits Standard Zombies
*/
class Sniper extends Gun {
  // Gun set variables
  float gunLength = 40;
  float gunWidth = 5;
  float rateOfFire = 25;
  float recoil = 0;
  int damage = 10;

  int ammo = 20;
  int bullet;
  
  int c = color(58, 61, 59);
  Sniper() {
  }

  public void display() {
    fill(c);
    rect(p.x - 5, p.y, gunLength, gunWidth);
    fill(0);
  }
  public int ammo(){
    return ammo;
  }

  public void shoot() {
    if(frameCount%this.rateOfFire == 0){
    if (bullet == 1000) {
      bullet = 0;
    }
    
    // random values attatched to mouseX and mouseY are for the recoil
    projectiles[bullet] = new Projectile(p.moveX+2, p.moveY, mouseX + random(-recoil, recoil), mouseY + random(-recoil, recoil), p.aim, gunLength, p.playerSize, damage);
    bullet++;
    ammo--;
    if(ammo == 0){
      g = null;
      g = new Gun();
    }
  }
  }
}
class Target {

  float x, y;
  float w, h;
  int count = 0;


  int c;

  Target(float x, float y) {
    this.x = x;
    this.y = y;
    this.w = 50;
    this.h = 50;

    this.c = color(255, 0, 0);
  }
  public void display() {
    fill(c);
    strokeWeight(2);
    rect(this.x, this.y, this.w, this.h);
    textSize(15);
    fill(0);
    text(count, this.x + 10, this.y + 25);
    checkHit();
  }

  public void checkHit() {
    for (int i = 0; i < projectiles.length; i++) {
      if (projectiles[i] != null) {
        float relativeX = projectiles[i].pos.x;
        float relativeY = projectiles[i].pos.y;
        if (relativeX >= this.x && relativeX <= this.x+this.w && relativeY >= this.y && relativeY <= this.y + this.h) {
          count++; 
          projectiles[i] = null;
        }
        if (this.count%10 == 0 && this.count > 0) {
          this.c = color(0, 255, 0);
        }
      }
    }
  }
}
/**
  Lootbox Uzi Gun. High Rate of Fire. Decreased Damage
*/
class Uzi extends Gun {
  // Gun set variables
  float gunLength = 15;
  float gunWidth = 5;
  float rateOfFire = 2;
  float recoil = 50;
  float damage = 0.75f;

  int ammo = 300;
  int bullet;
  
  int c = color(108, 101, 100);
  Uzi() {
  }

  public void display() {
    fill(c);
    rect(p.x + 5, p.y, gunLength, gunWidth);
    fill(0);
  }
  public int ammo(){
    return ammo;
  }

  public void shoot() {
    if(frameCount%this.rateOfFire == 0){
    if (bullet == 1000) {
      bullet = 0;
    }
    
    // random values attatched to mouseX and mouseY are for the recoil
    projectiles[bullet] = new Projectile(p.moveX+2, p.moveY, mouseX + random(-recoil, recoil), mouseY + random(-recoil, recoil), p.aim, gunLength, p.playerSize, damage);
    bullet++;
    ammo--;
    if(ammo == 0){
      g = null;
      g = new Gun();
    }
  }
  }
}
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

  public void build() {
    fill(255);
    stroke(0);
    strokeWeight(3);
    rect(this.x, this.y, this.w, this.h);
    this.checkHit();
  }

  public void top() {
    fill(150);
    strokeWeight(3);
    rect(this.x, this.y, this.w, -this.h/1.5f);
  }
  public void shadow() {
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
  public boolean inWallTop(float x, float y, float size) {
    if (x >= this.xLeft - size*0.5f && x <= this.xRight + size*0.5f && y >= this.yTop - size*0.5f && y <= this.yTop) {
      println("TOP");return true;
    } else return false;
  }
  public boolean inWallBottom(float x, float y, float size) {
    if (x >= this.xLeft - size*0.5f && x <= this.xRight + size*0.5f && y <= this.yBot + size*0.5f && y >= this.yBot) {
      println("Bottom");return true;
    } else return false;
  }
  public boolean inWallLeft(float x, float y, float size) {
    if (x >= this.xLeft - size*0.5f && x <= this.xLeft && y >= this.yTop - size*0.5f && y <= this.yBot + size*0.5f) {
      println("LEFT");return true;
    } else return false;
  }
  public boolean inWallRight(float x, float y, float size) {
    if (x <= this.xRight + size*0.5f && x >= this.xRight && y >= this.yTop - size*0.5f && y <= this.yBot + size*0.5f) {
      println("RIGHT");return true;
    } else return false;
  }
  /**
   Checks if projectile has hit wall
   */
  public void checkHit() {
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
/**
 Default Zombie.
 */
class Zombie {

  float x, y, moveX, moveY;
  float size  = 25;

  float health = 10;
  int maxHealth = (int) health;

  int c = color(105, 131, 98);

  float moveSpeed = 1;
  float look;
  int score = 25;

  boolean wTop, wBottom, wLeft, wRight;

  Zombie(float moveX, float moveY) {
    this.x = 0;

    this.moveX = moveX;
    this.moveY = moveY;
  }
  public void display() {

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
  public void look() {
    float angle = atan2(this.moveY - p.moveY, p.moveX - this.moveX); 
    this.look = -angle;
  }
  // Returning score of zombie ( 25 )
  public int score() {
    return this.score;
  }

  // Moves this Zombie
  public void move() {

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
          if (changeX > -0.5f) {
            this.moveX += -0.5f;
          } else this.moveX += changeX;
        } else {
          if (changeX < 0.5f) {
            this.moveX += 0.5f;
          } else this.moveX += changeX;
        }
        if (changeY < 0) {
          this.moveY += changeY;
        }
        // Acts if the bottom wall is the current face
      } else if (wBottom == true) {
        if (changeX < 0) {
          if (changeX > -0.5f) {
            this.moveX += -0.5f;
          } else this.moveX += changeX;
        } else {
          if (changeX < 0.5f) {
            this.moveX += 0.5f;
          } else this.moveX += changeX;
        }
        if (changeY > 0) {
          this.moveY += changeY;
        }
        // Acts if the Left wall is current face
      } else if (wLeft == true) {
        if (changeY < 0) {
          if (changeY > -0.8f) {
            this.moveY += -0.8f;
          } else this.moveY += changeY;
        } else {
          if (changeY < 0.8f) {
            this.moveY += 0.8f;
          } else this.moveY += changeY;
        }
        if (changeX < 0) {
          this.moveX += changeX;
        }
        // Acts if the Right wall is the current face
      } else if (wRight == true) {
        if (changeY < 0) {
          if (changeY > -0.8f) {
            this.moveY += -0.8f;
          } else this.moveY += changeY;
        } else {
          if (changeY < 0.8f) {
            this.moveY += 0.8f;
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
  public void healthBar() {
    float greenBar = this.size * health/maxHealth;
    float redBar = this.size * maxHealth/maxHealth;
    fill(255, 0, 0);
    rect(this.moveX - this.size/2, this.moveY + size, redBar, 7.5f);
    fill(0, 255, 0);
    rect(this.moveX - this.size/2, this.moveY + size, greenBar, 7.5f);
  }
  // Damages Player
  public void attack() {
    p.health--;
  }
  // Boolean returns state of Zombie
  public boolean isDead() {
    if (health <= 0) {
      return true;
    } else return false;
  }
  // Checks the Projectiles array to see if there is collision
  public void checkHit() {
    for (int i = 0; i < projectiles.length; i++) {
      if (projectiles[i] != null) {
        float relativeX = projectiles[i].pos.x;
        float relativeY = projectiles[i].pos.y;
        if (relativeX >= this.moveX - this.size/1.5f && relativeX <= this.moveX+ this.size/1.5f && 
          relativeY >= this.moveY - this.size/1.5f && relativeY <= this.moveY + this.size/1.5f) {
          health -= projectiles[i].damage; 
          projectiles[i] = null;
        }
      }
    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Boxhead" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
