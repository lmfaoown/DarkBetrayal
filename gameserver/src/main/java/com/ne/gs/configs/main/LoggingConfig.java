/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package com.ne.gs.configs.main;

import com.ne.commons.configuration.Property;

public final class LoggingConfig {

    @Property(key = "gameserver.enable.advanced.logging", defaultValue = "false")
    public static boolean ENABLE_ADVANCED_LOGGING;

    /**
     * Logging
     */
    @Property(key = "gameserver.log.audit", defaultValue = "true")
    public static boolean LOG_AUDIT;

    @Property(key = "gameserver.log.autogroup", defaultValue = "true")
    public static boolean LOG_AUTOGROUP;

    @Property(key = "gameserver.log.chat", defaultValue = "true")
    public static boolean LOG_CHAT;

    @Property(key = "gameserver.log.craft", defaultValue = "true")
    public static boolean LOG_CRAFT;

    @Property(key = "gameserver.log.faction", defaultValue = "false")
    public static boolean LOG_FACTION;

    @Property(key = "gameserver.log.gmaudit", defaultValue = "true")
    public static boolean LOG_GMAUDIT;

    @Property(key = "gameserver.log.ingameshop", defaultValue = "false")
    public static boolean LOG_INGAMESHOP;
    
	@Property(key = "gameserver.log.ingameshop.sql", defaultValue = "true")
	public static boolean LOG_INGAMESHOP_SQL;

    @Property(key = "gameserver.log.item", defaultValue = "true")
    public static boolean LOG_ITEM;

    @Property(key = "gameserver.log.kill", defaultValue = "false")
    public static boolean LOG_KILL;

    @Property(key = "gameserver.log.pl", defaultValue = "false")
    public static boolean LOG_PL;

    @Property(key = "gameserver.log.mail", defaultValue = "false")
    public static boolean LOG_MAIL;

    @Property(key = "gameserver.log.player.exchange", defaultValue = "false")
    public static boolean LOG_PLAYER_EXCHANGE;

    @Property(key = "gameserver.log.broker.exchange", defaultValue = "false")
    public static boolean LOG_BROKER_EXCHANGE;

    @Property(key = "gameserver.log.siege", defaultValue = "false")
    public static boolean LOG_SIEGE;

    @Property(key = "gameserver.log.sysmail", defaultValue = "false")
    public static boolean LOG_SYSMAIL;

    @Property(key = "gameserver.log.auction", defaultValue = "true")
    public static boolean LOG_HOUSE_AUCTION;

    @Property(key = "gameserver.log.quest.complete", defaultValue = "true")
    public static boolean LOG_QUEST_COMPLETE;
}