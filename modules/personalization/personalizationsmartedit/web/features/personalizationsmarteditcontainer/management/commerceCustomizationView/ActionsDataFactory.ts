import {SeInjectable, SeValueProvider, TypedMap} from 'smarteditcommons';

export const PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES_PROVIDER: SeValueProvider = {
	provide: "PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES",
	useValue: {
		OLD: 'old',
		NEW: 'new',
		DELETE: 'delete',
		UPDATE: 'update'
	}
};

@SeInjectable()
export class ActionsDataFactory {

	public actions: any[] = [];
	public removedActions: any[] = [];

	constructor(
		private PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES: TypedMap<string>
	) {}

	getActions = (): any[] => {
		return this.actions;
	}

	getRemovedActions = (): any[] => {
		return this.removedActions;
	}

	resetActions = () => {
		this.actions.length = 0;
	}

	resetRemovedActions = () => {
		this.removedActions.length = 0;
	}

	// This function requires two parameters
	// action to be added
	// and comparer = function(action,action) for defining if two actions are identical
	// comparer is used

	addAction = (action: any, comparer: any) => {

		let exist = false;
		this.actions.forEach((wrapper: any) => {
			exist = exist || comparer(action, wrapper.action);
		});
		if (!exist) {
			let status = this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.NEW;
			let removedIndex = -1;
			this.removedActions.forEach((wrapper: any, index: any) => {
				if (comparer(action, wrapper.action)) {
					removedIndex = index;
				}
			});
			if (removedIndex >= 0) { // we found or action in delete queue
				status = this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.OLD;
				this.removedActions.splice(removedIndex, 1);
			}
			this.actions.push({
				action,
				status
			});
		}
	}

	isItemInSelectedActions = (action: any, comparer: any) => {
		return this.actions.find((wrapper: any) => {
			return comparer(action, wrapper.action);
		});
	}
}