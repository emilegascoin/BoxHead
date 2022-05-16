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

  void display() {
    fill(0);
    rect(p.x + 5, p.y, gunLength, gunWidth);
  }
  // Returns ammo total
  int ammo(){
    return ammo;
  }

// Method to shoot the gun
  void shoot() {
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
