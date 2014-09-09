/**************************************************************************************************
 * _____   __     _______________________________________ 
 * ___  | / /     ___  __ \__  __ \_  __ \__  __ \__  __ \
 * __   |/ /________  / / /_  /_/ /  / / /_  /_/ /_  /_/ /
 * _  /|  /_/_____/  /_/ /_  _, _// /_/ /_  ____/_  ____/
 * /_/ |_/        /_____/ /_/ |_| \____/ /_/     /_/
 * 
 * National Collegiate Dodgeball Association (NCDA)
 * NCDA - Dodgeball Referee Officiating Application
 * http://www.ncdadodgeball.com
 * Copyright 2014. All Rights Reserved.
 *************************************************************************************************/
package com.ncdadodgeball.util;

import java.io.Serializable;

public class Event implements Serializable
{
	private static final long serialVersionUID = 0xD0D83BA11L;

	public static enum TYPE{ 	GC_PAUSE_RESUME, 
								SC_RESUME, SC_PAUSE, SC_RESET, 
								GAME_START, ADD_PLAYER, REMOVE_PLAYER };
								
								
	private TYPE 				m_eType;
	private GameSettings.STAFF	m_sSender;
	private GameSettings.STAFF	m_sReceiver;
	private int 				m_nValue;
	private long				m_lValue;
	private String 				m_strValue;
	
	public Event(TYPE event, GameSettings.STAFF sender, GameSettings.STAFF receiver, int value){
		m_eType = event;
		m_sSender = sender;
		m_sReceiver = receiver;
		m_nValue = value;
	}
	
	public Event(TYPE event, GameSettings.STAFF sender, GameSettings.STAFF receiver, String value){
		m_eType = event;
		m_sSender = sender;
		m_sReceiver = receiver;
		m_strValue = value;
	}
	
	public Event(TYPE event, GameSettings.STAFF sender, GameSettings.STAFF receiver, long value){
		m_eType = event;
		m_sSender = sender;
		m_sReceiver = receiver;
		m_lValue = value;
	}

	public TYPE getType() {
		return m_eType;
	}

	public void setType(TYPE type) {
		this.m_eType = type;
	}
	
	public GameSettings.STAFF getSender() {
		return m_sSender;
	}

	public void setSender(GameSettings.STAFF sender) {
		this.m_sSender = sender;
	}

	public GameSettings.STAFF getReceiver() {
		return m_sReceiver;
	}

	public void setReceiver(GameSettings.STAFF receiver) {
		this.m_sReceiver = receiver;
	}

	public int getNumericValue() {
		return m_nValue;
	}

	public void setNumericValue(int value) {
		this.m_nValue = value;
	}
	
	public long getLongValue() {
		return m_lValue;
	}

	public void setLongValue(long value) {
		this.m_lValue = value;
	}

	public String getStringValue() {
		return m_strValue;
	}

	public void setStringValue(String value) {
		this.m_strValue = value;
	}
	
	
}
