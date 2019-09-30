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

public final class EnchantsConfig {

    /**
     * Supplement Additional Rates
     */
    @Property(key = "gameserver.supplement.lesser", defaultValue = "1.15")
    public static float LESSER_SUP;

    @Property(key = "gameserver.supplement.regular", defaultValue = "1.20")
    public static float REGULAR_SUP;

    @Property(key = "gameserver.supplement.greater", defaultValue = "1.30")
    public static float GREATER_SUP;

    /**
     * Max enchant level
     */
    @Property(key = "gameserver.enchant.type1", defaultValue = "10")
    public static int ENCHANT_MAX_LEVEL_TYPE1;
    @Property(key = "gameserver.enchant.type2", defaultValue = "15")
    public static int ENCHANT_MAX_LEVEL_TYPE2;

    /**
     * ManaStone Rates
     */
    @Property(key = "gameserver.base.manastone", defaultValue = "50")
    public static float MANA_STONE;
    @Property(key = "gameserver.base.enchant", defaultValue = "60")
    public static float ENCHANT_STONE;
}