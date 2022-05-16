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
void setup() {
  //size(1500, 900);
  fullScreen();

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

void draw() {
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
    text(String.format("%.0f", frameRate), width - width * 0.025, height - height /100);

    // Round indicator
    textSize(50);
    fill(255, 0, 0);
    text(round, 10, 50);

    // Score Indicator
    textSize(35);
    fill(255, 0, 0);
    text("SCORE: " + score, width/2, height /100 * 4.5);

    // Total Ammo
    textSize(35);
    fill(0);
    if (g.ammo() == 1111) {
      text("AMMO: " + "Inf.", width/1.4, height /100 * 4.5);
    } else text("AMMO: " + g.ammo(), width/1.4, height /100 * 4.5);

    // Pause button
    pause();


    // Checks if round is over and spawns a zombie for each round there is
    if (zCheck == zTotal) {
      round++; 

      bossTotal = (int) round / 10;
      for (int i = 0; i < round; i++) {

        // Randomises which side and points the zombies will come from
        int side = (int) random(1, 4.9);
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
void gameOver() {
  background(50);
  float x = (width/10) * 4;
  float y = (height/10) * 2;
  float w = width/5;
  float h = height/2;

  strokeWeight(5);
  fill(220);
  rect(x, y, w, h);

  textSize(w/6.5);
  fill(0);
  text(" GAME OVER ", x + w * 0.015, y + h/6);

  fill(200, 5, 10);
  textSize(w/12);
  text("SCORE: " + score, x + w * 0.2, y + h/4); 

  fill(200, 5, 10);
  textSize(w/12);
  text("ROUND: " + round, x + w * 0.2, y + h/3); 

  color c = color(240);

  float frameWidth = w/1.3;
  float frameHeight = h/8;
  float frameX = x + w * 0.115;
  float frameY = y + h * 0.4;
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
  text(" NEW GAME ", frameX, frameY * 1.11);

  frameY = y + h * 0.6;
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
  text(" NEW GAME+ ", frameX, frameY * 1.09);

  frameY = y + h * 0.8;
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
  text(" EXIT TO DESKTOP ", frameX, frameY * 1.07);
}
/** 
 Start New Game
 */
void newGame() {
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
void newGamePlus() {
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
void pause() {
  color c = color(200);
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
void mouseClicked() {
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
void mousePressed() {
  g.shoot();
}
/**
 keyPressed and Released for key inputs 'WSAD'
 */
void keyPressed() {
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

void keyReleased() {
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
