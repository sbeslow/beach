# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table beach (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  url_code                  varchar(255),
  latitude                  double,
  longitude                 double,
  constraint pk_beach primary key (id))
;

create table beach_snapshot (
  id                        bigint auto_increment not null,
  scrape_time               datetime,
  beach_id                  bigint,
  swim_status               varchar(255),
  forecast_for_today        double,
  most_recent_result        double,
  result_collected          varchar(255),
  result_date               date,
  constraint pk_beach_snapshot primary key (id))
;

alter table beach_snapshot add constraint fk_beach_snapshot_beach_1 foreign key (beach_id) references beach (id) on delete restrict on update restrict;
create index ix_beach_snapshot_beach_1 on beach_snapshot (beach_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table beach;

drop table beach_snapshot;

SET FOREIGN_KEY_CHECKS=1;

