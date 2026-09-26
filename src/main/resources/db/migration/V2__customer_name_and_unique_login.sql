-- V2: customers get a name, and email / phone number must be unique (two customers can't share a login).
-- The customers table was empty when this was written; the DEFAULT '' only exists so the NOT NULL column
-- can be added to a table that already has rows, and is removed straight away.

ALTER TABLE public.customers ADD COLUMN name character varying(255) NOT NULL DEFAULT '';
ALTER TABLE public.customers ALTER COLUMN name DROP DEFAULT;

ALTER TABLE ONLY public.customers ADD CONSTRAINT uk_customers_email UNIQUE (email);
ALTER TABLE ONLY public.customers ADD CONSTRAINT uk_customers_phone_number UNIQUE (phone_number);
