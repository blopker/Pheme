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

create table count (
  id                        varchar(255) not null,
  component_id              varchar(255),
  counter_name              varchar(255),
  count                     bigint,
  created                   timestamp,
  constraint pk_count primary key (id))
;

create table log (
  id                        varchar(255) not null,
  component_id              varchar(255),
  log_type                  varchar(255),
  message                   varchar(255),
  created                   timestamp,
  constraint pk_log primary key (id))
;

create sequence component_seq;

create sequence count_seq;

create sequence log_seq;

alter table count add constraint fk_count_component_1 foreign key (component_id) references component (id) on delete restrict on update restrict;
create index ix_count_component_1 on count (component_id);
alter table log add constraint fk_log_component_2 foreign key (component_id) references component (id) on delete restrict on update restrict;
create index ix_log_component_2 on log (component_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists component;

drop table if exists count;

drop table if exists log;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists component_seq;

drop sequence if exists count_seq;

drop sequence if exists log_seq;

