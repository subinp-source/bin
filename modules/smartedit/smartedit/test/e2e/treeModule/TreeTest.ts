/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { TreePageObject } from './TreePageObject';
import { AlertsComponentObject } from '../utils/components/AlertsComponentObject';

describe('Tree - ', () => {
    describe('Legacy -', () => {
        beforeEach(async () => {
            await TreePageObject.Actions.navigate();
            await TreePageObject.Actions.setMode('legacy');
        });

        describe('1st level - ', () => {
            it('it displays correct nodes at the start', async () => {
                await TreePageObject.Assertions.hasNodeNames(['node1', 'node2']);
            });

            it('it displays correct node names after clicking outside button', async () => {
                await TreePageObject.Actions.addSiblingOutside();

                await TreePageObject.Assertions.hasNodeNames(['node1', 'node2', 'root2']);
            });

            it('will add sibling', async () => {
                await TreePageObject.Actions.addSibling('node1');
                await TreePageObject.Assertions.hasCorrectNodeCount(3);
                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 0);
                await TreePageObject.Assertions.hasNodeNames(['node1', 'node2', 'root2']);
            });

            it('will remove a node', async () => {
                await TreePageObject.Actions.remove('node1');

                await TreePageObject.Assertions.hasCorrectNodeCount(1);
            });

            it('will trigger custom action having access to node data', async () => {
                await TreePageObject.Actions.showModal('node1');

                await TreePageObject.Assertions.modalContainingTextIsVisible(
                    'Custom action triggered: node1'
                );
            });

            it('will expand node after child is added, then after retoggled children are still present', async () => {
                await TreePageObject.Actions.addChild('node1');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 4);

                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 4);
            });

            it('WHEN I drag a node to position 0, a confirmation modal will pop-up as configured in the beforeDropCallback', async () => {
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Actions.startDragging('node5').then(async () => {
                    return await TreePageObject.Actions.mouseUp().then(async () => {
                        await TreePageObject.Assertions.modalContainingTextIsVisible(
                            'se.tree.confirm.message'
                        );
                    });
                });
            });

            it('WHEN I confirm the modal after being prompted, the modification will happen', async () => {
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);

                await TreePageObject.Actions.startDragging('node3');
                await TreePageObject.Actions.moveMouseToNode('node5');
                await TreePageObject.Actions.mouseUp();
                await TreePageObject.Actions.confirmModal();

                browser.waitUntilNoModal();

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node3',
                    'node5'
                ]);
            });

            it('WHEN I cancel the modal after being prompted, the modification will not happen', async () => {
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);

                await TreePageObject.Actions.startDragging('node3');
                await TreePageObject.Actions.moveMouseToNode('node5');
                await TreePageObject.Actions.mouseUp();
                await TreePageObject.Actions.rejectModal();

                browser.waitUntilNoModal();
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);
            });

            it('WHEN I drag a node to position >1, the modification will not happen as configured in the beforeDropCallback', async () => {
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);

                await TreePageObject.Actions.startDragging('node5');
                await TreePageObject.Actions.moveMouseToNode('node3');
                await TreePageObject.Actions.mouseUp();

                await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(1);

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);
            });

            it('wont accept drop for node outside of its parents scope', async () => {
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);

                await TreePageObject.Actions.startDragging('node5');
                await TreePageObject.Actions.moveMouseToNode('node1');
                await TreePageObject.Actions.mouseUp();

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);
            });

            it('WHEN I drag and drop a node without any failures from beforeDropCallback or acceptDropCallback, the dropCallback action is triggered', async () => {
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Actions.startDragging('node5');
                await TreePageObject.Actions.moveMouseToNode('node4');
                await TreePageObject.Actions.mouseUp();

                await TreePageObject.Assertions.modalContainingTextIsVisible(
                    'Node dropped succesfully!'
                );
            });
        });

        describe('2nd level', () => {
            beforeEach(async () => {
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Actions.addChild('node1');
            });

            it('will add sibling', async () => {
                await TreePageObject.Actions.addSibling('node13');
                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 5);
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3',
                    'node13',
                    'node14'
                ]);
            });

            it('will remove a node', async () => {
                await TreePageObject.Actions.remove('node13');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 3);
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);
            });

            it('will trigger custom action having access to node data', async () => {
                await TreePageObject.Actions.showModal('node13');

                await TreePageObject.Assertions.modalContainingTextIsVisible(
                    'Custom action triggered: node13'
                );
            });

            it('WHEN add child to node that is expanded THEN child is added and children are the same after collapsing and expanding', async () => {
                await TreePageObject.Actions.addChild('node13');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node13', 1);
                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 5);
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node13', ['node130']);

                await TreePageObject.Actions.toggle('node13');
                await TreePageObject.Actions.toggle('node13');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node13', 1);
                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 5);
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node13', ['node130']);
            });
        });
    });

    describe('Angular -', () => {
        beforeEach(async () => {
            await TreePageObject.Actions.navigate();
            await TreePageObject.Actions.setMode('angular');
        });

        describe('1st level - ', async () => {
            it('it displays correct nodes at the start', async () => {
                await TreePageObject.Assertions.hasNodeNames(['node1', 'node2']);
            });

            it('it displays correct node names after clicking outside button', async () => {
                await TreePageObject.Actions.addSiblingOutside();

                await TreePageObject.Assertions.hasNodeNames(['node1', 'node2', 'root2']);
            });

            it('will add sibling', async () => {
                await TreePageObject.Actions.addSibling('node1');

                await TreePageObject.Assertions.hasCorrectNodeCount(3);
                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 0);
                await TreePageObject.Assertions.hasNodeNames(['node1', 'node2', 'root2']);
            });

            it('will remove a node', async () => {
                await TreePageObject.Actions.remove('node1');

                await TreePageObject.Assertions.hasCorrectNodeCount(1);
            });

            it('will trigger custom action having access to node data', async () => {
                await TreePageObject.Actions.showModal('node1');

                await TreePageObject.Assertions.modalContainingTextIsVisible(
                    'Custom action triggered: node1'
                );
            });

            it('will expand node after child is added, then after retoggled children are still present', async () => {
                await TreePageObject.Actions.addChild('node1');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 4);

                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 4);
            });

            it('WHEN I drag a node to position 0, a confirmation modal will pop-up as configured in the beforeDropCallback', async () => {
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Actions.startDragging('node5').then(async () => {
                    return await TreePageObject.Actions.mouseUp().then(async () => {
                        await TreePageObject.Assertions.modalContainingTextIsVisible(
                            'se.tree.confirm.message'
                        );
                    });
                });
            });

            it('WHEN I confirm the modal after being prompted, the modification will happen', async () => {
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);

                await TreePageObject.Actions.startDragging('node3');
                await TreePageObject.Actions.moveMouseToNode('node5');
                await TreePageObject.Actions.mouseUp();
                await TreePageObject.Actions.confirmModal();

                browser.waitUntilNoModal();

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node3',
                    'node5'
                ]);
            });

            it('WHEN I cancel the modal after being prompted, the modification will not happen', async () => {
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);

                await TreePageObject.Actions.startDragging('node3');
                await TreePageObject.Actions.moveMouseToNode('node5');
                await TreePageObject.Actions.mouseUp();
                await TreePageObject.Actions.rejectModal();

                browser.waitUntilNoModal();
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Actions.toggle('node1');

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);
            });

            it('WHEN I drag a node to position >1, the modification will not happen as configured in the beforeDropCallback', async () => {
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);

                await TreePageObject.Actions.startDragging('node5');
                await TreePageObject.Actions.moveMouseToNode('node3');
                await TreePageObject.Actions.mouseUp();

                await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(1);

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);
            });

            it('wont accept drop for node outside of its parents scope', async () => {
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);

                await TreePageObject.Actions.startDragging('node5');
                await TreePageObject.Actions.moveMouseToNode('node1');
                await TreePageObject.Actions.mouseUp();

                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);
            });

            it('WHEN I drag and drop a node without any failures from beforeDropCallback or acceptDropCallback, the dropCallback action is triggered', async () => {
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Actions.startDragging('node5');
                await TreePageObject.Actions.moveMouseToNode('node4');
                await TreePageObject.Actions.mouseUp();

                await TreePageObject.Assertions.modalContainingTextIsVisible(
                    'Node dropped succesfully!'
                );
            });
        });

        describe('2nd level', async () => {
            beforeEach(async () => {
                await TreePageObject.Actions.toggle('node1');
                await TreePageObject.Actions.addChild('node1');
            });

            it('will add sibling', async () => {
                await TreePageObject.Actions.addSibling('node13');
                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 5);
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3',
                    'node13',
                    'node14'
                ]);
            });

            it('will remove a node', async () => {
                await TreePageObject.Actions.remove('node13');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 3);
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node1', [
                    'node4',
                    'node5',
                    'node3'
                ]);
            });

            it('will trigger custom action having access to node data', async () => {
                await TreePageObject.Actions.showModal('node13');

                await TreePageObject.Assertions.modalContainingTextIsVisible(
                    'Custom action triggered: node13'
                );
            });

            it('WHEN add child to node that is expanded THEN child is added and children are the same after collapsing and expanding', async () => {
                await TreePageObject.Actions.addChild('node13');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node13', 1);
                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 5);
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node13', ['node130']);

                await TreePageObject.Actions.toggle('node13');
                await TreePageObject.Actions.toggle('node13');

                await TreePageObject.Assertions.hasCorrectChildrenCount('node13', 1);
                await TreePageObject.Assertions.hasCorrectChildrenCount('node1', 5);
                await TreePageObject.Assertions.hasChildrenInCorrectOrder('node13', ['node130']);
            });
        });
    });
});
