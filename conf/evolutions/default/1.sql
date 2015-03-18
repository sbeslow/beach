# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table beach (
  id                        bigint not null,
  name                      varchar(255),
  url_code                  varchar(255),
  constraint pk_beach primary key (id))
;

create table beach_snapshot (
  id                        bigint not null,
  scrape_time               timestamp,
  beach_id                  bigint,
  swim_status               varchar(255),
  forecast_for_today        integer,
  most_recent_result        integer,
  result_collected          varchar(255),
  constraint pk_beach_snapshot primary key (id))
;

create table significant_error (
  id                        bigint not null,
  date_time                 timestamp,
  message                   varchar(255),
  constraint pk_significant_error primary key (id))
;

create sequence beach_seq;

create sequence beach_snapshot_seq;

create sequence significant_error_seq;

alter table beach_snapshot add constraint fk_beach_snapshot_beach_1 foreign key (beach_id) references beach (id) on delete restrict on update restrict;
create index ix_beach_snapshot_beach_1 on beach_snapshot (beach_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists beach;

drop table if exists beach_snapshot;

drop table if exists significant_error;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists beach_seq;

drop sequence if exists beach_snapshot_seq;

drop sequence if exists significant_error_seq;

