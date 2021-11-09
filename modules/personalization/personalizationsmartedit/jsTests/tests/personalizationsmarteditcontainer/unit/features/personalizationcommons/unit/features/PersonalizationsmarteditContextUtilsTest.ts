import 'jasmine';
import {PersonalizationsmarteditContextUtils} from "../../../../../../../../web/features/personalizationcommons/PersonalizationsmarteditContextUtils";

describe('personalizationsmarteditContextUtils', () => {

	// Service being tested
	let personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils;

	// === SETUP ===
	beforeEach(() => {
		personalizationsmarteditContextUtils = new PersonalizationsmarteditContextUtils();

	});

	describe('clearCustomizeContext', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditContextUtils.clearCustomizeContext).toBeDefined();
		});

	});

	describe('clearCustomizeContextAndReloadPreview', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview).toBeDefined();
		});

		it('should call proper functions in services', () => {
			// given
			const mockVariations = [{
				name: "1"
			}, {
				name: "2"
			}];
			const mockCustomize = {
				enabled: false,
				selectedCustomization: "test",
				selectedVariations: mockVariations,
				selectedComponents: null as any
			};
			const mockPreviewService = {
				removePersonalizationDataFromPreview() {
					return '';
				}
			};
			const mockContextService = {
				getCustomize() {
					return mockCustomize;
				},
				setCustomize() {
					return;
				}
			};

			spyOn(mockPreviewService, 'removePersonalizationDataFromPreview').and.callThrough();
			spyOn(mockContextService, 'getCustomize').and.callThrough();
			spyOn(mockContextService, 'setCustomize').and.callThrough();
			// when
			personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview(mockPreviewService, mockContextService);

			// then
			expect(mockPreviewService.removePersonalizationDataFromPreview).not.toHaveBeenCalled();
			expect(mockContextService.getCustomize).toHaveBeenCalled();
			expect(mockContextService.setCustomize).toHaveBeenCalled();
		});

	});

	describe('clearCombinedViewCustomizeContext', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext).toBeDefined();
		});

	});

	describe('clearCombinedViewContext', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditContextUtils.clearCombinedViewContext).toBeDefined();
		});

	});

	describe('clearCombinedViewContextAndReloadPreview', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditContextUtils.clearCombinedViewContextAndReloadPreview).toBeDefined();
		});

		it('should call proper functions in services and set properties to initial values', () => {
			// given
			const mockCombinedView: any = {
				enabled: true,
				selectedItems: []
			};
			const mockPreviewService = {
				removePersonalizationDataFromPreview() {
					return '';
				}
			};
			const mockContextService = {
				getCombinedView() {
					return mockCombinedView;
				},
				setCombinedView() {
					return;
				}
			};

			spyOn(mockPreviewService, 'removePersonalizationDataFromPreview').and.callThrough();
			spyOn(mockContextService, 'getCombinedView').and.callThrough();
			spyOn(mockContextService, 'setCombinedView').and.callThrough();
			// when
			personalizationsmarteditContextUtils.clearCombinedViewContextAndReloadPreview(mockPreviewService, mockContextService);

			// then
			expect(mockPreviewService.removePersonalizationDataFromPreview).toHaveBeenCalled();
			expect(mockContextService.getCombinedView).toHaveBeenCalled();
			expect(mockContextService.setCombinedView).toHaveBeenCalled();
		});

	});
});
