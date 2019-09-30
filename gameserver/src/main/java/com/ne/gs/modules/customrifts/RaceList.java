/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package com.ne.gs.modules.customrifts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

import com.ne.commons.utils.xml.ElementList;
import com.ne.gs.model.Race;

/**
 * @author hex1r0
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "RaceList")
public class RaceList extends ElementList<Race> {

    @XmlElement(name = "race")
    public List<Race> getRaces() {
        return getElements();
    }
}