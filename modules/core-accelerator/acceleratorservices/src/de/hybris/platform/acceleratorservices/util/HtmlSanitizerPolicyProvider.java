/**
 *
 */
package de.hybris.platform.acceleratorservices.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class HtmlSanitizerPolicyProvider
{
	protected static final PolicyFactory DEFAULT_POLICY = new HtmlPolicyBuilder()
			.allowElements("pre", "address", "em", "hr", "a")
			.allowAttributes("class").onElements("em")
			.allowAttributes("class", "target").onElements("a")
			.toFactory()
			.and(Sanitizers.BLOCKS)
			.and(Sanitizers.FORMATTING)
			.and(Sanitizers.LINKS)
			.and(Sanitizers.TABLES)
			.and(Sanitizers.STYLES);

  public static PolicyFactory defaultPolicy()
  {
	  return DEFAULT_POLICY;
  }
}
