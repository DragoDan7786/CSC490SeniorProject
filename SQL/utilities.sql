--Grant execute permissions for e.g., stored procedures
GRANT EXECUTE
ON DATABASE::pablo
TO csc490app;

--Un-disable an account (in order to test disable function or other functions)
--First re-enable the listings.
UPDATE sprint04.listing
SET isAvailable = 1, isActive = 1, isVisible = 1
WHERE sellerUserID = 3
;
--Then re-enable the user.
UPDATE sprint04.[user]
SET isActive = 1
WHERE userID = 3
;

--An example of holding data in a temporary table while re-creating an original table...it's basically a swap!
CREATE TABLE sprint05.temp_message(
	messageID INT IDENTITY(1,1)
	,fromUsername VARCHAR(250) NOT NULL
	,toUsername VARCHAR(250) NOT NULL
	,datetimeSent DATETIME2 NOT NULL DEFAULT GETDATE()
	,aboutListingID INT
	,subject VARCHAR(250)
	,contents VARCHAR(5000)
	,replyToMessageID INT
	,hidden BIT DEFAULT 0
);

INSERT INTO sprint05.temp_message(fromUsername, toUsername, datetimeSent, aboutListingID, subject, contents, replyToMessageID, hidden)
SELECT fromUsername, toUsername, datetimeSent, aboutListingID, subject, contents, replyToMessageID, isHidden
FROM sprint05.message
;

DROP TABLE sprint05.message;

CREATE TABLE sprint05.message(
	messageID INT IDENTITY(1,1)
	,fromUsername VARCHAR(250) NOT NULL
	,toUsername VARCHAR(250) NOT NULL
	,datetimeSent DATETIME2 NOT NULL DEFAULT GETDATE()
	,aboutListingID INT
	,subject VARCHAR(250)
	,contents VARCHAR(5000)
	,replyToMessageID INT
	,hidden BIT NOT NULL DEFAULT 0
	,CONSTRAINT message_pk PRIMARY KEY(messageID)
	,CONSTRAINT message_from_user_fk FOREIGN KEY(fromUsername) REFERENCES sprint05.[user](userName)
	,CONSTRAINT message_to_user_fk FOREIGN KEY(toUsername) REFERENCES sprint05.[user](userName)
	,CONSTRAINT message_about_listing_fk FOREIGN KEY(aboutListingID) REFERENCES sprint05.listing(listingID)
	,CONSTRAINT reply_to_message_fk FOREIGN KEY(replyToMessageID) REFERENCES sprint05.message(messageID)
);

INSERT INTO sprint05.message(fromUsername, toUsername, datetimeSent, aboutListingID, subject, contents, replyToMessageID, hidden)
SELECT fromUsername, toUsername, datetimeSent, aboutListingID, subject, contents, replyToMessageID, hidden
FROM sprint05.temp_message
;

DROP TABLE sprint05.temp_message;