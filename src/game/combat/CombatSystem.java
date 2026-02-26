// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.engine.GameLogger;
import game.engine.GameWorld;
import game.items.Treasure;
import game.map.Position;

/**
 * Handles combat logic between two entities in the game world.
 */
public class CombatSystem {

    public static void resolveCombat(Combatant attacker, Combatant defender, GameWorld world) {
        Position attackerPos = attacker.getPosition();
        Position defenderPos = defender.getPosition();

        boolean inRange = false;

        if (attacker instanceof MeleeFighter mf) {
            inRange = mf.isInMeleeRange(attackerPos, defenderPos);
        } else if (attacker instanceof RangedFighter rf) {
            inRange = rf.isInRange(attackerPos, defenderPos);
        }

        if (!inRange) {
            GameLogger.log("Combat skipped: Target out of range.");
            return;
        }

        if (defender.tryEvade()) {
            GameLogger.log("Combat: " + defender.getDisplaySymbol() + " evaded attack from " + attacker.getDisplaySymbol());
            return;
        }

        int preHP = defender.getHealth();

        if (attacker instanceof MagicAttacker ma) {
            ma.castSpell(defender);
        } else if (attacker instanceof PhysicalAttacker pa) {
            pa.attack(defender);
        } else {
            GameLogger.log("Combat failed: Attacker cannot attack.");
            return;
        }

        int postHP = defender.getHealth();
        int damageDealt = preHP - postHP;

        GameLogger.log("Combat: " + attacker.getDisplaySymbol() + " dealt " + damageDealt +
                " to " + defender.getDisplaySymbol() + ". Remaining HP: " + postHP);

        if (defender.isDead()) {
            defender.defeat();
            GameLogger.log("Combat: " + defender.getDisplaySymbol() + " defeated at " + defenderPos);

            if (defender instanceof LootDropper dropper) {
                Treasure loot = dropper.generateLoot();
                if (loot != null) {
                    world.addItem(loot);
                    world.getMap().addEntity(loot);

                    if (attacker instanceof PlayerCharacter pc) {
                        pc.updateTreasurePoint(loot.getValue());
                        GameLogger.log("Loot: Player collected treasure worth " + loot.getValue());
                    } else {
                        GameLogger.log("Loot: Treasure dropped at " + loot.getPosition());
                    }
                } else {
                    GameLogger.log("Loot: No treasure dropped.");
                }
            }

            if (defender instanceof Enemy) {
                world.removeEnemy((Enemy) defender);
            }

        } else if (defender instanceof PhysicalAttacker counter && defender instanceof Combatant) {
            GameLogger.log("Combat: " + defender.getDisplaySymbol() + " counterattacks.");

            int attackerHPBefore = attacker.getHealth();
            counter.attack(attacker);
            int attackerHPAfter = attacker.getHealth();
            int damageTaken = attackerHPBefore - attackerHPAfter;

            GameLogger.log("Combat: " + defender.getDisplaySymbol() + " dealt " + damageTaken +
                    " in retaliation. " + attacker.getDisplaySymbol() + " now has " + attackerHPAfter + " HP.");

            if (attacker instanceof PlayerCharacter pc && attacker.isDead()) {
                pc.defeat();
            }
        }
    }
}
