/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.ishalgen;

import com.ne.gs.model.Race;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;
import com.ne.gs.services.QuestService;

/**
 * @author MrPoke
 */
public class _2000Prologue extends QuestHandler {

    private final static int questId = 2000;

    public _2000Prologue() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerOnEnterWorld(questId);
        qe.registerOnMovieEndQuest(2, questId);
    }

    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        if (player.getCommonData().getRace() != Race.ASMODIANS) {
            return false;
        }
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            env.setQuestId(questId);
            QuestService.startQuest(env);
        }
        qs = player.getQuestStateList().getQuestState(questId);
        if (qs.getStatus() == QuestStatus.START) {
            player.sendPck(new SM_PLAY_MOVIE(1, 2));
            return true;
        }
        return false;
    }

    @Override
    public boolean onMovieEndEvent(QuestEnv env, int movieId) {
        if (movieId != 2) {
            return false;
        }
        Player player = env.getPlayer();
        if (player.getCommonData().getRace() != Race.ASMODIANS) {
            return false;
        }
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }
        qs.setStatus(QuestStatus.REWARD);
        QuestService.finishQuest(env);
        return true;
    }
}