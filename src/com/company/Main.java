package com.company;

import java.util.Random;

public class Main {
    static Random rn = new Random();
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {280, 270, 250, 300, 200, 200, 600, 150};
    public static int[] heroesDamage = {10, 15, 20, 0, 10, 30, 5, 25};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Lucky", "Berserk", "Golem", "Thor"};
    public static int roundNumber = 1;


    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            playRound();
        }
    }
//*Добавить n-го игрока, Thor,
// удар по боссу имеет шанс оглушить босса на 1 раунд, вследствие чего босс пропускает 1 раунд и не наносит урон героям.
// random.nextBoolean(); - true, false
    public static void Thor() {
        boolean isBlind = rn.nextBoolean();
        int damageBoss = bossDamage;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[7] > 0 && isBlind == true) {
                bossDamage = 0;
                System.out.println("Boss оглушён");
            } else {
                bossDamage = damageBoss;
            }
            break;
        }

    }
//**Добавить n-го игрока, Golem, который имеет увеличенную жизнь но слабый удар.
// Принимает на себя 1/5 часть урона исходящего от босса по другим игрокам
    public static void Golem() {
        int golemTaken = bossDamage / 5;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesAttackType[i].equals("Golem")) {
                continue;
            }
           else  if (heroesHealth[6] > 0) {
                 heroesHealth[6] -= golemTaken;
             }

            else if (bossDamage == 50) {
                    bossDamage -= golemTaken;
                    break;
                }


        }


    }
    //**Добавить n-го игрока, Berserk, блокирует часть удара босса по себе и прибавляет заблокированный урон к своему урону и возвращает его боссу
    public static void Berserk() {
        int berserkDamage = heroesDamage[5];
        int sheld = bossDamage/3;
        for (int i = 0; i < heroesHealth.length; i++) {

            if (heroesDamage[5]>30) {
                heroesDamage[5] = berserkDamage;
            }
                if (heroesHealth[5] > 0 ) {
                    heroesHealth[5] += sheld;
                    if(heroesDamage[5] <= 30) {
                        heroesDamage[5] += sheld;
                    }
                    break;
                }


        }
    }
//*Добавить n-го игрока, Lucky, имеет шанс уклонения от ударов босса.
    public static void Lucky(){
        boolean evasion;
        evasion= rn.nextBoolean();
        for (int i = 0; i < heroesHealth.length; i++) {
            if (evasion ==true&&heroesHealth[4]>0) {
                heroesHealth[4]+=bossDamage;
                System.out.println("lucky уклонился");
            } else {
                System.out.println("lucky не уклонился");
            }
            break;
        }

    }
    //Добавить 4-го игрока Medic, у которого есть способность лечить после каждого раунда на N-ное количество единиц здоровья только одного из членов команды,
    // имеющего здоровье менее 100 единиц. Мертвых героев медик оживлять не может, и лечит он до тех пор пока жив сам.
    // Медик не участвует в бою, но получает урон от Босса. Сам себя медик лечить не может.
public static void MedicHealth(){
    for (int i = 0; i < heroesHealth.length; i++) {
        if(heroesAttackType[i].equals("Medic")){
            continue;
        }
        else if(heroesHealth[i]<100&&heroesHealth[i]>0&&heroesHealth[3]>0){
            int addHealth=rn.nextInt(5,8);
            heroesHealth[i]+=addHealth;
            System.out.println("Медик вылечил "+heroesAttackType[i]);
            break;
        }
    }
}
    public static void chooseBossDefence() {
        int randomIndex = rn.nextInt(heroesAttackType.length); // 0,1,2
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossHits();
        heroesHit();
        MedicHealth();
        Golem();
        Lucky();
        Berserk();
        Thor();
        printStatistics();
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (bossDefence == heroesAttackType[i]) {
                    int coeff = rn.nextInt(8) + 2; // 2,3,4,5,6,7,8,9
                    damage = damage * coeff;
                    System.out.println("Critical damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth -= damage;
                }
            }
        }
    }

    public static void bossHits() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] -= bossDamage; // heroesHealth[i] = heroesHealth[i] - bossDamage;
                }
            }
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        /*if (heroesHealth[0] <= 0 && heroesHealth[1] <= 0 && heroesHealth[2] <= 0) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;*/
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ---------------");
        /*String defence;
        if (bossDefence == null) {
            defence = "No defence";
        } else {
            defence = bossDefence;
        }*/
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: "
                + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]
                    + " damage: " + heroesDamage[i]);
        }
    }
}
