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
  
  color c = color(58, 61, 59);
  Sniper() {
  }

  void display() {
    fill(c);
    rect(p.x - 5, p.y, gunLength, gunWidth);
    fill(0);
  }
  int ammo(){
    return ammo;
  }

  void shoot() {
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
