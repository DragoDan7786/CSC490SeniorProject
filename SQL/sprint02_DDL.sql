--DDL for Sprint 02. Includes 'user' and 'saleable_item' tables, and sample data. Primarily for testing "add item" functionality.
CREATE SCHEMA sprint02;
GO

CREATE TABLE sprint02.[user](
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
	,CONSTRAINT user_pk PRIMARY KEY(userID)
);

INSERT INTO sprint02.[user]
	(userName, pWord, firstName, middleName, lastName, dateOfBirth, street, city, [state], zip, phoneNum, isAdmin)
	VALUES
	 ('admin','pass','Maddy','Em','Straightener','1973-12-11','123 Maple Street','Tree City', 'NY', '12345', '1-234-567-8901)', 1)
	,('user','pass','Us', '','Er','1993-03-21','987 Grass Street', 'Plains City', 'WY', '98765', '9-876-543-2109', 0)
;

CREATE TABLE sprint02.saleable_item(
	itemID INT IDENTITY(1,1)
	,name VARCHAR(250) NOT NULL
	,description VARCHAR(MAX) NOT NULL DEFAULT ''
	,priceInCents INT NOT NULL
	,isAvailable BIT NOT NULL DEFAULT 1
	,isForRent BIT NOT NULL --false indicates an item for sale, not for rental
	,rentalPeriodHours INT NOT NULL
	,itemImage VARBINARY(MAX)
	,sellerUserID INT NOT NULL
	,CONSTRAINT saleable_item_pk PRIMARY KEY(itemID)
	,CONSTRAINT saleable_item_seller_fk FOREIGN KEY(sellerUserID) REFERENCES sprint02.[user](userID)
);