-- Step 1: Add new columns with the desired data types
ALTER TABLE users
ADD shipping_info_new VARCHAR(100);

ALTER TABLE users
ADD password_new VARCHAR(100);

-- Step 2: Copy data from the old columns to the new columns
UPDATE users
SET shipping_info_new = CAST(shipping_info AS VARCHAR(100)),
    password_new = CAST(password AS VARCHAR(100));

-- Step 3: Drop the old columns
ALTER TABLE users
DROP COLUMN shipping_info;

ALTER TABLE users
DROP COLUMN password;

-- Step 4: Rename the new columns to match the old column names
ALTER TABLE users
RENAME COLUMN shipping_info_new TO shipping_info;

ALTER TABLE users
RENAME COLUMN password_new TO password;
