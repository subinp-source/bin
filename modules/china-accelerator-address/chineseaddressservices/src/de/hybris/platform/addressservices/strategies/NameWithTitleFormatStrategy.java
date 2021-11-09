/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.strategies;

public interface NameWithTitleFormatStrategy
{
	/**
	 * get full name with title name
	 *
	 * @param fullname
	 * @param title
	 * @return full name with title name
	 */
	String getFullnameWithTitle(String fullname, String title);

	/**
	 * get full name with title name
	 *
	 * @param firstname
	 * @param lastname
	 * @param title
	 * @return full name with title name
	 */
	String getFullnameWithTitle(String firstname, String lastname, String title);

	/**
	 * get full name with title name
	 *
	 * @param fullname
	 * @param title
	 * @param isocode
	 * @return full name with title name
	 */
	String getFullnameWithTitleForISOCode(String fullname, String title, String isocode);

	/**
	 *
	 * @param firstname
	 * @param lastname
	 * @param title
	 * @param isocode
	 * @return full name with title name
	 */
	String getFullnameWithTitleForISOCode(String firstname, String lastname, String title, String isocode);
}
