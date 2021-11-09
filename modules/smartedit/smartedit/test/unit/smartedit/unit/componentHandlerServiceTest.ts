/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { ComponentHandlerService } from 'smartedit/services';

describe('componentHandlerService - inner', () => {
    const yjQueryObject = {} as any;
    let componentHandlerService: ComponentHandlerService;
    let yjQuery: any;

    beforeEach(() => {
        yjQuery = jasmine.createSpy('yjQuery');
        yjQuery.and.callFake(function(arg: any) {
            return yjQueryObject;
        });

        const isBlank = jasmine.createSpy('isBlank');
        isBlank.and.callFake(function(value: string) {
            return (
                typeof value === 'undefined' ||
                value === null ||
                value === 'null' ||
                value.toString().trim().length === 0
            );
        });

        componentHandlerService = new ComponentHandlerService(yjQuery);
    });

    it('getOverlay will get a yjQuery reference on the overlay by id', () => {
        expect(componentHandlerService.getOverlay()).toBe(yjQueryObject);
        expect(yjQuery).toHaveBeenCalledWith('#smarteditoverlay');
    });

    it('getComponent will get a yjQuery reference on an object containing the given class and having the given id and type', () => {
        expect(componentHandlerService.getComponent('theid', 'thetype', 'myclass')).toBe(
            yjQueryObject
        );
        expect(yjQuery).toHaveBeenCalledWith(
            ".myclass[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']"
        );
    });

    it('getComponentUnderSlot will get a yjQuery reference on an object containing the given class and having the given id and type', () => {
        expect(
            componentHandlerService.getComponentUnderSlot(
                'theComponentId',
                'thetype',
                'theSlotId',
                'myclass'
            )
        ).toBe(yjQueryObject);
        expect(yjQuery).toHaveBeenCalledWith(
            "[data-smartedit-component-id='theSlotId'][data-smartedit-component-type='ContentSlot'] .myclass[data-smartedit-component-id='theComponentId'][data-smartedit-component-type='thetype']"
        );
    });

    it('getOriginalComponent will get a yjQuery reference on an object containing the smartEditComponent class and having the given id and type', () => {
        expect(componentHandlerService.getOriginalComponent('theid', 'thetype')).toBe(
            yjQueryObject
        );
        expect(yjQuery).toHaveBeenCalledWith(
            ".smartEditComponent[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']"
        );
    });

    it('getOriginalComponentWithinSlot will get a yjQuery reference on an object containing the smartEditComponent class and having the given id and type within a given slot ID', () => {
        expect(
            componentHandlerService.getOriginalComponentWithinSlot('theid', 'thetype', 'theSlotId')
        ).toBe(yjQueryObject);
        expect(yjQuery).toHaveBeenCalledWith(
            "[data-smartedit-component-id='theSlotId'][data-smartedit-component-type='ContentSlot'] .smartEditComponent[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']"
        );
    });

    it('getOverlayComponentWithinSlot will get a yjQuery reference on an object containing the smartEditComponent class and having the given id and type within a given slot ID', () => {
        expect(
            componentHandlerService.getOverlayComponentWithinSlot('theid', 'thetype', 'theSlotId')
        ).toBe(yjQueryObject);
        expect(yjQuery).toHaveBeenCalledWith(
            "[data-smartedit-component-id='theSlotId'][data-smartedit-component-type='ContentSlot'] .smartEditComponentX[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']"
        );
    });

    it('getOverlayComponent will fetch component under slot in the overlay', () => {
        const parent = {} as HTMLElement;
        const originalComponent = jasmine.createSpyObj('originalComponent', ['attr', 'parent']);
        originalComponent.parent.and.returnValue(parent);
        originalComponent.attr.and.callFake(function(attribute: string) {
            if (attribute === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attribute === 'data-smartedit-component-type') {
                return 'thetype';
            } else {
                throw new Error('unexpected selector');
            }
        });

        spyOn(componentHandlerService, 'getParentSlotForComponent').and.returnValue('slotId');
        expect(componentHandlerService.getOverlayComponent(originalComponent)).toBe(yjQueryObject);

        expect(componentHandlerService.getParentSlotForComponent).toHaveBeenCalledWith(parent);
        expect(yjQuery).toHaveBeenCalledWith(
            "[data-smartedit-component-id='slotId'][data-smartedit-component-type='ContentSlot'] .smartEditComponentX[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']"
        );
    });

    it('getOverlayComponent will fetch slot under in the overlay', () => {
        const parent = {} as HTMLElement;
        const originalComponent = jasmine.createSpyObj('originalComponent', ['attr', 'parent']);
        originalComponent.parent.and.returnValue(parent);
        originalComponent.attr.and.callFake(function(attribute: string) {
            if (attribute === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attribute === 'data-smartedit-component-type') {
                return 'thetype';
            } else {
                throw new Error('unexpected selector');
            }
        });

        spyOn(componentHandlerService, 'getParentSlotForComponent').and.returnValue(undefined);
        expect(componentHandlerService.getOverlayComponent(originalComponent)).toBe(yjQueryObject);

        expect(componentHandlerService.getParentSlotForComponent).toHaveBeenCalledWith(parent);
        expect(yjQuery).toHaveBeenCalledWith(
            ".smartEditComponentX[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']"
        );
    });

    it('getParent of an original component will fetch closest parent in the storefront layer', () => {
        const parent = {} as JQuery;
        const component = jasmine.createSpyObj('component', ['attr', 'hasClass', 'closest']);
        component.attr.and.callFake(function(attribute: string) {
            if (attribute === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attribute === 'data-smartedit-component-type') {
                return 'thetype';
            } else {
                throw new Error('unexpected selector');
            }
        });

        component.hasClass.and.callFake(function(className: string) {
            if (className === 'smartEditComponent') {
                return true;
            } else if (className === 'smartEditComponentX') {
                return false;
            } else {
                throw new Error('unexpected selector');
            }
        });

        component.closest.and.returnValue(parent);
        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getParent(component)).toBe(parent);
        expect(component.closest).toHaveBeenCalledWith(
            ".smartEditComponent[data-smartedit-component-id][data-smartedit-component-id!='theid']"
        );
    });

    it('getParent of an overlay component will fetch closest parent in the overlay', () => {
        const parent = {} as JQuery;
        const component = jasmine.createSpyObj('component', ['attr', 'hasClass', 'closest']);
        component.attr.and.callFake(function(attribute: string) {
            if (attribute === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attribute === 'data-smartedit-component-type') {
                return 'thetype';
            } else {
                throw new Error('unexpected selector');
            }
        });

        component.hasClass.and.callFake(function(className: string) {
            if (className === 'smartEditComponent') {
                return false;
            } else if (className === 'smartEditComponentX') {
                return true;
            } else {
                throw new Error('unexpected selector');
            }
        });

        component.closest.and.returnValue(parent);
        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getParent(component)).toBe(parent);
        expect(component.closest).toHaveBeenCalledWith(
            ".smartEditComponentX[data-smartedit-component-id][data-smartedit-component-id!='theid']"
        );
    });

    it('getParent of a component from an unkown layer will throw an exception', () => {
        const parent = {} as HTMLElement;
        const component = jasmine.createSpyObj('component', ['attr', 'hasClass', 'closest']);
        component.attr.and.callFake(function(attribute: string) {
            if (attribute === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attribute === 'data-smartedit-component-type') {
                return 'thetype';
            } else {
                throw new Error('unexpected selector');
            }
        });

        component.hasClass.and.returnValue(null);

        component.closest.and.returnValue(parent);
        yjQuery.and.returnValue(component);
        expect(() => {
            componentHandlerService.getParent(component);
        }).toThrowError('componentHandlerService.getparent.error.component.from.unknown.layer');
    });

    it('setId will set the data-smartedit-component-id field of a given component', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        yjQuery.and.returnValue(component);
        componentHandlerService.setId(originalComponent, 'theid');

        expect(component.attr).toHaveBeenCalledWith('data-smartedit-component-id', 'theid');
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getId will get the data-smartedit-component-id field of a given component', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.returnValue('theid');

        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getId(originalComponent)).toBe('theid');
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('setType will set the data-smartedit-component-type field of a given component', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        yjQuery.and.returnValue(component);
        componentHandlerService.setType(originalComponent, 'thetype');

        expect(component.attr).toHaveBeenCalledWith('data-smartedit-component-type', 'thetype');
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedId will get the data-smartedit-container-id when it is defined AND data-smartedit-container-type is defined', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.callFake(function(attr: string) {
            if (attr === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attr === 'data-smartedit-container-id') {
                return 'thecontainerid';
            } else if (attr === 'data-smartedit-container-type') {
                return 'thecontainertype';
            } else {
                throw new Error('unexpected selector');
            }
        });

        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getSlotOperationRelatedId(originalComponent)).toBe(
            'thecontainerid'
        );
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedId will get the data-smartedit-component-id when data-smartedit-container-id is defined BUT data-smartedit-container-type is undefined', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.callFake(function(attr: string) {
            if (attr === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attr === 'data-smartedit-container-id') {
                return 'thecontainerid';
            } else if (attr === 'data-smartedit-container-type') {
                return undefined;
            } else {
                throw new Error('unexpected selector');
            }
        });

        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getSlotOperationRelatedId(originalComponent)).toBe('theid');
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedId will get the data-smartedit-component-id when data-smartedit-container-id is undefined', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.callFake(function(attr: string) {
            if (attr === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attr === 'data-smartedit-container-id') {
                return undefined;
            } else {
                throw new Error('unexpected selector');
            }
        });

        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getSlotOperationRelatedId(originalComponent)).toBe('theid');
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getType will get the data-smartedit-component-type field of a given component', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.returnValue('thetype');

        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getType(originalComponent)).toBe('thetype');
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedType will get the data-smartedit-container-type when it is defined AND data-smartedit-container-id is defined', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.callFake(function(attr: string) {
            if (attr === 'data-smartedit-component-type') {
                return 'thetype';
            } else if (attr === 'data-smartedit-container-type') {
                return 'thecontainertype';
            } else if (attr === 'data-smartedit-container-id') {
                return 'thecontainerid';
            } else {
                throw new Error('unexpected selector');
            }
        });

        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getSlotOperationRelatedType(originalComponent)).toBe(
            'thecontainertype'
        );
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedType will get the data-smartedit-component-type when data-smartedit-container-type is defined BUT data-smartedit-container-id is undefined', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.callFake(function(attr: string) {
            if (attr === 'data-smartedit-component-type') {
                return 'thetype';
            } else if (attr === 'data-smartedit-container-type') {
                return 'thecontainertype';
            } else if (attr === 'data-smartedit-container-id') {
                return undefined;
            } else {
                throw new Error('unexpected selector');
            }
        });

        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getSlotOperationRelatedType(originalComponent)).toBe(
            'thetype'
        );
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedType will get the data-smartedit-component-type when data-smartedit-container-type is undefined', () => {
        const originalComponent = ({
            key: 'value'
        } as any) as JQuery;
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.callFake(function(attr: string) {
            if (attr === 'data-smartedit-component-type') {
                return 'thetype';
            } else if (attr === 'data-smartedit-container-type') {
                return undefined;
            } else {
                throw new Error('unexpected selector');
            }
        });

        yjQuery.and.returnValue(component);
        expect(componentHandlerService.getSlotOperationRelatedType(originalComponent)).toBe(
            'thetype'
        );
        expect(yjQuery).toHaveBeenCalledWith(originalComponent);
    });

    it('getAllComponentsSelector will return a yjQuery selector matching all non-slots components', () => {
        expect(componentHandlerService.getAllComponentsSelector()).toBe(
            ".smartEditComponent[data-smartedit-component-type!='ContentSlot']"
        );
    });

    it('getAllSlotsSelector will return a yjQuery selector matching all slots components', () => {
        expect(componentHandlerService.getAllSlotsSelector()).toBe(
            ".smartEditComponent[data-smartedit-component-type='ContentSlot']"
        );
    });

    it('getParentSlotForComponent will return slot ID for the given component', () => {
        const parent = jasmine.createSpyObj('parent', ['attr']);
        const component = jasmine.createSpyObj('component', ['closest']);

        parent.attr.and.returnValue('slotId');

        yjQuery.and.returnValue(component);
        component.closest.and.returnValue(parent);

        expect(componentHandlerService.getParentSlotForComponent(component)).toBe('slotId');
    });

    it('getComponentPositionInSlot will return the position for the given component within a slot', () => {
        const slotId = 'slot1';
        const componentId = 'comp2';

        spyOn(componentHandlerService, 'getId');
        (componentHandlerService as jasmine.SpyObj<ComponentHandlerService>).getId.and.callFake(
            function(component: HTMLElement) {
                return component.id;
            }
        );
        spyOn(componentHandlerService, 'getOriginalComponentsWithinSlot').and.returnValue([
            {
                id: 'comp1'
            },
            {
                id: componentId
            },
            {
                id: 'comp3'
            }
        ]);

        expect(componentHandlerService.getComponentPositionInSlot(slotId, componentId)).toBe(1);
    });

    describe('getFirstSmartEditComponentChildren', () => {
        let root: HTMLElement;
        let components: HTMLElement[];

        const createElement = (isSmartEditComponent: boolean, children: HTMLElement[] = []) => {
            const div = document.createElement('div');
            if (isSmartEditComponent) {
                div.classList.add('smartEditComponent');
            }

            children.forEach((child) => {
                div.appendChild(child);
            });

            return div;
        };

        beforeEach(() => {
            yjQuery.and.callFake((element: HTMLElement | HTMLElement[]) => {
                if (Array.isArray(element)) {
                    return element;
                }
                return [element];
            });

            const comp1 = createElement(true);
            const comp2 = createElement(true);
            comp1.appendChild(comp2);

            const comp3 = createElement(true);
            const comp4 = createElement(true);
            comp3.appendChild(comp4);

            root = createElement(false, [createElement(false, [comp1]), comp3]);

            components = [comp1, comp2, comp3, comp4];
        });

        it('should return the first level of SmartEdit components', () => {
            const actual = componentHandlerService.getFirstSmartEditComponentChildren(root);

            expect(actual as any).toEqual([components[0], components[2]]);
        });

        it('should return the children of the SmartEdit component', () => {
            const actual = componentHandlerService.getFirstSmartEditComponentChildren(
                components[0]
            );

            expect(actual as any).toEqual([components[1]]);
        });

        it('should return no smartedit component in an empty root', () => {
            const actual = componentHandlerService.getFirstSmartEditComponentChildren(
                createElement(false, [])
            );

            expect(actual as any).toEqual([]);
        });
    });
});
