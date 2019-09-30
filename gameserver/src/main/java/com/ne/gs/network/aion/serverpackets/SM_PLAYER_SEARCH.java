/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package com.ne.gs.network.aion.serverpackets;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.network.aion.AionConnection;
import com.ne.gs.network.aion.AionServerPacket;

/**
 * Sent to fill the search panel of a players social window<br />
 * I.E.: In response to a <tt>CM_PLAYER_SEARCH</tt>
 *
 * @author Ben
 */
public class SM_PLAYER_SEARCH extends AionServerPacket {

    private static final Logger log = LoggerFactory.getLogger(SM_PLAYER_SEARCH.class);

    private final List<Player> players;
    private final int region;

    /**
     * Constructs a new packet that will send these players
     *
     * @param players List of players to show
     * @param region of search - should be passed as parameter to prevent null
     * in player.getActiveRegion()
     */
    public SM_PLAYER_SEARCH(List<Player> players, int region) {
        this.players = new ArrayList<>(players);
        this.region = region;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeH(players.size());
        for (Player player : players) {
            if (player.getActiveRegion() == null) {
                log.warn("CHECKPOINT: null active region for " + player.getObjectId() + "-" + player.getX() + "-" + player.getY() + "-" + player.getZ());
            }
            writeD(player.getActiveRegion() == null ? region : player.getActiveRegion().getMapId());
            writeF(player.getPosition().getX());
            writeF(player.getPosition().getY());
            writeF(player.getPosition().getZ());
            writeC(player.getPlayerClass().getClassId());
            writeC(player.getGender().getGenderId());
            writeC(player.getLevel());
            if (player.isInGroup2()) {
                writeC(3);
            } else if (player.isLookingForGroup()) {
                writeC(2);
            } else {
                writeC(0);
            }
            writeS(player.getName(), 56);

        }
    }

}