class Target {

  float x, y;
  float w, h;
  int count = 0;


  color c;

  Target(float x, float y) {
    this.x = x;
    this.y = y;
    this.w = 50;
    this.h = 50;

    this.c = color(255, 0, 0);
  }
  void display() {
    fill(c);
    strokeWeight(2);
    rect(this.x, this.y, this.w, this.h);
    textSize(15);
    fill(0);
    text(count, this.x + 10, this.y + 25);
    checkHit();
  }

  void checkHit() {
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
