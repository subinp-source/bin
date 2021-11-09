/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesecommerceorgaddressaddon.forms;


import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This is chinese address form which adds four more fields(cityIso,districtIso,cellphone,fullname)
 */
public class ChineseUnitAddressForm extends AddressForm
{
	public static final String ATTR_CELLPHONE = "cellphone";

	private String cityIso;
	private String districtIso;
	private String cellphone;
	private String fullname;

	@NotNull(message = "{address.city.required}")
	public String getCityIso()
	{
		return cityIso;
	}

	public void setCityIso(final String cityIso)
	{
		this.cityIso = cityIso;
	}

	@NotNull(message = "{address.district.required}")
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

	@NotNull(message = "{address.fullname.required}")
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
