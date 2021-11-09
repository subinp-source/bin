package de.hybris.platform.odata2services.odata.persistence.hook

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class PrePersistHookExceptionUnitTest extends Specification {

	private static final String TEST_HOOK = "testHook"
	private static final String TEST_EXCEPTION_MESSAGE = "test exception message"
	private static final String TEST_INTEGRATION_KEY = "123|abc"

	@Test
	def "message contains hook name and cause when building a pre persist hook exception " () {
		when:
		def hookException = new PrePersistHookException(TEST_HOOK, new IllegalArgumentException(TEST_EXCEPTION_MESSAGE), TEST_INTEGRATION_KEY)
		then:
		hookException.getMessage().contains("Pre-Persist-Hook")
		hookException.getMessage().contains(TEST_HOOK)
		hookException.getMessage().contains(TEST_EXCEPTION_MESSAGE)
		hookException.getMessage().contains("IllegalArgumentException")
	}
}
