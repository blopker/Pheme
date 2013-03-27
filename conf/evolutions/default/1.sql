# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table component (
  id                        varchar(255) not null,
  component_name            varchar(255),
  component_type            integer,
  created                   timestamp,
  constraint ck_component_component_type check (component_type in (0,1)),
  constraint pk_component primary key (id))
;

create sequence component_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists component;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists component_seq;

