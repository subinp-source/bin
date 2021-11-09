/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    stringUtils,
    ComponentAttributes,
    IContextualMenuButton,
    IContextualMenuConfiguration,
    PriorityService,
    SystemEventService
} from 'smarteditcommons';
import { ContextualMenu, ContextualMenuService } from 'smartedit/services';

describe('test contextualMenuService', () => {
    const regexpKeys: string[] = ['^((?!Slot).)*$'];
    const nameI18nKey: string = 'some.key';
    const componentAttributes = {} as ComponentAttributes;
    const element: JQuery<HTMLElement> = {} as JQuery<HTMLElement>;
    const slotId: string = 'slotId';
    const slotUuid: string = 'slotUuid';
    const isComponentHidden: boolean = false;

    const trueCondition = (configuration: IContextualMenuConfiguration) => {
        return true;
    };
    const falseCondition = (configuration: IContextualMenuConfiguration) => {
        return false;
    };
    const id1Condition = (configuration: IContextualMenuConfiguration) => {
        return configuration.componentId === 'ComponentId1';
    };
    const id2Condition = (configuration: IContextualMenuConfiguration) => {
        return configuration.componentId === 'ComponentId2';
    };
    const type1Condition = (configuration: IContextualMenuConfiguration) => {
        return configuration.componentType === 'ComponentType1';
    };

    let contextualMenuService: ContextualMenuService;
    let priorityService: jasmine.SpyObj<PriorityService>;
    let systemEventsService: jasmine.SpyObj<SystemEventService>;

    beforeEach(() => {
        priorityService = jasmine.createSpyObj('priorityService', ['sort']);
        priorityService.sort.and.callFake((arr: any[]) => {
            return arr.sort((item1: any, item2: any) => {
                let output: number = item1.priority - item2.priority;
                if (output === 0) {
                    output = stringUtils.encode(item1).localeCompare(stringUtils.encode(item2));
                }
                return output;
            });
        });

        systemEventsService = jasmine.createSpyObj('systemEventsService', ['publishAsync']);

        contextualMenuService = new ContextualMenuService(priorityService, systemEventsService);
    });

    function getItem(key: string, priority?: number): IContextualMenuButton {
        return {
            key,
            action: {
                template: 'dummyTemplate string'
            },
            priority
        } as IContextualMenuButton;
    }

    it('addItems WILL throw an error when item doesnt contain a valid key', () => {
        expect(() => {
            contextualMenuService.addItems({
                type1: [
                    {
                        action: {
                            template: 'dummyTemplate string'
                        },
                        key: null,
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        action: {
                            template: 'dummyTemplate string'
                        },
                        key: 'contextualMenuItem2',
                        regexpKeys,
                        nameI18nKey
                    }
                ]
            });
        }).toThrow(new Error("addItems() - Cannot add items. Error: Item doesn't have key."));
    });

    it('onContextualMenuItemsAdded.next will be called with component type', () => {
        const spy = spyOn(contextualMenuService.onContextualMenuItemsAdded, 'next');

        contextualMenuService.addItems({
            Type: [getItem('element5'), getItem('element6')]
        });

        expect(spy).toHaveBeenCalledWith('Type');
    });

    it('getContextualMenuByType will return an unique array of contextual menu items when it matches the regexps', () => {
        contextualMenuService.addItems({
            '*Suffix': [getItem('element1'), getItem('element2')],
            '.*Suffix': [getItem('element2'), getItem('element3')],
            TypeSuffix: [getItem('element3'), getItem('element4')],
            '^((?!Middle).)*$': [getItem('element4'), getItem('element5')],
            PrefixType: [getItem('element5'), getItem('element6')]
        });

        expect(contextualMenuService.getContextualMenuByType('TypeSuffix')).toEqual([
            getItem('element1'),
            getItem('element2'),
            getItem('element3'),
            getItem('element4'),
            getItem('element5')
        ]);

        expect(contextualMenuService.getContextualMenuByType('TypeSuffixes')).toEqual([
            getItem('element2'),
            getItem('element3'),
            getItem('element4'),
            getItem('element5')
        ]);

        expect(contextualMenuService.getContextualMenuByType('MiddleTypeSuffix')).toEqual([
            getItem('element1'),
            getItem('element2'),
            getItem('element3')
        ]);
    });

    describe('getContextualMenuItems will return an array-of-array of contextual menu items based on condition', () => {
        it('will return those menu items which satisfy the condition or those that have no condition set (default condition to be true)', () => {
            // GIVEN
            contextualMenuService.addItems({
                ComponentType1: [
                    {
                        key: 'key1',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON1',
                        iconIdle: 'icon1.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key2',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON2',
                        condition: id2Condition,
                        iconIdle: 'icon2.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key3',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON3',
                        condition: trueCondition,
                        iconIdle: 'icon3.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key4',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON4',
                        condition: falseCondition,
                        iconIdle: 'icon4.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key5',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON5',
                        condition: id1Condition,
                        iconIdle: 'icon5.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key6',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON6',
                        condition: type1Condition,
                        iconIdle: 'icon6.png',
                        regexpKeys,
                        nameI18nKey
                    }
                ]
            });

            // WHEN
            const promise: Promise<ContextualMenu> = contextualMenuService.getContextualMenuItems({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                iLeftBtns: 3,
                element,
                componentAttributes,
                slotId,
                slotUuid,
                isComponentHidden
            });

            promise.then((result) => {
                // THEN
                expect(result).toEqual({
                    leftMenuItems: [
                        {
                            key: 'key1',
                            action: {
                                template: 'dummy template string'
                            },
                            i18nKey: 'ICON1',
                            iconIdle: 'icon1.png',
                            regexpKeys,
                            nameI18nKey
                        },
                        {
                            key: 'key3',
                            action: {
                                template: 'dummy template string'
                            },
                            i18nKey: 'ICON3',
                            condition: trueCondition,
                            iconIdle: 'icon3.png',
                            regexpKeys,
                            nameI18nKey
                        },
                        {
                            key: 'key5',
                            action: {
                                template: 'dummy template string'
                            },
                            i18nKey: 'ICON5',
                            condition: id1Condition,
                            iconIdle: 'icon5.png',
                            regexpKeys,
                            nameI18nKey
                        }
                    ],
                    moreMenuItems: [
                        {
                            key: 'key6',
                            action: {
                                template: 'dummy template string'
                            },
                            i18nKey: 'ICON6',
                            condition: type1Condition,
                            iconIdle: 'icon6.png',
                            regexpKeys,
                            nameI18nKey
                        }
                    ]
                } as ContextualMenu);
            });
        });

        it('GIVEN all menu items have a priority WHEN called THEN it will return the buttons in the right priority', () => {
            // GIVEN
            contextualMenuService.addItems({
                ComponentType1: [
                    {
                        key: 'key1',
                        action: {
                            template: 'dummy template string'
                        },
                        priority: 300,
                        i18nKey: 'ICON1',
                        iconIdle: 'icon1.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key2',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON2',
                        condition: trueCondition,
                        priority: 400,
                        iconIdle: 'icon2.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key3',
                        action: {
                            template: 'dummy template string'
                        },
                        priority: 1,
                        i18nKey: 'ICON3',
                        condition: trueCondition,
                        iconIdle: 'icon3.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key4',
                        action: {
                            template: 'dummy template string'
                        },
                        priority: 500,
                        i18nKey: 'ICON4',
                        iconIdle: 'icon4.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key5',
                        action: {
                            template: 'dummy template string'
                        },
                        priority: 100,
                        i18nKey: 'ICON5',
                        iconIdle: 'icon5.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key6',
                        action: {
                            template: 'dummy template string'
                        },
                        priority: 200,
                        i18nKey: 'ICON6',
                        iconIdle: 'icon6.png',
                        regexpKeys,
                        nameI18nKey
                    }
                ]
            });

            // WHEN
            const promise: Promise<ContextualMenu> = contextualMenuService.getContextualMenuItems({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                iLeftBtns: 3,
                element,
                componentAttributes,
                slotId,
                slotUuid,
                isComponentHidden
            });

            // THEN
            promise.then((result) => {
                // THEN
                expect(result).toEqual({
                    leftMenuItems: [
                        {
                            key: 'key3',
                            action: {
                                template: 'dummy template string'
                            },
                            priority: 1,
                            i18nKey: 'ICON3',
                            condition: trueCondition,
                            iconIdle: 'icon3.png',
                            regexpKeys,
                            nameI18nKey
                        },
                        {
                            key: 'key5',
                            action: {
                                template: 'dummy template string'
                            },
                            priority: 100,
                            i18nKey: 'ICON5',
                            iconIdle: 'icon5.png',
                            regexpKeys,
                            nameI18nKey
                        },
                        {
                            key: 'key6',
                            action: {
                                template: 'dummy template string'
                            },
                            priority: 200,
                            i18nKey: 'ICON6',
                            iconIdle: 'icon6.png',
                            regexpKeys,
                            nameI18nKey
                        }
                    ],
                    moreMenuItems: [
                        {
                            key: 'key1',
                            action: {
                                template: 'dummy template string'
                            },
                            priority: 300,
                            i18nKey: 'ICON1',
                            iconIdle: 'icon1.png',
                            regexpKeys,
                            nameI18nKey
                        },
                        {
                            key: 'key2',
                            action: {
                                template: 'dummy template string'
                            },
                            i18nKey: 'ICON2',
                            condition: trueCondition,
                            priority: 400,
                            iconIdle: 'icon2.png',
                            regexpKeys,
                            nameI18nKey
                        },
                        {
                            key: 'key4',
                            action: {
                                template: 'dummy template string'
                            },
                            priority: 500,
                            i18nKey: 'ICON4',
                            iconIdle: 'icon4.png',
                            regexpKeys,
                            nameI18nKey
                        }
                    ]
                } as ContextualMenu);
            });
        });

        it('for iLeftBtns= 3, will set a maximum of 3 menu items to the left (1st element in the array) and the rest to the right (2nd element in the array)', () => {
            // GIVEN
            contextualMenuService.addItems({
                ComponentType1: [
                    {
                        key: 'key1',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON1',
                        iconIdle: 'icon1.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key2',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON2',
                        condition: id2Condition,
                        iconIdle: 'icon2.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key3',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON3',
                        condition: trueCondition,
                        iconIdle: 'icon3.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key4',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON4',
                        condition: falseCondition,
                        iconIdle: 'icon4.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key5',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON5',
                        condition: id1Condition,
                        iconIdle: 'icon5.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key6',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON6',
                        condition: type1Condition,
                        iconIdle: 'icon6.png',
                        regexpKeys,
                        nameI18nKey
                    }
                ]
            });

            // WHEN
            const promise: Promise<ContextualMenu> = contextualMenuService.getContextualMenuItems({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                iLeftBtns: 3,
                element,
                componentAttributes,
                slotId,
                slotUuid,
                isComponentHidden
            });

            promise.then((result) => {
                // THEN
                expect(result.leftMenuItems).toEqual([
                    {
                        key: 'key1',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON1',
                        iconIdle: 'icon1.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key3',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON3',
                        condition: trueCondition,
                        iconIdle: 'icon3.png',
                        regexpKeys,
                        nameI18nKey
                    },
                    {
                        key: 'key5',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON5',
                        condition: id1Condition,
                        iconIdle: 'icon5.png',
                        regexpKeys,
                        nameI18nKey
                    }
                ]);

                // THEN
                expect(result.moreMenuItems).toEqual([
                    {
                        key: 'key6',
                        action: {
                            template: 'dummy template string'
                        },
                        i18nKey: 'ICON6',
                        condition: type1Condition,
                        iconIdle: 'icon6.png',
                        regexpKeys,
                        nameI18nKey
                    }
                ]);
            });
        });
    });

    it('getContextualMenuItems will provide the dom element', () => {
        // GIVEN
        const contextualItemMock = jasmine.createSpyObj('contextualItemMock', ['condition']);
        contextualItemMock.key = 'key1';
        contextualItemMock.i18nKey = 'ICON1';
        contextualItemMock.condition.and.returnValue(true);
        contextualItemMock.icon = 'icon1.png';
        contextualItemMock.action = {
            template: 'dummy template string'
        };

        const obj = {
            ComponentType1: [contextualItemMock]
        };
        contextualMenuService.addItems(obj);

        // WHEN
        const config = {
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            iLeftBtns: 1,
            element,
            componentAttributes,
            slotId,
            slotUuid,
            isComponentHidden
        };
        const promise: Promise<ContextualMenu> = contextualMenuService.getContextualMenuItems(
            config
        );

        promise.then((result) => {
            // THEN
            expect(result.leftMenuItems).toEqual(obj.ComponentType1);

            // THEN
            expect(contextualItemMock.condition).toHaveBeenCalledWith(config);
        });
    });

    it('removeItemByKey will remove all the items with the provided key when the condition is called', () => {
        // GIVEN
        contextualMenuService.addItems({
            ComponentType1: [
                {
                    key: 'key1',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON1',
                    iconIdle: 'icon1.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key2',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON2',
                    iconIdle: 'icon2.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key3',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON3',
                    iconIdle: 'icon3.png',
                    regexpKeys,
                    nameI18nKey
                }
            ],
            ComponentType2: [
                {
                    key: 'key1',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON1',
                    iconIdle: 'icon1.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key2',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON2',
                    iconIdle: 'icon2.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key5',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON5',
                    iconIdle: 'icon5.png',
                    regexpKeys,
                    nameI18nKey
                }
            ]
        });

        // WHEN
        contextualMenuService.removeItemByKey('key2');

        const common = {
            iLeftBtns: 3,
            element,
            componentAttributes,
            slotId,
            slotUuid,
            isComponentHidden
        };

        const promiseCmp1: Promise<ContextualMenu> = contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            ...common
        });

        const promiseCmp2 = contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId2',
            componentType: 'ComponentType2',
            ...common
        });

        Promise.all([promiseCmp1, promiseCmp2]).then(([result0, result1]) => {
            // THEN

            expect(result0.leftMenuItems).toEqual([
                {
                    key: 'key1',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON1',
                    iconIdle: 'icon1.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key3',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON3',
                    iconIdle: 'icon3.png',
                    regexpKeys,
                    nameI18nKey
                }
            ]);

            // THEN
            expect(result1.leftMenuItems).toEqual([
                {
                    key: 'key1',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON1',
                    iconIdle: 'icon1.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key5',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON5',
                    iconIdle: 'icon5.png',
                    regexpKeys,
                    nameI18nKey
                }
            ]);
        });
    });

    it('removeItemByKey will not do anything when the provided key does not match an item', () => {
        // GIVEN
        contextualMenuService.addItems({
            ComponentType1: [
                {
                    key: 'key1',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON1',
                    iconIdle: 'icon1.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key2',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON2',
                    iconIdle: 'icon2.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key3',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON3',
                    iconIdle: 'icon3.png',
                    regexpKeys,
                    nameI18nKey
                }
            ],
            ComponentType2: [
                {
                    key: 'key1',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON1',
                    iconIdle: 'icon1.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key2',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON2',
                    iconIdle: 'icon2.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key5',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON5',
                    iconIdle: 'icon5.png',
                    regexpKeys,
                    nameI18nKey
                }
            ]
        });
        contextualMenuService.removeItemByKey('key10');

        // WHEN

        const common = {
            iLeftBtns: 3,
            element,
            componentAttributes,
            slotId,
            slotUuid,
            isComponentHidden
        };

        const promise0: Promise<ContextualMenu> = contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            ...common
        });

        const promise1: Promise<ContextualMenu> = contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId2',
            componentType: 'ComponentType2',
            ...common
        });

        Promise.all([promise0, promise1]).then(([result0, result1]) => {
            // THEN
            expect(result0.leftMenuItems).toEqual([
                {
                    key: 'key1',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON1',
                    iconIdle: 'icon1.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key2',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON2',
                    iconIdle: 'icon2.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key3',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON3',
                    iconIdle: 'icon3.png',
                    regexpKeys,
                    nameI18nKey
                }
            ]);

            // THEN
            expect(result1.leftMenuItems).toEqual([
                {
                    key: 'key1',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON1',
                    iconIdle: 'icon1.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key2',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON2',
                    iconIdle: 'icon2.png',
                    regexpKeys,
                    nameI18nKey
                },
                {
                    key: 'key5',
                    action: {
                        template: 'dummy template string'
                    },
                    i18nKey: 'ICON5',
                    iconIdle: 'icon5.png',
                    regexpKeys,
                    nameI18nKey
                }
            ]);
        });
    });
});
