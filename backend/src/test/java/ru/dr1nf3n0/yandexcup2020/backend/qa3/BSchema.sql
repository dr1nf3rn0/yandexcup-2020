CREATE TABLE qrtz_log(
    id integer not null,  
    job_name varchar(80) not null,  
    job_group varchar(80) not null,  
    trigger_fire_time timestamp with time zone not null,  
    job_finished_time timestamp with time zone,  
    job_status varchar(200),  
    host_name varchar(80) not null
);