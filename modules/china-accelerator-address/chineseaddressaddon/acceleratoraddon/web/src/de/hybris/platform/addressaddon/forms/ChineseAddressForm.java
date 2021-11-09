/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressaddon.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 * Uses for submitting Chinese address information.
 */

public class ChineseAddressForm extends AddressForm
{
	public static final String ATTR_CELLPHONE = "cellphone";

	private String cityIso;
	private String districtIso;
	private String cellphone;
	private String fullname;

	@NotBlank(message = "{address.city.required}")
	public String getCityIso()
	{
		return cityIso;
	}

	public void setCityIso(final String cityIso)
	{
		this.cityIso = cityIso;
	}

	@NotBlank(message = "{address.district.required}")
	public String getDistrictIso()
	{
		return districtIso;
	}

	public void setDistrictIso(final String districtIso)
	{
		this.districtIso = districtIso;
	}

	@NotBlank(message = "{address.cellphone.invalid}")
	public String getCellphone()
	{
		return cellphone;
	}

	public void setCellphone(final String cellphone)
	{
		this.cellphone = cellphone;
	}


	@NotEmpty(message = "{address.fullname.required}")
	@Size(max = 255, message = "{address.maxlength}")
	public String getFullname()
	{
		return fullname;
	}

	public void setFullname(final String fullname)
	{
		this.fullname = fullname;
	}

}