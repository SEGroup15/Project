--------------DROP TABLE-------------------
drop table if exists users cascade;
drop table if exists possession_maintainer cascade;
drop table if exists competence cascade;
drop table if exists assigned cascade;
drop table if exists extra_competence cascade;
drop table if exists ewo_competence cascade;
drop table if exists possession_procedure cascade;
drop table if exists procedure cascade;
drop table if exists activity cascade;
drop table if exists possession_act cascade;
drop table if exists typology cascade;
drop table if exists site cascade;
drop table if exists material cascade;
drop table if exists pm cascade;

---------------CREATE TABLE-------------------
create table users(
username varchar(20) primary key,
password varchar(20) not null,
role varchar(15)not null check (role='planner' or role='maintainer' or role='system_admin'));

create table competence(
name_competence varchar(40) primary key);

create table possession_maintainer(
name_competence varchar(40) not null references competence(name_competence) on delete cascade,
username varchar(20) not null references users(username) on delete cascade,
primary key(name_competence,username)
);


create table procedure(
name_procedure varchar(40) primary key,
url_pdf varchar(70) null
);
create table possession_procedure(
name_competence varchar(40) not null references competence(name_competence) on delete cascade,
name_procedure varchar(40) not null references procedure(name_procedure) on delete cascade,
primary key(name_competence,name_procedure)
);

create table typology(
name_typology varchar(30) primary key
);



create table material(
name_material varchar(30) primary key
);


create table site(
factory_site varchar(30) not null,
area varchar(30) not null,
primary key(factory_site,area)
);


create table activity(
id integer primary key check(id>0),
factory_site varchar(30) not null,
area varchar(30) not null,
name_typology varchar(30) not null  references typology(name_typology) on delete cascade,
description varchar(100) null,
estimated_time integer null check (estimated_time>=0),
interruptibility bool not null,
week integer not null check (week>0 and week < 53),
workspace_notes varchar(100) null,
type varchar(20) not null,
foreign key(factory_site,area) references site(factory_site,area) on delete cascade,
check((type='planned' and estimated_time is not null and description is not null) or (type='ewo') or (type='extra')));


create table pm(
name_material varchar(30) not null references material(name_material) on delete cascade,
id integer not null references activity(id) on delete cascade,
primary key(name_material,id));

create table assigned(
giorno varchar(15) not null check(giorno='lunedi' or giorno='martedi' or giorno='mercoledi' or giorno='giovedi' or giorno='venerdi' or giorno='sabato' or giorno='domenica'),
fascia integer not null check (fascia>0 and fascia<8),
id integer not null references activity(id) on delete cascade,
username varchar(20) not null references users(username) on delete cascade,
minuti integer not null check (minuti>=0 and minuti <=60),
primary key(giorno,fascia,id,username)
);

create table extra_competence(
name_competence varchar(40) not null references competence(name_competence) on delete cascade,
id integer not null references activity(id) on delete cascade,
primary key(id,name_competence)
);

create table ewo_competence(
name_competence varchar(40) not null references competence(name_competence) on delete cascade,
id integer not null references activity(id) on delete cascade,
primary key(id,name_competence)
);

create table possession_act(
name_procedure varchar(40) not null references procedure(name_procedure) on delete cascade,
id integer not null references activity(id) on delete cascade,
primary key(id,name_procedure)
);

-------------TRIGGER TO RESOLVE CONSTRAINTS------------------
drop function if exists check_ewo_competence cascade;
drop function if exists check_extra_competence cascade;
drop function if exists check_possession_act cascade;
drop function if exists check_possession_maintainer cascade;
drop function if exists check_assigned_maintainer cascade;


CREATE OR REPLACE FUNCTION check_ewo_competence() RETURNS TRIGGER AS $$
declare tipo varchar;
BEGIN 
select type into tipo from activity A where (a.id=new.id);
if (tipo!='ewo') then
raise exception 'Inserting competences for ewo activities to a no ewo activity';
end if;
return new;
END $$ language plpgsql;

create trigger checkewo_competence
before insert on ewo_competence
for each row execute procedure check_ewo_competence();


CREATE OR REPLACE FUNCTION check_extra_competence() RETURNS TRIGGER AS $$
declare tipo varchar;
BEGIN 
select type into tipo from activity A where (a.id=new.id);
if (tipo!='extra') then
raise exception 'Inserting competences for extra activities to a no extra activity';
end if;
return new;
END $$ language plpgsql;

create trigger checkextra_competence
before insert on extra_competence
for each row execute procedure check_extra_competence();



CREATE OR REPLACE FUNCTION check_possession_act() RETURNS TRIGGER AS $$
declare tipo varchar;
BEGIN 
select type into tipo from activity A where (a.id=new.id);
if (tipo!='planned') then
raise exception 'inserting a procedure for an activity not planned';
end if;
return new;
END $$ language plpgsql;

create trigger checkpossession_act
before insert on possession_act
for each row execute procedure check_possession_act();



CREATE OR REPLACE FUNCTION check_possession_maintainer() RETURNS TRIGGER AS $$
declare ruolo varchar;
BEGIN 
select role into ruolo from users U where (U.username=new.username);
if (ruolo!='maintainer') then
raise exception 'Assigning competences to someone who''s not a maintainer';
end if;
return new;
END $$ language plpgsql;

create trigger checkpossession_maintainer
before insert on possession_maintainer
for each row execute procedure check_possession_maintainer();



CREATE OR REPLACE FUNCTION check_assigned_maintainer() RETURNS TRIGGER AS $$
declare ruolo varchar;
BEGIN 
select role into ruolo from users U where (U.username=new.username);
if (ruolo!='maintainer') then
raise exception 'Assigning activity to someone who''s not a maintainer';
end if;
return new;
END $$ language plpgsql;

create trigger checkassigned_maintainer
before insert on assigned
for each row execute procedure check_assigned_maintainer();

------------------VIEWS---------------------------------------------
drop view if exists planned_activity;
create view planned_activity as 
select factory_site,area,name_typology,description,estimated_time,interruptibility,week,workspace_notes 
from activity;

drop function if exists insert_planned cascade;
CREATE OR REPLACE FUNCTION insert_planned() RETURNS TRIGGER AS $$
declare iddi integer;
BEGIN 
select max(id) into iddi from activity;
if (iddi is null) then
iddi=0;
end if;
iddi=iddi+1;
insert into activity values (iddi,new.factory_site,new.area,new.name_typology,new.description,new.estimated_time,new.interruptibility,new.week,new.workspace_notes,'planned');
return new;
END $$ language plpgsql;

create trigger insertplanned
instead of insert on planned_activity
for each row execute procedure insert_planned();


drop view if exists extra_activity;
create view extra_activity as 
select factory_site,area,name_typology,interruptibility,week,workspace_notes 
from activity;

drop function if exists insert_extra cascade;
CREATE OR REPLACE FUNCTION insert_extra() RETURNS TRIGGER AS $$
declare iddi integer;
BEGIN 
select max(id) into iddi from activity;
if (iddi is null) then
iddi=0;
end if;
iddi=iddi+1;
insert into activity values (iddi,new.factory_site,new.area,new.name_typology,null,null,new.interruptibility,new.week,new.workspace_notes,'extra');
return new;
END $$ language plpgsql;

create trigger insertextra
instead of insert on extra_activity
for each row execute procedure insert_extra();



drop view if exists ewo_activity;
create view ewo_activity as 
select factory_site,area,name_typology,interruptibility,week,workspace_notes 
from activity;


drop function if exists insert_ewo cascade;
CREATE OR REPLACE FUNCTION insert_ewo() RETURNS TRIGGER AS $$
declare iddi integer;
BEGIN 
select max(id) into iddi from activity;
if (iddi is null) then
iddi=0;
end if;
iddi=iddi+1;
insert into activity values (iddi,new.factory_site,new.area,new.name_typology,null,null,new.interruptibility,new.week,new.workspace_notes,'ewo');
return new;
END $$ language plpgsql;

create trigger insertewo
instead of insert on ewo_activity
for each row execute procedure insert_ewo();

----------------INSERT NON GESTITI IN JAVA------------------
insert into site values('Fisciano','Biblioteca'), ('Fisciano','Mensa'),('Fisciano','Aula magna'),('Napoli','Sotterranei'),('Santamaria Castellabate','Porto'),
('San marco','Niente'), ('Fisciano','Aula H'),('Fisciano','Macchinette'),('Fisciano','Bar di lettere'),('Striano','Platano'),('Poggiomarino','Paninoteca da Alessio'),
('Poggiomarino','Nanninella'),('Striano','Banca');

insert into users values
('Pippo','psw1','maintainer'),
('Pluto','psw2','maintainer'),
('Paperino','psw3','maintainer'),
('Minnie','psw4','maintainer'),
('Topolino','psw5','maintainer');

insert into procedure values
('pr1'),
('pr2'),
('pr3'),
('pr4'),
('pr5');

insert into competence values
('PAV certification'),
('electrical manteinance'),
('knowledge of cable type'),
('xyz-type robot knowledge'),
('knowledge of robot workstation 23'),
('knowledge of AI'),
('knowledge of SCRUM'),
('knowledge of Java');

insert into possession_maintainer values
('PAV certification','Pippo'),
('PAV certification','Pluto'),
('PAV certification','Topolino'),
('electrical manteinance','Pippo'),
('electrical manteinance','Paperino'),
('knowledge of cable type','Pippo'),
('knowledge of cable type','Paperino'),
('knowledge of cable type','Topolino'),
('xyz-type robot knowledge','Pippo'),
('xyz-type robot knowledge','Pluto'),
('knowledge of robot workstation 23','Pippo'),
('knowledge of robot workstation 23','Topolino'),
('knowledge of AI','Topolino'),
('knowledge of SCRUM','Pippo'),
('knowledge of SCRUM','Paperino'),
('knowledge of SCRUM','Pluto'),
('knowledge of Java','Topolino'),
('knowledge of Java','Pippo');


insert into possession_procedure values
('PAV certification','pr1'),
('PAV certification','pr2'),
('PAV certification','pr5'),
('electrical manteinance','pr1'),
('electrical manteinance','pr4'),
('knowledge of cable type','pr1'),
('knowledge of cable type','pr3'),
('xyz-type robot knowledge','pr1'),
('knowledge of robot workstation 23','pr4'),
('knowledge of AI','pr2'),
('knowledge of SCRUM','pr5'),
('knowledge of Java','pr3'),
('knowledge of Java','pr4');

insert into typology values ('Hydraulic'),('Mechanical'),('Electronic'),('Chemical'),('Electrical'),('Informatical');

insert into material values ('Martello'),('Chiodi'),('Bulloni'),('Cacciaviti'),('Pinze'),('Chiave inglese'),('Mattarello'),('Joystick'),('Candela');
------------------------STORED FUNCTION-------------------
drop function if exists availability_1;
CREATE or replace FUNCTION availability_1 (proc varchar,wik int)
returns table(username varchar, mc bigint, pc bigint,lunedi bigint,martedi bigint,mercoledi bigint,giovedi bigint,venerdi bigint,sabato bigint,domenica bigint) language plpgsql as $$ begin
return query(
select L.username, L.mc,L.pc,
(select sum(minuti)  from assigned K, activity F where (giorno='lunedi' and K.id=F.id and K.username=L.username and F.week=wik)) as lunedi,
(select sum(minuti)  from assigned K, activity F where (giorno='martedi' and K.id=F.id and K.username=L.username and F.week=wik) ) as martedi,
(select sum(minuti)  from assigned K, activity F where (giorno='mercoledi' and K.id=F.id and K.username=L.username and F.week=wik) ) as mercoledi,
(select sum(minuti)  from assigned K, activity F where (giorno='giovedi' and K.id=F.id and K.username=L.username and F.week=wik) ) as giovedi,
(select sum(minuti)  from assigned K, activity F where (giorno='venerdi' and K.id=F.id and K.username=L.username and F.week=wik) ) as venerdi,
(select sum(minuti)  from assigned K, activity F where (giorno='sabato' and K.id=F.id and K.username=L.username and F.week=wik) ) as sabato,
(select sum(minuti)  from assigned K, activity F where (giorno='domenica' and K.id=F.id and K.username=L.username and F.week=wik) ) as domenica
from (select M.username, count(M.name_competence) as mc, (select count(name_competence) as pc from possession_procedure where (name_procedure=proc)) 
from possession_maintainer M,possession_procedure  P where (M.name_competence=P.name_competence and P.name_procedure=proc) group by M.username) as L group by L.username,L.pc,L.mc ); end; $$;


drop function if exists availability_2(varchar,int);
CREATE or replace FUNCTION availability_2 (proc varchar,wik int)
returns table(username varchar, mc bigint, pc bigint,lunedi bigint,martedi bigint,mercoledi bigint,giovedi bigint,venerdi bigint,sabato bigint,domenica bigint) language plpgsql as $$ begin
return query( select * from availability_1(proc,wik)
			 union
select L.username, 0 ,L.pc,
(select sum(minuti) from assigned K, activity F where (giorno='lunedi' and K.id=F.id and K.username=L.username and F.week=wik)) as lunedi,
(select sum(minuti) from assigned K, activity F where (giorno='martedi' and K.id=F.id and K.username=L.username and F.week=wik) ) as martedi,
(select sum(minuti) from assigned K, activity F where (giorno='mercoledi' and K.id=F.id and K.username=L.username and F.week=wik) ) as mercoledi,
(select sum(minuti) from assigned K, activity F where (giorno='giovedi' and K.id=F.id and K.username=L.username and F.week=wik) ) as giovedi,
(select sum(minuti) from assigned K, activity F where (giorno='venerdi' and K.id=F.id and K.username=L.username and F.week=wik) ) as venerdi,
(select sum(minuti) from assigned K, activity F where (giorno='sabato' and K.id=F.id and K.username=L.username and F.week=wik) ) as sabato,
(select sum(minuti) from assigned K, activity F where (giorno='domenica' and K.id=F.id and K.username=L.username and F.week=wik) ) as domenica
from (select Z.username, count(M.name_competence) as mc, (select count(name_competence) as pc from possession_procedure where (name_procedure=proc)) 
from possession_maintainer M,possession_procedure  P , users Z where
(M.name_competence!=P.name_competence and P.name_procedure=proc and Z.username!= M.username) group by Z.username) as L where(L.username not in (select availability_1.username from availability_1(proc,wik))) group by L.username,L.pc,L.mc ); end; $$;



drop function if exists availability_3(varchar,int);
CREATE or replace FUNCTION availability_3 (proc varchar,wik int)
returns table(username varchar, mc bigint, pc bigint,lunedi int,martedi int,mercoledi int,giovedi int,venerdi int,sabato int,domenica int) language plpgsql as $$ begin
return query (select availability_2.username,availability_2.mc,availability_2.pc,
			  cast(100-((availability_2.lunedi/420.0)*100) as integer),
			  cast(100-((availability_2.martedi/420.0)*100) as integer),
			  cast(100-((availability_2.mercoledi/420.0)*100) as integer),
			  cast(100-((availability_2.giovedi/420.0)*100) as integer),
			  cast(100-((availability_2.venerdi/420.0)*100) as integer),
			  cast(100-((availability_2.sabato/420.0)*100) as integer),
			  cast(100-((availability_2.domenica/420.0)*100) as integer) from availability_2(proc,wik));
			  end; $$;



drop function if exists management_ewo cascade;
CREATE OR REPLACE FUNCTION management_ewo() RETURNS TRIGGER AS $$
declare ewo varchar;
declare est int;
declare notavail int;
declare avail int;
declare interr int;
declare iddi int;
declare mins int;
declare maint varchar;
declare gg varchar;
declare wik int;
declare wik2 int;
declare fasc int;
BEGIN 
select type into ewo from activity A where (A.id=new.id);
if(ewo = 'ewo') then

	if(new.minuti=60) then
		return new;
	end if;
	select week into wik2 from activity A where (A.id=new.id);
	select sum(minuti) into notavail from assigned C,activity A where(A.interruptibility='false' and C.id=A.id and A.week=wik2 and C.fascia = new.fascia and C.giorno=new.giorno and C.username=new.username);
	if (notavail is null) then notavail:=0; end if;
	avail := 60 - notavail;
	select sum(minuti) into interr from assigned C,activity A where(A.interruptibility='true' and C.id=A.id and C.fascia = new.fascia and A.week=wik2 and C.giorno=new.giorno and C.username=new.username);
	if (avail >= interr + new.minuti) then
		return new;
	end if;
	est := new.minuti - avail + interr;
	while (est>0) loop
		select C.minuti,C.username,C.fascia,A.week,C.giorno,C.id into mins,maint,fasc,wik,gg,iddi from assigned C, activity A where (C.id=A.id and A.interruptibility='true' and C.fascia = new.fascia and A.week=wik2 and C.giorno=new.giorno and C.username=new.username) order by minuti desc;
		if (mins>=est) then
			update assigned set minuti = minuti - est where (fascia=fasc and username= maint and giorno=gg and id=iddi);
			update activity set estimated_time = estimated_time + est where (id=iddi);
			
		else
			delete from assigned where  (fascia=fasc and username= maint and giorno=gg and id=iddi);
			update activity set estimated_time = estimated_time + mins where (id=iddi);
			
		end if;
		est = est - mins;
	end loop;
end if;
return new;
END
$$ LANGUAGE plpgsql;

create trigger manage_ewo
before insert on assigned
for each row execute procedure management_ewo();



drop function if exists table_ewo;
create or replace function table_ewo(iddi integer,usnm varchar) returns
table(mc bigint, ec bigint) language plpgsql as $$ 
begin
return query (select count(name_competence) mc,(select count(name_competence) ec from ewo_competence where id=iddi) from possession_maintainer pm where (pm.name_competence in (select name_competence from ewo_competence where id=iddi) and pm.username=usnm)
); end; $$;
CREATE OR REPLACE FUNCTION funz5() RETURNS TRIGGER AS $$
BEGIN 
if (new.id in (select id from ewo_competence) and new.name_competence in (select name_competence from ewo_competence)) then
delete from ewo_competence where (id=new.id and name_competence=new.name_competence); 
end if;
return new;
END
$$ LANGUAGE plpgsql;


create trigger test5
before insert on ewo_competence
for each row execute procedure funz5();

drop function if exists updatewo cascade;
create or replace function updatewo(iddi integer) returns integer as $$
begin
delete from ewo_competence where id=iddi;
return iddi;
end $$ language plpgsql;

drop function if exists setestim;
create or replace function setestim() returns trigger as $$ 
begin
if (new.type='ewo' and new.estimated_time=0) then
new.estimated_time:=null;
end if;
return new;
end $$ language plpgsql;

create trigger setest
before insert on activity
for each row execute procedure setestim();



drop function if exists support cascade;
create or replace function support() returns trigger as $$ 
begin
if (new.type='planned') then
insert into possession_act values('pr1',new.id);
end if;
return new;
end $$ language plpgsql;

create trigger supportrigger
after insert on activity
for each row execute procedure support();


grant all privileges on activity to kek;
grant all privileges on assigned to kek;
grant all privileges on competence to kek;
grant all privileges on ewo_competence to kek;
grant all privileges on extra_competence to kek;
grant all privileges on material to kek;
grant all privileges on pm to kek;
grant all privileges on possession_act to kek;
grant all privileges on possession_maintainer to kek;
grant all privileges on possession_procedure to kek;
grant all privileges on procedure to kek;
grant all privileges on site to kek;
grant all privileges on typology to kek;
grant all privileges on users to kek;
grant all privileges on planned_activity to kek;
grant all privileges on ewo_activity to kek;
grant all privileges on extra_activity to kek;

grant all privileges on activity to kek2;
grant all privileges on assigned to kek2;
grant all privileges on competence to kek2;
grant all privileges on ewo_competence to kek2;
grant all privileges on extra_competence to kek2;
grant all privileges on material to kek2;
grant all privileges on pm to kek2;
grant all privileges on possession_act to kek2;
grant all privileges on possession_maintainer to kek2;
grant all privileges on possession_procedure to kek2;
grant all privileges on procedure to kek2;
grant all privileges on site to kek2;
grant all privileges on typology to kek2;
grant all privileges on users to kek2;
grant all privileges on planned_activity to kek2;
grant all privileges on ewo_activity to kek2;
grant all privileges on extra_activity to kek2;