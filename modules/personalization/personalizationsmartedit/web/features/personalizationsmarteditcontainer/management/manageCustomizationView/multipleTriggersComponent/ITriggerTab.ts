export interface ITriggerTab {
	id: string;
	title: string;
	templateUrl: string;
	isTriggerDefined(): boolean; // Function returns 'true' if trigger is created and correct, otherwise 'false'
	isValidOrEmpty(): boolean; // Function returns 'true' if trigger is correct or is empty, otherwise 'false'
	// In multiple trigger environment all trigger tabs must be valid or empty, and at least one must have defined trigger
}
