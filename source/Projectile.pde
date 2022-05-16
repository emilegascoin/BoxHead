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
  void display() {

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
  void travel() {
    this.pos = this.pos.add(this.dir.copy().mult( -moveSpeed ));
  }
}
