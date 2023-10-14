CREATE USER csc490app WITH PASSWORD = 'putPasswordHere';

GRANT SELECT ON SCHEMA::[poc_connectivity] TO csc490app;

ALTER ROLE db_datawriter ADD MEMBER csc490app;
ALTER ROLE db_datareader ADD MEMBER csc490app;