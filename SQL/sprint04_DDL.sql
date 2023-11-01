--DDL for Sprint 04. Added isActive, registrationDatetime to user table. Added soldAtPriceInCents, isActive, isVisible to lising table.
CREATE SCHEMA sprint04;
GO

CREATE TABLE sprint04.[user](
	userID INT IDENTITY(1,1)
	,userName VARCHAR(250) NOT NULL UNIQUE
	,pWord VARCHAR(250) NOT NULL
	,firstName VARCHAR(250) NOT NULL
	,middleName VARCHAR(250) NOT NULL DEFAULT ''
	,lastName VARCHAR(250) NOT NULL
	,dateOfBirth DATE NOT NULL
	,street VARCHAR(250) NOT NULL
	,city VARCHAR(250) NOT NULL
	,[state] CHAR(2) NOT NULL
	,zip CHAR(5) NOT NULL
	,phoneNum VARCHAR(20) NOT NULL
	,isAdmin BIT NOT NULL DEFAULT 0
	,isActive BIT NOT NULL DEFAULT 1
	,registrationDatetime DATETIME2 DEFAULT GETDATE()
	,CONSTRAINT user_pk PRIMARY KEY(userID)
);

INSERT INTO sprint04.[user]
	(userName, pWord, firstName, middleName, lastName, dateOfBirth, street, city, [state], zip, phoneNum, isAdmin)
	VALUES
	 ('admin','pass','Maddy','Em','Straightener','1973-12-11','123 Maple Street','Tree City', 'NY', '12345', '1-234-567-8901)', 1)
	,('user','pass','Us', '','Er','1993-03-21','987 Grass Street', 'Plains City', 'WY', '98765', '9-876-543-2109', 0)
;

CREATE TABLE sprint04.listing(
	listingID INT IDENTITY(1,1)
	,title VARCHAR(250) NOT NULL
	,description VARCHAR(MAX) NOT NULL DEFAULT ''
	,priceInCents INT NOT NULL
	,isAvailable BIT NOT NULL DEFAULT 1 --possibly redundant with isActive + isVisible, may want to refactor to remove in next revision
	,isForRent BIT NOT NULL --false indicates an item for sale, not for rental
	,rentalPeriodHours INT NOT NULL
	,listingImage VARBINARY(MAX)
	,sellerUserID INT NOT NULL
	,datetimeAdded DATETIME2 DEFAULT GETDATE()
	,datetimeModified DATETIME2
	,soldAtPriceInCents INT
	,isActive BIT DEFAULT 1
	,isVisible BIT DEFAULT 1
	,CONSTRAINT listing_pk PRIMARY KEY(listingID)
	,CONSTRAINT listing_to_seller_fk FOREIGN KEY(sellerUserID) REFERENCES sprint04.[user](userID)
);
GO

--Sets the datetimeModified whenever a listing is altered.
CREATE TRIGGER sprint04_listing_INSERT_UPDATE
	ON sprint04.listing
	AFTER INSERT, UPDATE
AS
	UPDATE sprint04.listing
	SET datetimeModified = GETDATE()
	WHERE listingID IN (SELECT listingID FROM Inserted)
;
GO

INSERT INTO sprint04.listing(title, description, priceInCents, isAvailable, isForRent, rentalPeriodHours, listingImage, sellerUserID, datetimeAdded, datetimeModified)
SELECT title, description, priceInCents, isAvailable, isForRent, rentalPeriodHours, listingImage, sellerUserID, datetimeAdded, datetimeModified
FROM sprint03.listing
;