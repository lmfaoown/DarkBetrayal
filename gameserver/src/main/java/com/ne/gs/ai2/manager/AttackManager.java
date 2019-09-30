/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package com.ne.gs.ai2.manager;

import com.ne.gs.ai2.*;
import com.ne.gs.ai2.event.AIEventType;
import com.ne.gs.model.gameobjects.Creature;
import com.ne.gs.model.gameobjects.Npc;

/**
 * @author ATracer
 */
public final class AttackManager {

    /**
     * @param npcAI
     */
    public static void startAttacking(NpcAI2 npcAI) {
        if (npcAI.isLogging()) {
            AI2Logger.info(npcAI, "AttackManager: startAttacking");
        }
        npcAI.getOwner().getGameStats().setFightStartingTime();
        EmoteManager.emoteStartAttacking(npcAI.getOwner());
        scheduleNextAttack(npcAI);
    }

    /**
     * @param npcAI
     */
    public static void scheduleNextAttack(NpcAI2 npcAI) {
        if (npcAI.isLogging()) {
            AI2Logger.info(npcAI, "AttackManager: scheduleNextAttack");
        }

        // don't start attack while in casting substate
        AISubState subState = npcAI.getSubState();
        if (subState == AISubState.NONE) {
            chooseAttack(npcAI, npcAI.getOwner().getGameStats().getNextAttackInterval());
        } else if (npcAI.isLogging()) {
            AI2Logger.info(npcAI, "Will not choose attack in substate" + subState);
        }
    }

    /**
     * choose attack type
     */
    protected static void chooseAttack(NpcAI2 npcAI, int delay) {
        AttackIntention attackIntention = npcAI.chooseAttackIntention();
        if (npcAI.isLogging()) {
            AI2Logger.info(npcAI, "AttackManager: chooseAttack " + attackIntention + " delay " + delay);
        }

        if (!npcAI.canThink()) {
            return;
        }
        switch (attackIntention) {
            case SIMPLE_ATTACK:
                SimpleAttackManager.performAttack(npcAI, delay);
                break;
            case SKILL_ATTACK:
                SkillAttackManager.performAttack(npcAI, delay);
                break;
            case FINISH_ATTACK:
                npcAI.think();
                break;
        }
    }

    /**
     * @param npcAI
     */
    public static void targetTooFar(NpcAI2 npcAI) {
        Npc npc = npcAI.getOwner();
        if (npcAI.isLogging()) {
            AI2Logger.info(npcAI, "AttackManager: attackTimeDelta " + npc.getGameStats().getLastAttackTimeDelta());
        }

        // switch target if there is more hated creature
        if (npc.getGameStats().getLastChangeTargetTimeDelta() > 5) {
            Creature mostHated = npc.getAggroList().getMostHated();
            if (mostHated != null && !mostHated.getLifeStats().isAlreadyDead() && !npc.isTargeting(mostHated.getObjectId())) {
                if (npcAI.isLogging()) {
                    AI2Logger.info(npcAI, "AttackManager: switching target during chase");
                }
                npcAI.onCreatureEvent(AIEventType.TARGET_CHANGED, mostHated);
                return;
            }
        }

        //if(npc.getEffectController().isUnderFear())
            //return;

        if (!npc.canSee((Creature) npc.getTarget())) {
            npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
            return;
        }
        if (checkGiveupDistance(npcAI)) {
            npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
            return;
        }
        if (npcAI.isMoveSupported()) {
            npc.getMoveController().moveToTargetObject();
            return;
        }
        npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
    }

    private static boolean checkGiveupDistance(NpcAI2 npcAI) {
        Npc npc = npcAI.getOwner();
        // if target run away too far
        float distanceToTarget = npc.getDistanceToTarget();
        if (npcAI.isLogging()) {
            AI2Logger.info(npcAI, "AttackManager: distanceToTarget " + distanceToTarget);
        }
        // TODO may be ask AI too
        int chaseTarget = npc.isBoss() ? 50 : npc.getPosition().getWorldMapInstance().getTemplate().getAiInfo().getChaseTarget();
        if (distanceToTarget > chaseTarget) {
            return true;
        }
        double distanceToHome = npc.getDistanceToSpawnLocation();
        // if npc is far away from home
        int chaseHome = npc.isBoss() ? 150 : npc.getPosition().getWorldMapInstance().getTemplate().getAiInfo().getChaseHome();
        if (distanceToHome > chaseHome) {
            return true;
        }

        if (chaseHome <= 200
                && (npc.getGameStats().getLastAttackTimeDelta() > 20 && npc.getGameStats().getLastAttackedTimeDelta() > 20 || distanceToHome > chaseHome / 2
                && npc.getGameStats().getLastAttackedTimeDelta() > 10)) {
            return true;
        }

        return false;
    }

}