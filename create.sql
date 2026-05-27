-- Table: public.tpl_config

DROP TABLE IF EXISTS public.tpl_config;

CREATE TABLE IF NOT EXISTS public.tpl_config
(
    id bigserial NOT NULL,
    agentid character varying(255) COLLATE pg_catalog."default",
    routeid character varying(255) COLLATE pg_catalog."default",
    config_version character varying(255) COLLATE pg_catalog."default",
    config_key character varying(255) COLLATE pg_catalog."default",
    config_value character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT tpl_config_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tpl_config
    OWNER to postgres;