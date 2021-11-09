import 'jasmine';
import {PersonalizationsmarteditTriggerService} from "../../../../../web/features/personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditTriggerService";

describe('PersonalizationsmarteditTriggerService', () => {

	const container = {
		type: 'container',
		nodes: [{}]
	};
	const item = {
		type: 'item',
		selectedSegment: {}

	};
	const emptyContainer = {
		type: 'container',
		nodes: []
	};
	const dropzone = {
		type: 'dropzone'
	};

	let triggerService: PersonalizationsmarteditTriggerService;

	// === SETUP ===
	beforeEach(() => {
		triggerService = new PersonalizationsmarteditTriggerService();
	});


	describe('isContainer', () => {

		it('should be defined', () => {
			expect(triggerService.isContainer).toBeDefined();
		});

		it('should return true for a container', () => {
			expect(triggerService.isContainer(container)).toBe(true);
		});

		it('should return false for not a container', () => {
			expect(triggerService.isContainer(item)).toBe(false);
			expect(triggerService.isContainer(dropzone)).toBe(false);
		});

		it('should return false for undefined', () => {
			expect(triggerService.isContainer()).toBe(false);
		});

	});

	describe('isNotEmptyContainer', () => {

		it('should be defined', () => {
			expect(triggerService.isNotEmptyContainer).toBeDefined();
		});

		it('should return true for a not empty container', () => {
			expect(triggerService.isNotEmptyContainer(container)).toBe(true);
		});

		it('should return false for empty container', () => {
			expect(triggerService.isNotEmptyContainer(emptyContainer)).toBe(false);
		});

		it('should return false for undefined', () => {
			expect(triggerService.isNotEmptyContainer()).toBe(false);
		});

	});

	describe('isEmptyContainer', () => {

		it('should be defined', () => {
			expect(triggerService.isEmptyContainer).toBeDefined();
		});

		it('should return true for a empty container', () => {
			expect(triggerService.isEmptyContainer(emptyContainer)).toBe(true);
		});

		it('should return false for not empty container', () => {
			expect(triggerService.isEmptyContainer(container)).toBe(false);
		});

		it('should return false for undefined', () => {
			expect(triggerService.isEmptyContainer()).toBe(false);
		});
	});

	describe('isItem', () => {

		it('should be defined', () => {
			expect(triggerService.isItem).toBeDefined();
		});

		it('should return true for a item', () => {
			expect(triggerService.isItem(item)).toBe(true);
		});

		it('should return false for not a item', () => {
			expect(triggerService.isItem(container)).toBe(false);
			expect(triggerService.isItem(dropzone)).toBe(false);
		});

		it('should return false for undefined', () => {
			expect(triggerService.isItem()).toBe(false);
		});
	});

	describe('isDropzone', () => {

		it('should be defined', () => {
			expect(triggerService.isDropzone).toBeDefined();
		});

		it('should return true for a dropzone', () => {
			expect(triggerService.isDropzone(dropzone)).toBe(true);
		});

		it('should return false for not a dropzone', () => {
			expect(triggerService.isDropzone(container)).toBe(false);
			expect(triggerService.isDropzone(item)).toBe(false);
		});

		it('should return false for undefined', () => {
			expect(triggerService.isDropzone()).toBe(false);
		});
	});

	describe('actions', () => {

		it('should be defined', () => {
			expect(triggerService.actions).toBeDefined();
		});

		it('should have id and name', () => {
			triggerService.actions.forEach((itemAction: any) => {
				expect(itemAction.id).toBeDefined();
				expect(itemAction.name).toBeDefined();
			});

		});

	});

	describe('isValidExpression', () => {

		it('should be defined', () => {
			expect(triggerService.isValidExpression).toBeDefined();
		});

		it('should return true for simple expression', () => {
			const expression = {
				type: 'container',
				nodes: [item, item, item]
			};

			expect(triggerService.isValidExpression(expression)).toBe(true);
		});

		it('should return true for complex expresion', () => {
			const complexExpression = {
				type: 'container',
				nodes: [{
					type: 'container',
					nodes: [
						item, {
							type: 'container',
							nodes: [item]
						}, {
							type: 'container',
							nodes: [item]
						}
					]
				}, item, item]
			};

			expect(triggerService.isValidExpression(complexExpression)).toBe(true);
		});

		it('should return false for expression with empty container', () => {
			const emptyExpression = {
				type: 'container',
				nodes: [] // empty container
			};

			expect(triggerService.isValidExpression(emptyExpression)).toBe(false);
		});

		it('should return false for complex expression with empty container', () => {
			const emptyComplexExpression = {
				type: 'container',
				nodes: [{
					type: 'container',
					nodes: [
						item, {
							type: 'container',
							nodes: [item]
						}, {
							type: 'container',
							nodes: [{}] // invalid item in container
						}
					]
				}, item, item]
			};

			expect(triggerService.isValidExpression(emptyComplexExpression)).toBe(false);
		});

		it('should return false for undefined', () => {
			expect(triggerService.isValidExpression()).toBe(false);
		});

	});

	describe('buildTriggers', () => {

		const itemA = {
			type: 'item',
			operation: {},
			selectedSegment: {
				code: 'A'
			}
		};
		const itemB = {
			type: 'item',
			operation: {},
			selectedSegment: {
				code: 'B'
			}
		};
		const undefinedTrigger = {
			type: 'undefindedTriggerData',
			code: 'undefined'
		};

		it('should be defined', () => {
			expect(triggerService.buildTriggers).toBeDefined();
		});

		it('should build default trigger', () => {
			const form = {
				isDefault: true
			};

			const trigger = {
				type: 'defaultTriggerData'
			};

			expect(triggerService.buildTriggers(form)).toEqual([trigger]);
		});

		it('should build segment trigger', () => {
			const form = {
				expression: [{
					type: 'group',
					operation: {
						id: 'AND'
					},
					nodes: [itemA, itemB]
				}]
			};

			const trigger = {
				type: 'segmentTriggerData',
				groupBy: 'AND',
				segments: [{
					code: 'A'
				}, {
					code: 'B'
				}]
			};

			expect(triggerService.buildTriggers(form)).toEqual([trigger]);
		});

		it('should build simple expression trigger', () => {
			const form = {
				expression: [{
					type: 'container',
					operation: {
						id: 'NOT'
					},
					nodes: [itemA, itemB]
				}]
			};

			const trigger = {
				type: 'expressionTriggerData',
				expression: {
					type: 'negationExpressionData',
					element: {
						type: 'groupExpressionData',
						operator: 'AND',
						elements: [{
							type: 'segmentExpressionData',
							code: 'A'
						}, {
							type: 'segmentExpressionData',
							code: 'B'
						}]
					}
				}
			};

			expect(triggerService.buildTriggers(form)).toEqual([trigger]);
		});

		it('should build complex expression trigger', () => {
			const form = {
				expression: [{
					type: 'container',
					operation: {
						id: 'OR'
					},
					nodes: [{
						type: 'container',
						operation: {
							id: 'NOT'
						},
						nodes: [itemA, itemB]
					}, itemB]
				}]
			};

			const trigger = {
				type: 'expressionTriggerData',
				expression: {
					type: 'groupExpressionData',
					operator: 'OR',
					elements: [{
						type: 'negationExpressionData',
						element: {
							type: 'groupExpressionData',
							operator: 'AND',
							elements: [{
								type: 'segmentExpressionData',
								code: 'A'
							}, {
								type: 'segmentExpressionData',
								code: 'B'
							}]
						}
					}, {
						type: 'segmentExpressionData',
						code: 'B'
					}]
				}
			};

			expect(triggerService.buildTriggers(form)).toEqual([trigger]);
		});

		it('should merge default trigger', () => {
			const form = {
				isDefault: true
			};

			const trigger = {
				type: 'defaultTriggerData'
			};

			const existingTriggers = [{
				type: 'expressionTriggerData',
				code: 'codeA'
			}, {
				type: 'segmentTriggerData',
				code: 'code'
			}, undefinedTrigger];


			expect(triggerService.buildTriggers(form, existingTriggers)).toEqual([undefinedTrigger, trigger]);
		});

		it('should merge segment trigger', () => {
			const form = {
				expression: [{
					type: 'group',
					operation: {
						id: 'AND'
					},
					nodes: [itemA, itemB]
				}]
			};

			const trigger = {
				type: 'segmentTriggerData',
				code: 'code',
				groupBy: 'AND',
				segments: [{
					code: 'A'
				}, {
					code: 'B'
				}]
			};

			const existingTriggers = [{
				type: 'expressionTriggerData',
				code: 'codeA'
			}, {
				type: 'segmentTriggerData',
				code: 'code'
			}, undefinedTrigger];

			expect(triggerService.buildTriggers(form, existingTriggers)).toEqual([undefinedTrigger, trigger]);
		});

		it('should merge expression trigger', () => {
			const form = {
				expression: [{
					type: 'container',
					operation: {
						id: 'NOT'
					},
					nodes: [itemA, itemB]
				}]
			};

			const trigger = {
				type: 'expressionTriggerData',
				code: 'code',
				expression: {
					type: 'negationExpressionData',
					element: {
						type: 'groupExpressionData',
						operator: 'AND',
						elements: [{
							type: 'segmentExpressionData',
							code: 'A'
						}, {
							type: 'segmentExpressionData',
							code: 'B'
						}]
					}
				}
			};

			const existingTriggers = [{
				type: 'expressionTriggerData',
				code: 'code'
			}, undefinedTrigger];

			expect(triggerService.buildTriggers(form, existingTriggers)).toEqual([undefinedTrigger, trigger]);
		});

		it('should build nothing', () => {
			expect(triggerService.buildTriggers()).toEqual([{}]);
		});

	});

	describe('buildData', () => {

		it('should be defined', () => {
			expect(triggerService.buildData).toBeDefined();
		});

		it('should build data from default trigger', () => {
			const trigger = {
				type: 'defaultTriggerData',
				code: 'code'
			};

			const data = [{
				type: 'container',
				operation: {
					id: 'AND',
					name: 'personalization.modal.customizationvariationmanagement.targetgrouptab.expression.and'
				},
				nodes: []
			}];

			expect(triggerService.buildData([trigger])).toEqual(data);
		});

		it('should build data from segment trigger', () => {
			const trigger = {
				type: 'segmentTriggerData',
				code: 'code',
				groupBy: 'OR',
				segments: [{
					code: 'SegmentA'
				}, {
					code: 'SegmentB'
				}]
			};
			const data = [{
				type: 'container',
				operation: {
					id: 'OR',
					name: 'personalization.modal.customizationvariationmanagement.targetgrouptab.expression.or'
				},
				nodes: [{
					type: 'item',
					operation: '',
					selectedSegment: {
						code: 'SegmentA'
					},
					nodes: []
				}, {
					type: 'item',
					operation: '',
					selectedSegment: {
						code: 'SegmentB'
					},
					nodes: []
				}]
			}];

			expect(triggerService.buildData([trigger])).toEqual(data);
		});

		it('should build data from expression trigger', () => {
			const trigger = {
				type: 'expressionTriggerData',
				code: 'code',
				expression: {
					type: 'negationExpressionData',
					element: {
						type: 'groupExpressionData',
						operator: 'AND',
						elements: [{
							type: 'segmentExpressionData',
							code: 'A'
						}, {
							type: 'segmentExpressionData',
							code: 'B'
						}]
					}
				}
			};

			const data = [{
				type: 'container',
				operation: {
					id: 'NOT',
					name: 'personalization.modal.customizationvariationmanagement.targetgrouptab.expression.not'
				},
				nodes: [{
					type: 'item',
					operation: '',
					selectedSegment: {
						code: 'A'
					},
					nodes: []
				}, {
					type: 'item',
					operation: '',
					selectedSegment: {
						code: 'B'
					},
					nodes: []
				}]
			}];

			expect(triggerService.buildData([trigger])).toEqual(data);
		});

		it('should build empty data', () => {
			const data = [{
				type: 'container',
				operation: {
					id: 'AND',
					name: 'personalization.modal.customizationvariationmanagement.targetgrouptab.expression.and'
				},
				nodes: []
			}];

			expect(triggerService.buildData({})).toEqual(data);
		});

	});

	describe('getExpressionAsString', () => {

		const dropzoneItem = {
			type: 'dropzone'
		};

		it('should be defined', () => {
			expect(triggerService.getExpressionAsString).toBeDefined();
		});

		it('should return empty string if called with undefined expression object', () => {
			expect(triggerService.getExpressionAsString()).toBe("");
		});

		it('should return proper string object for empty expression container', () => {

			const data = [{
				type: 'container',
				operation: {
					id: 'OR',
				},
				nodes: [{
					type: 'container',
					operation: {
						id: "OR"
					},
					nodes: [dropzoneItem]
				}]
			}];

			const stringExpression = "(( [] ))";

			expect(triggerService.getExpressionAsString(data[0])).toEqual(stringExpression);
		});

		it('should return proper string object for empty expression container with NOT operator', () => {

			const data = [{
				type: 'container',
				operation: {
					id: 'NOT',
				},
				nodes: [dropzoneItem]
			}];

			const stringExpression = " NOT ( [] )";

			expect(triggerService.getExpressionAsString(data[0])).toEqual(stringExpression);
		});

		it('should return proper string object for none empty expression container with NOT operator', () => {

			const data = [{
				type: 'container',
				operation: {
					id: 'NOT',
				},
				nodes: [{
					type: 'container',
					operation: {
						id: "NOT"
					},
					nodes: [{
						type: 'item',
						operation: '',
						selectedSegment: {
							code: 'SegmentA'
						},
						nodes: []
					}]
				}]
			}];

			const stringExpression = " NOT ( NOT (SegmentA))";

			expect(triggerService.getExpressionAsString(data[0])).toEqual(stringExpression);
		});

		it('should return proper string object for simple expression', () => {

			const data = [{
				type: 'container',
				operation: {
					id: 'OR',
				},
				nodes: [{
					type: 'item',
					operation: '',
					selectedSegment: {
						code: 'SegmentA'
					},
					nodes: []
				}, {
					type: 'item',
					operation: '',
					selectedSegment: {
						code: 'SegmentB'
					},
					nodes: []
				}]
			}];

			const stringExpression = "(SegmentA OR SegmentB)";

			expect(triggerService.getExpressionAsString(data[0])).toEqual(stringExpression);
		});

		it('should return proper string object for expression with NOT container', () => {

			const data = [{
				type: 'container',
				operation: {
					id: 'OR',
				},
				nodes: [{
					type: 'item',
					operation: '',
					selectedSegment: {
						code: 'SegmentA'
					},
					nodes: []
				}, {
					type: 'container',
					operation: {
						id: 'NOT'
					},
					nodes: [{
						type: 'item',
						operation: '',
						selectedSegment: {
							code: 'SegmentC'
						},
						nodes: []
					}, {
						type: 'item',
						operation: '',
						selectedSegment: {
							code: 'SegmentB'
						},
						nodes: []
					}]
				}]
			}];

			const stringExpression = "(SegmentA OR  NOT (SegmentC AND SegmentB))";

			expect(triggerService.getExpressionAsString(data[0])).toEqual(stringExpression);
		});

	});

});
