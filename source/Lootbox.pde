/**
 Lootbox, Spawns randomly every 1000 frames. Contains Weapons
 */
class Lootbox {
  float x, y, size;
  boolean goodPos = false; 
  boolean collected = false;
  color c = color(255, 153, 51);
  Lootbox() {
    goodPos = inWall();
    this.size = 10;
  }

  void display() {
    if (goodPos == true && collected == false) {
      fill(c);
      rect(this.x, this.y, this.size, this.size);
      collect();
    }
  }
  /**
   Checks If Box Is Collected, Goes to rollTheDice table if so.
   */
  void collect() {
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
  void rollTheDice() {

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
  float randomX() {
    return random(25, width- 25);
  }
  float randomY() {
    return random(height/14, height - 25 );
  }
  /**
   Prevents box from spawning in a wall
   */
  boolean inWall() {
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
