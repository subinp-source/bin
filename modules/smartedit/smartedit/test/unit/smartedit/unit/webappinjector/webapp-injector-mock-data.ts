/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// !!!CAUTION!!! caution when modifying: these kind of go hand in hand with allowedOriginURLs below
export const allowedConfigs = [
    '127.0.0.1:80',
    `localhost:${location.port}`,
    '*.some.domain:9000',
    'some.domain:900',
    'some.domain:80',
    '*.my-shop.cx:80',
    '*-hybris.my-site.io:443',
    '*.hybirs.my-site.net:443'
];

// !!!CAUTION!!! caution when modifying: these kind of go hand in hand with disallowedOriginURLs below
export const disallowedConfigs = [
    '*',
    'foo.*',
    '127.0.0.1',
    'foo.com*',
    'foo.n*t',
    'bar.foo.o*g',
    '*.com',
    'f*o.net',
    'bar.*.org',
    'bar.*foo.io',
    '.',
    'f*o.c*m',
    'bar..',
    'foo.bar.*',
    '*:9000',
    '*.com:9000',
    'bar.*.com:9000',
    '*-hybris.my-site.io', // no port
    '*.hybirs.my-site.net' // no port
];

// !!!CAUTION!!! caution when modifying: these kind of go hand in hand with allowedConfigs above
export const allowedOriginURLs: string[] = [
    'http://127.0.0.1:80',
    `http://localhost:${location.port}`,
    'http://hmm.some.domain:9000',
    'http://some.domain:900',
    'http://some.domain:80',
    'http://subdomain.my-shop.cx:80',
    'https://prefix-hybris.my-site.io:443',
    'https://subdomain.hybirs.my-site.net:443'
];

// !!!CAUTION!!! caution when modifying: these kind of go hand in hand with disallowedConfigs above
export const disallowedOriginURLs: any[] = [
    null,
    undefined,
    42,
    { object: 'foo' },
    ['array'],
    'null',
    'file://',
    'file:',
    'javascript://',
    'data:',
    'http://127.0.0.2:9876',
    'http://*.some.*:9000',
    'http://fewafwa.some.domain:900',
    'http://some.domainn',
    'http://fewaea...my-shop.cx',
    'http://subsubdomain.subdomain.hybirs.my-site.net'
];
