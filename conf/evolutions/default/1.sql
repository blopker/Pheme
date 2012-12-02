# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table log (
  id                        varchar(255) not null,
  source_name               varchar(255),
  level                     integer,
  message                   varchar(255),
  created                   timestamp,
  constraint ck_log_level check (level in (0,1,2,3)),
  constraint pk_log primary key (id))
;

create sequence log_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists log;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists log_seq;

