///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///

export const LOG = false;

export function log(...params) {
	if (LOG) {
		console.log(...params);
	}
}

/**
 * Wrapper around Accelerator sanitizer
 * @param {string} string
 */
export function sanitize(string) {
	return ACC.sanitizer.sanitize(string);
}
