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