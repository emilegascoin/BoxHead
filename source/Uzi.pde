/**
  Lootbox Uzi Gun. High Rate of Fire. Decreased Damage
*/
class Uzi extends Gun {
  // Gun set variables
  float gunLength = 15;
  float gunWidth = 5;
  float rateOfFire = 2;
  float recoil = 50;
  float damage = 0.75;

  int ammo = 300;
  int bullet;
  
  color c = color(108, 101, 100);
  Uzi() {
  }

  void display() {
    fill(c);
    rect(p.x + 5, p.y, gunLength, gunWidth);
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
