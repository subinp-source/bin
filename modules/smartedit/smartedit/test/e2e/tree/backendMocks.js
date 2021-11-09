/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('e2eBackendMocks', ['resourceLocationsModule', 'smarteditServicesModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/test\/e2e/)
    .run(function(httpBackendService, parseQuery) {
        var map = [
            {
                value: '"thepreviewTicketURI"',
                key: 'previewTicketURI'
            },
            {
                value: '{"smartEditContainerLocation":"/test/e2e/tree/outerapp.js"}',
                key: 'applications.outerapp'
            }
        ];

        httpBackendService.whenGET(/configuration$/).respond(function() {
            return [200, map];
        });

        httpBackendService.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
            languages: [
                {
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }
            ]
        });

        var nodes = [
            {
                uid: '1',
                name: 'node1',
                title: {
                    en: 'node1_en',
                    fr: 'node1_fr'
                },
                parentUid: 'root',
                hasChildren: true
            },
            {
                uid: '2',
                name: 'node2',
                title: {
                    en: 'node2_en',
                    fr: 'node2_fr'
                },
                parentUid: 'root',
                hasChildren: true
            },
            {
                uid: '4',
                name: 'node4',
                title: {
                    en: 'nodeA',
                    fr: 'nodeA'
                },
                parentUid: '1',
                hasChildren: false
            },
            {
                uid: '5',
                name: 'node5',
                title: {
                    en: 'nodeB',
                    fr: 'nodeB'
                },
                parentUid: '1',
                hasChildren: false
            },
            {
                uid: '3',
                name: 'node3',
                title: {
                    en: 'nodeF',
                    fr: 'nodeF'
                },
                parentUid: '1',
                hasChildren: false
            },
            {
                uid: '6',
                name: 'node6',
                title: {
                    en: 'nodeC',
                    fr: 'nodeC'
                },
                parentUid: '2',
                hasChildren: false
            }
        ];

        httpBackendService.whenGET(/someNodeURI/).respond(function(method, url, data) {
            var query = parseQuery(data);
            var parentUID = query.parentUid;

            return [
                200,
                {
                    navigationNodes: nodes.filter(function(node) {
                        return node.parentUid === parentUID;
                    })
                }
            ];
        });

        httpBackendService.whenPOST(/someNodeURI/).respond(function(method, url, data) {
            var payload = JSON.parse(data);
            var uid = new Date().getTime().toString();
            var node = {
                uid: uid,
                name: payload.name,
                parentUid: payload.parentUid,
                hasChildren: false
            };
            nodes.push(node);
            return [200, node];
        });

        httpBackendService.whenDELETE(/someNodeURI/).respond(function(method, url) {
            var uid = /someNodeURI\/(.*)/.exec(url)[1];
            nodes = nodes.filter(function(node) {
                return node.uid !== uid;
            });
            return [204];
        });

        httpBackendService.whenGET('/smarteditwebservices/v1/i18n/languages').respond({});

        httpBackendService.whenGET(/tree/).passThrough();
    });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
