/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.support.util

import org.apache.commons.lang3.ClassUtils

/**
 * Assertion utilities.
 */
class ObjectUtils {

	private ObjectUtils() {
		// utility class
	}

	/**
	 * Checks if an object is null or empty.
	 *
	 * @param obj
	 *  - the object to check
	 *
	 * @return <code>true</code> if the object is empty, <code>false</code> otherwise
	 */
	static final isEmpty(obj) {
		if (obj == null) {
			return true
		}

		def objMetaclass = obj.getMetaClass()

		if (objMetaclass.respondsTo(obj, 'isEmpty', null)) {
			return obj.isEmpty()
		}

		if (objMetaclass.respondsTo(obj, 'size', null)) {
			return obj.size() == 0
		}

		throw new UnsupportedOperationException()
	}

	/**
	 * Checks if an object is not null and not empty.
	 *
	 * @param obj
	 *  - the object to check
	 *
	 * @return <code>true</code> if the object is empty, <code>false</code> otherwise
	 */
	static final isNotEmpty(obj) {
		return !isEmpty(obj)
	}

	/**
	 * Checks if a container object contains the given object. The following rules are applied:
	 *	<ul>
	 *		<li>Collections are expected to be of the same size, elements are compared using the general rules of this method (called in a recursive way)</li>
	 *		<li>Maps can be a subset of the container map, keys should be equal but values are compared using the general rules of this method (called in a recursive way)</li>
	 *		<li>For all other object types, general object equality applies</li>
	 *  </ul>
	 *
	 * @param container
	 *  - the container object
	 * @param obj
	 *  - the object
	 *
	 * @return <code>true</code> if the container object contains the given object, <code>false</code> otherwise
	 */
	static final boolean matchContains(def container, def obj) {
		if (container == null && obj == null) {
			return true
		} else if (container == null || obj == null) {
			return false
		} else if (container instanceof Collection && obj instanceof Collection) {
			Comparator comparator = { a, b ->
				matchContains(a, b) ? 0 : -1
			} as Comparator
			return Arrays.equals(((Collection) container).toArray(), ((Collection) obj).toArray(), comparator)
		} else if (container instanceof Map && obj instanceof Map) {
			for (def key : obj.keySet()) {
				if (!matchContains(container.get(key), obj.get(key))) {
					return false
				}
			}

			return true
		} else if (!ClassUtils.isPrimitiveOrWrapper(container.getClass()) && obj instanceof Map) {
			def containerProperties = container.properties

			for (def key : obj.keySet()) {
				if (!matchContains(containerProperties.get(key), obj.get(key))) {
					return false
				}
			}

			return true
		} else {
			return container == obj.asType(container.class)
		}
	}
}
