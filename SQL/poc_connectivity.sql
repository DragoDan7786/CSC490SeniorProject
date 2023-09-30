--create the database
CREATE DATABASE buysellswap;
GO

USE buysellswap;
GO

GRANT CREATE SCHEMA ON DATABASE::buysellswap TO admin;
GO

--"poc" = proof of concept
--In this case, this schema will host tables to be used in establishing that we can connect our frontend to our backend.
--That is to say, these tables will serve as proof of concept that we can manipulate our DB from within our Java app.
CREATE SCHEMA poc_connectivity;
GO
--This statement did not work because of some sort of issue with permissions. However, I was able to manually create the schema and give ownership to admin using Management Studio.

--This table contains the values currently being stored in the User class.
--The exception is hash: this is not currently being used, but will be used later if we are able to setup Firebase as our user registration service.
CREATE TABLE poc_connectivity.[user](
	userID INT IDENTITY(1,1) --will probably be replaced by the hash later, if we get that working
	,userName VARCHAR(250) NOT NULL
	,pWord VARCHAR(250) NOT NULL
	,firstName VARCHAR(250) NOT NULL
	,middleName VARCHAR(250)
	,lastName VARCHAR(250) NOT NULL
	,dateOfBirth DATE NOT NULL
	,street VARCHAR(250) NOT NULL
	,city VARCHAR(250) NOT NULL
	,[state] CHAR(2) NOT NULL
	,zip CHAR(5) NOT NULL
	,phoneNum VARCHAR(20)
	,[hash] VARCHAR(250)
	,isAdmin BIT NOT NULL DEFAULT 0
	,CONSTRAINT user_pk PRIMARY KEY(userID)
);

INSERT INTO poc_connectivity.[user]
	(userName, pWord, firstName, middleName, lastName, dateOfBirth, street, city, [state], zip, phoneNum, [hash], isAdmin)
	VALUES
	 ('admin','pass','Maddy','Em','Straightener','1973-12-11','123 Maple Street','Tree City', 'NY', '12345', '1-234-567-8901)','thisIsNotARealHash', 1)
	,('user','pass','U.', 'S.','Er','1993-03-21','987 Grass Street', 'Plains City', 'WY', '98765', '9-876-543-2109','norIsThisARealHash', 0)
;


