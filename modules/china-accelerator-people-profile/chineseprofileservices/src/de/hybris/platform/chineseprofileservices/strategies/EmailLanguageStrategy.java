/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.strategies;

 /**
  * The strategy to get default email language
  */
public interface EmailLanguageStrategy
{
	/**
	 * Gets default email language.
	 * 
	 * @return default email language
	 */
	String getDefaultEmailLanguage();

}
