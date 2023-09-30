--create the database
CREATE DATABASE buysellswap;
GO

--"poc" = proof of concept
--In this case, this schema will host tables to be used in establishing that we can connect our frontend to our backend.
--That is to say, these tables will serve as proof of concept that we can manipulate our DB from within our Java app.
CREATE SCHEMA poc_connectivity;
GO

--This table contains the values currently being stored in the User class.
--The exception is hash: this is not currently being used, but will be used later if we are able to setup Firebase as our user registration service.
CREATE TABLE poc_connectivity.[user](
	userID INT IDENTITY(1,1)
	,[password] VARCHAR(250) NOT NULL
	,first_name VARCHAR(250) NOT NULL
	,middle_name VARCHAR(250)
	,last_name VARCHAR(250) NOT NULL
	,date_of_birth DATE NOT NULL
	,street VARCHAR(250)
	,city VARCHAR(250)
	,[state] CHAR(2)
	,zip CHAR(5)
	,phone VARCHAR(20)
	,[hash] VARCHAR(250)
	,isAdmin BIT NOT NULL DEFAULT 0
	,CONSTRAINT user_pk PRIMARY KEY(userID)
);



