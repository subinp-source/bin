/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.odata2webservicesfeaturetests.constants.Odata2webservicesfeaturetestsConstants;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.test.TestItem TestIntegrationItem2}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedTestIntegrationItem2 extends TestItem
{
	/** Qualifier of the <code>TestIntegrationItem2.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>TestIntegrationItem2.requiredName</code> attribute **/
	public static final String REQUIREDNAME = "requiredName";
	/** Qualifier of the <code>TestIntegrationItem2.optionalSimpleAttr</code> attribute **/
	public static final String OPTIONALSIMPLEATTR = "optionalSimpleAttr";
	/** Qualifier of the <code>TestIntegrationItem2.requiredStringMap</code> attribute **/
	public static final String REQUIREDSTRINGMAP = "requiredStringMap";
	/** Qualifier of the <code>TestIntegrationItem2.bigDecimalMap</code> attribute **/
	public static final String BIGDECIMALMAP = "bigDecimalMap";
	/** Qualifier of the <code>TestIntegrationItem2.bigIntegerMap</code> attribute **/
	public static final String BIGINTEGERMAP = "bigIntegerMap";
	/** Qualifier of the <code>TestIntegrationItem2.booleanMap</code> attribute **/
	public static final String BOOLEANMAP = "booleanMap";
	/** Qualifier of the <code>TestIntegrationItem2.byteMap</code> attribute **/
	public static final String BYTEMAP = "byteMap";
	/** Qualifier of the <code>TestIntegrationItem2.characterMap</code> attribute **/
	public static final String CHARACTERMAP = "characterMap";
	/** Qualifier of the <code>TestIntegrationItem2.dateMap</code> attribute **/
	public static final String DATEMAP = "dateMap";
	/** Qualifier of the <code>TestIntegrationItem2.doubleMap</code> attribute **/
	public static final String DOUBLEMAP = "doubleMap";
	/** Qualifier of the <code>TestIntegrationItem2.floatMap</code> attribute **/
	public static final String FLOATMAP = "floatMap";
	/** Qualifier of the <code>TestIntegrationItem2.integerMap</code> attribute **/
	public static final String INTEGERMAP = "integerMap";
	/** Qualifier of the <code>TestIntegrationItem2.longMap</code> attribute **/
	public static final String LONGMAP = "longMap";
	/** Qualifier of the <code>TestIntegrationItem2.shortMap</code> attribute **/
	public static final String SHORTMAP = "shortMap";
	/** Qualifier of the <code>TestIntegrationItem2.bigDecimal2StringMap</code> attribute **/
	public static final String BIGDECIMAL2STRINGMAP = "bigDecimal2StringMap";
	/** Qualifier of the <code>TestIntegrationItem2.character2DateMap</code> attribute **/
	public static final String CHARACTER2DATEMAP = "character2DateMap";
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.bigDecimal2StringMap</code> attribute.
	 * @return the bigDecimal2StringMap
	 */
	public Map<BigDecimal,String> getAllBigDecimal2StringMap(final SessionContext ctx)
	{
		Map<BigDecimal,String> map = (Map<BigDecimal,String>)getProperty( ctx, BIGDECIMAL2STRINGMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.bigDecimal2StringMap</code> attribute.
	 * @return the bigDecimal2StringMap
	 */
	public Map<BigDecimal,String> getAllBigDecimal2StringMap()
	{
		return getAllBigDecimal2StringMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.bigDecimal2StringMap</code> attribute. 
	 * @param value the bigDecimal2StringMap
	 */
	public void setAllBigDecimal2StringMap(final SessionContext ctx, final Map<BigDecimal,String> value)
	{
		setProperty(ctx, BIGDECIMAL2STRINGMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.bigDecimal2StringMap</code> attribute. 
	 * @param value the bigDecimal2StringMap
	 */
	public void setAllBigDecimal2StringMap(final Map<BigDecimal,String> value)
	{
		setAllBigDecimal2StringMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.bigDecimalMap</code> attribute.
	 * @return the bigDecimalMap
	 */
	public Map<BigDecimal,BigDecimal> getAllBigDecimalMap(final SessionContext ctx)
	{
		Map<BigDecimal,BigDecimal> map = (Map<BigDecimal,BigDecimal>)getProperty( ctx, BIGDECIMALMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.bigDecimalMap</code> attribute.
	 * @return the bigDecimalMap
	 */
	public Map<BigDecimal,BigDecimal> getAllBigDecimalMap()
	{
		return getAllBigDecimalMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.bigDecimalMap</code> attribute. 
	 * @param value the bigDecimalMap
	 */
	public void setAllBigDecimalMap(final SessionContext ctx, final Map<BigDecimal,BigDecimal> value)
	{
		setProperty(ctx, BIGDECIMALMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.bigDecimalMap</code> attribute. 
	 * @param value the bigDecimalMap
	 */
	public void setAllBigDecimalMap(final Map<BigDecimal,BigDecimal> value)
	{
		setAllBigDecimalMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.bigIntegerMap</code> attribute.
	 * @return the bigIntegerMap
	 */
	public Map<BigInteger,BigInteger> getAllBigIntegerMap(final SessionContext ctx)
	{
		Map<BigInteger,BigInteger> map = (Map<BigInteger,BigInteger>)getProperty( ctx, BIGINTEGERMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.bigIntegerMap</code> attribute.
	 * @return the bigIntegerMap
	 */
	public Map<BigInteger,BigInteger> getAllBigIntegerMap()
	{
		return getAllBigIntegerMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.bigIntegerMap</code> attribute. 
	 * @param value the bigIntegerMap
	 */
	public void setAllBigIntegerMap(final SessionContext ctx, final Map<BigInteger,BigInteger> value)
	{
		setProperty(ctx, BIGINTEGERMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.bigIntegerMap</code> attribute. 
	 * @param value the bigIntegerMap
	 */
	public void setAllBigIntegerMap(final Map<BigInteger,BigInteger> value)
	{
		setAllBigIntegerMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.booleanMap</code> attribute.
	 * @return the booleanMap
	 */
	public Map<Boolean,Boolean> getAllBooleanMap(final SessionContext ctx)
	{
		Map<Boolean,Boolean> map = (Map<Boolean,Boolean>)getProperty( ctx, BOOLEANMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.booleanMap</code> attribute.
	 * @return the booleanMap
	 */
	public Map<Boolean,Boolean> getAllBooleanMap()
	{
		return getAllBooleanMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.booleanMap</code> attribute. 
	 * @param value the booleanMap
	 */
	public void setAllBooleanMap(final SessionContext ctx, final Map<Boolean,Boolean> value)
	{
		setProperty(ctx, BOOLEANMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.booleanMap</code> attribute. 
	 * @param value the booleanMap
	 */
	public void setAllBooleanMap(final Map<Boolean,Boolean> value)
	{
		setAllBooleanMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.byteMap</code> attribute.
	 * @return the byteMap
	 */
	public Map<Byte,Byte> getAllByteMap(final SessionContext ctx)
	{
		Map<Byte,Byte> map = (Map<Byte,Byte>)getProperty( ctx, BYTEMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.byteMap</code> attribute.
	 * @return the byteMap
	 */
	public Map<Byte,Byte> getAllByteMap()
	{
		return getAllByteMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.byteMap</code> attribute. 
	 * @param value the byteMap
	 */
	public void setAllByteMap(final SessionContext ctx, final Map<Byte,Byte> value)
	{
		setProperty(ctx, BYTEMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.byteMap</code> attribute. 
	 * @param value the byteMap
	 */
	public void setAllByteMap(final Map<Byte,Byte> value)
	{
		setAllByteMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.character2DateMap</code> attribute.
	 * @return the character2DateMap
	 */
	public Map<Character,Date> getAllCharacter2DateMap(final SessionContext ctx)
	{
		Map<Character,Date> map = (Map<Character,Date>)getProperty( ctx, CHARACTER2DATEMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.character2DateMap</code> attribute.
	 * @return the character2DateMap
	 */
	public Map<Character,Date> getAllCharacter2DateMap()
	{
		return getAllCharacter2DateMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.character2DateMap</code> attribute. 
	 * @param value the character2DateMap
	 */
	public void setAllCharacter2DateMap(final SessionContext ctx, final Map<Character,Date> value)
	{
		setProperty(ctx, CHARACTER2DATEMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.character2DateMap</code> attribute. 
	 * @param value the character2DateMap
	 */
	public void setAllCharacter2DateMap(final Map<Character,Date> value)
	{
		setAllCharacter2DateMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.characterMap</code> attribute.
	 * @return the characterMap
	 */
	public Map<Character,Character> getAllCharacterMap(final SessionContext ctx)
	{
		Map<Character,Character> map = (Map<Character,Character>)getProperty( ctx, CHARACTERMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.characterMap</code> attribute.
	 * @return the characterMap
	 */
	public Map<Character,Character> getAllCharacterMap()
	{
		return getAllCharacterMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.characterMap</code> attribute. 
	 * @param value the characterMap
	 */
	public void setAllCharacterMap(final SessionContext ctx, final Map<Character,Character> value)
	{
		setProperty(ctx, CHARACTERMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.characterMap</code> attribute. 
	 * @param value the characterMap
	 */
	public void setAllCharacterMap(final Map<Character,Character> value)
	{
		setAllCharacterMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.code</code> attribute.
	 * @return the code - Unique identifier of this test item.
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.code</code> attribute.
	 * @return the code - Unique identifier of this test item.
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.code</code> attribute. 
	 * @param value the code - Unique identifier of this test item.
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.code</code> attribute. 
	 * @param value the code - Unique identifier of this test item.
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.dateMap</code> attribute.
	 * @return the dateMap
	 */
	public Map<Date,Date> getAllDateMap(final SessionContext ctx)
	{
		Map<Date,Date> map = (Map<Date,Date>)getProperty( ctx, DATEMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.dateMap</code> attribute.
	 * @return the dateMap
	 */
	public Map<Date,Date> getAllDateMap()
	{
		return getAllDateMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.dateMap</code> attribute. 
	 * @param value the dateMap
	 */
	public void setAllDateMap(final SessionContext ctx, final Map<Date,Date> value)
	{
		setProperty(ctx, DATEMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.dateMap</code> attribute. 
	 * @param value the dateMap
	 */
	public void setAllDateMap(final Map<Date,Date> value)
	{
		setAllDateMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.doubleMap</code> attribute.
	 * @return the doubleMap
	 */
	public Map<Double,Double> getAllDoubleMap(final SessionContext ctx)
	{
		Map<Double,Double> map = (Map<Double,Double>)getProperty( ctx, DOUBLEMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.doubleMap</code> attribute.
	 * @return the doubleMap
	 */
	public Map<Double,Double> getAllDoubleMap()
	{
		return getAllDoubleMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.doubleMap</code> attribute. 
	 * @param value the doubleMap
	 */
	public void setAllDoubleMap(final SessionContext ctx, final Map<Double,Double> value)
	{
		setProperty(ctx, DOUBLEMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.doubleMap</code> attribute. 
	 * @param value the doubleMap
	 */
	public void setAllDoubleMap(final Map<Double,Double> value)
	{
		setAllDoubleMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.floatMap</code> attribute.
	 * @return the floatMap
	 */
	public Map<Float,Float> getAllFloatMap(final SessionContext ctx)
	{
		Map<Float,Float> map = (Map<Float,Float>)getProperty( ctx, FLOATMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.floatMap</code> attribute.
	 * @return the floatMap
	 */
	public Map<Float,Float> getAllFloatMap()
	{
		return getAllFloatMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.floatMap</code> attribute. 
	 * @param value the floatMap
	 */
	public void setAllFloatMap(final SessionContext ctx, final Map<Float,Float> value)
	{
		setProperty(ctx, FLOATMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.floatMap</code> attribute. 
	 * @param value the floatMap
	 */
	public void setAllFloatMap(final Map<Float,Float> value)
	{
		setAllFloatMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.integerMap</code> attribute.
	 * @return the integerMap
	 */
	public Map<Integer,Integer> getAllIntegerMap(final SessionContext ctx)
	{
		Map<Integer,Integer> map = (Map<Integer,Integer>)getProperty( ctx, INTEGERMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.integerMap</code> attribute.
	 * @return the integerMap
	 */
	public Map<Integer,Integer> getAllIntegerMap()
	{
		return getAllIntegerMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.integerMap</code> attribute. 
	 * @param value the integerMap
	 */
	public void setAllIntegerMap(final SessionContext ctx, final Map<Integer,Integer> value)
	{
		setProperty(ctx, INTEGERMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.integerMap</code> attribute. 
	 * @param value the integerMap
	 */
	public void setAllIntegerMap(final Map<Integer,Integer> value)
	{
		setAllIntegerMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.longMap</code> attribute.
	 * @return the longMap
	 */
	public Map<Long,Long> getAllLongMap(final SessionContext ctx)
	{
		Map<Long,Long> map = (Map<Long,Long>)getProperty( ctx, LONGMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.longMap</code> attribute.
	 * @return the longMap
	 */
	public Map<Long,Long> getAllLongMap()
	{
		return getAllLongMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.longMap</code> attribute. 
	 * @param value the longMap
	 */
	public void setAllLongMap(final SessionContext ctx, final Map<Long,Long> value)
	{
		setProperty(ctx, LONGMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.longMap</code> attribute. 
	 * @param value the longMap
	 */
	public void setAllLongMap(final Map<Long,Long> value)
	{
		setAllLongMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.optionalSimpleAttr</code> attribute.
	 * @return the optionalSimpleAttr
	 */
	public String getOptionalSimpleAttr(final SessionContext ctx)
	{
		return (String)getProperty( ctx, OPTIONALSIMPLEATTR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.optionalSimpleAttr</code> attribute.
	 * @return the optionalSimpleAttr
	 */
	public String getOptionalSimpleAttr()
	{
		return getOptionalSimpleAttr( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.optionalSimpleAttr</code> attribute. 
	 * @param value the optionalSimpleAttr
	 */
	public void setOptionalSimpleAttr(final SessionContext ctx, final String value)
	{
		setProperty(ctx, OPTIONALSIMPLEATTR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.optionalSimpleAttr</code> attribute. 
	 * @param value the optionalSimpleAttr
	 */
	public void setOptionalSimpleAttr(final String value)
	{
		setOptionalSimpleAttr( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.requiredName</code> attribute.
	 * @return the requiredName
	 */
	public String getRequiredName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, REQUIREDNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.requiredName</code> attribute.
	 * @return the requiredName
	 */
	public String getRequiredName()
	{
		return getRequiredName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.requiredName</code> attribute. 
	 * @param value the requiredName
	 */
	public void setRequiredName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, REQUIREDNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.requiredName</code> attribute. 
	 * @param value the requiredName
	 */
	public void setRequiredName(final String value)
	{
		setRequiredName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.requiredStringMap</code> attribute.
	 * @return the requiredStringMap
	 */
	public Map<String,String> getAllRequiredStringMap(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, REQUIREDSTRINGMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.requiredStringMap</code> attribute.
	 * @return the requiredStringMap
	 */
	public Map<String,String> getAllRequiredStringMap()
	{
		return getAllRequiredStringMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.requiredStringMap</code> attribute. 
	 * @param value the requiredStringMap
	 */
	public void setAllRequiredStringMap(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, REQUIREDSTRINGMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.requiredStringMap</code> attribute. 
	 * @param value the requiredStringMap
	 */
	public void setAllRequiredStringMap(final Map<String,String> value)
	{
		setAllRequiredStringMap( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.shortMap</code> attribute.
	 * @return the shortMap
	 */
	public Map<Short,Short> getAllShortMap(final SessionContext ctx)
	{
		Map<Short,Short> map = (Map<Short,Short>)getProperty( ctx, SHORTMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem2.shortMap</code> attribute.
	 * @return the shortMap
	 */
	public Map<Short,Short> getAllShortMap()
	{
		return getAllShortMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.shortMap</code> attribute. 
	 * @param value the shortMap
	 */
	public void setAllShortMap(final SessionContext ctx, final Map<Short,Short> value)
	{
		setProperty(ctx, SHORTMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem2.shortMap</code> attribute. 
	 * @param value the shortMap
	 */
	public void setAllShortMap(final Map<Short,Short> value)
	{
		setAllShortMap( getSession().getSessionContext(), value );
	}
	
}
