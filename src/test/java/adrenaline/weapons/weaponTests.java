package adrenaline.weapons;

import adrenaline.PowerUpCard;
import org.junit.jupiter.api.Test;

public class weaponTests {
    private RocketLaucher rocketLaucher;
    private Railgun railgun;
    private PowerGlove powerGlove;
    private PlasmaGun plasmaGun;
    private MachineGun machineGun;
    private LockRifle lockRifle;
    private Hellion hellion;
    @Test
    public void constructor(){
        hellion=new Hellion();
        hellion.canShootAlt();
        hellion.canShootBase();
        hellion.toString();

        lockRifle=new LockRifle();
        lockRifle.canShootBase();
        lockRifle.canShootOp1();
        lockRifle.toString();

        machineGun=new MachineGun();
        machineGun.canShootBase();
        machineGun.canShootOp1();
        machineGun.canShootOp2();
        machineGun.toString();

        rocketLaucher=new RocketLaucher();
        railgun=new Railgun();
        powerGlove=new PowerGlove();
        plasmaGun=new PlasmaGun();

        plasmaGun.canShootBase();
        plasmaGun.canShootOp1();
        plasmaGun.canShootOp2();
        plasmaGun.toString();

        powerGlove.canShootAlt();
        powerGlove.canShootBase();
        powerGlove.toString();

        railgun.canShootAlt();
        railgun.canShootBase();
        railgun.toString();

        rocketLaucher.canShootBase();
        rocketLaucher.canShootOp1();
        rocketLaucher.canShootOp2();
        rocketLaucher.toString();
    }
}
