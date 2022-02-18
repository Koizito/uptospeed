CREATE TABLE IF NOT EXISTS public.pos_requests
(
    original_string text COLLATE pg_catalog."default" NOT NULL,
    pos_string text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pos_requests_pkey PRIMARY KEY (original_string)
)
