use db;
UPDATE roles 
SET
    role = 'admin'
where
    id = 1;

UPDATE roles 
SET
    role = 'sub_admin'
where
    id = 2;

UPDATE roles 
SET
    role = 'client'
where
    id = 3;