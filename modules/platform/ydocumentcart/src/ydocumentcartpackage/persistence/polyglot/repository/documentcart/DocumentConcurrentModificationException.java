/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import de.hybris.platform.persistence.polyglot.uow.PolyglotPersistenceConcurrentModificationException;


public class DocumentConcurrentModificationException extends PolyglotPersistenceConcurrentModificationException
{
	public DocumentConcurrentModificationException(final String msg)
	{
		super(msg);
	}

	public static DocumentConcurrentModificationException missingDocumentForRemoval(final Entity itemToRemove)
	{
		return new DocumentConcurrentModificationException("Couldn't remove entity ('" + itemToRemove.getId()
				+ "') because the Document it belongs to has been removed in the meantime.");
	}

	public static DocumentConcurrentModificationException missingDocumentForModification(final EntityModification modification)
	{
		return new DocumentConcurrentModificationException("Couldn't modify entity ('" + modification.getId()
				+ "') because the Document it belongs to has been removed in the meantime.");
	}

	public static DocumentConcurrentModificationException missingDocumentForCreation(final EntityCreation creation)
	{
		return new DocumentConcurrentModificationException("Couldn't create entity ('" + creation.getId()
				+ "') because the Document it belongs to has been removed in the meantime.");
	}

	public static DocumentConcurrentModificationException documentAlreadyExist(final Document document)
	{
		return new DocumentConcurrentModificationException(
				"Couldn't create document with id '" + document.getRootId() + "'. It already exists.");
	}

	public static DocumentConcurrentModificationException documentHasBeenRemoved(final Document document)
	{
		return new DocumentConcurrentModificationException("Document with id '" + document.getRootId() + "' has been removed.");
	}

	public static DocumentConcurrentModificationException documentHasBeenModified(final Document document)
	{
		return new DocumentConcurrentModificationException(
				"Document with id '" + document.getRootId() + "' and version '" + document.getVersion() + "' has been modified.");
	}

}
