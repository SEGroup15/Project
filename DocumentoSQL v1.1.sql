-----------DROP----------------
drop table if exists calendar cascade;
drop table if exists proceduraComp cascade;
drop table if exists maintainerComp cascade;
drop table if exists activity cascade;
drop table if exists procedura cascade;
drop table if exists competence cascade;
drop table if exists maintainer cascade;
drop table if exists for_maintainer cascade;
drop trigger if exists test on calendar cascade;
drop trigger if exists test2 on maintainer cascade;
drop function if exists funz cascade;
drop function if exists funz2 cascade;
drop function if exists table1 cascade;
drop function if exists table2 cascade;
drop function if exists table3 cascade;

---------------CREAZIONE TABELLE--------------

create table procedura(
nome varchar(30) primary key);

create table competence(
nome varchar(40) primary key
);

create table maintainer(
username varchar(10) primary key,
passw varchar(10) not null
);

create table maintainerComp(
maintainer varchar(10) references maintainer(username) on delete cascade,
competence varchar(40) references competence(nome) on delete cascade,
primary key (maintainer,competence)
);

create table proceduraComp(
procedura varchar(30) references procedura(nome) on delete cascade,
competence varchar(40) references competence(nome) on delete cascade,
primary key (procedura,competence)
);


create table activity (
activityId integer primary key check (activityId>0),
factorySite varchar(20) not null,
area varchar(20) not null,
typology varchar(20) not null check(typology= 'electrical' or typology = 'electronic' or typology='hydraulic' or typology='mechanical' ),
description varchar(100) not null,
estimatedTime integer not null check (estimatedTime>=0),
interruptible boolean not null,
materials varchar(100) null,
week integer not null check(week>0 and week<53),
workspaceNotes varchar(100) null,
procedura varchar(30) not null references procedura(nome) on delete cascade
);

create table calendar(
maintainer varchar(10) not null references maintainer(username) on delete cascade,
idattivita integer  not null references activity(activityId) on delete cascade,
week integer null check(week>0 and week<53),
fascia integer not null check(fascia>0 and fascia<8),
day integer not null check(day > 0 and day<8),
minuti integer not null check(minuti>=0 and minuti<=60),
primary key(idattivita,fascia)
);


create table for_maintainer(
maintainer varchar(10) references maintainer(username) on delete cascade,
week integer null,
lun integer null,
mar integer null,
mer integer null,
gio integer null,
ven integer null,
sab integer null,
dom integer null
);

-----------CREAZIONE TRIGGER E FUNZIONI--------------------

CREATE or replace FUNCTION table1 (proc varchar)
returns table(maintainer varchar,mc bigint,pc bigint,week int,lun int,mar int,mer int,gio int,ven int,sab int,dom int) language plpgsql as $$ begin
return query (SELECT F.maintainer,S.mc,S.pc,F.week,F.lun,F.mar,F.mer,F.gio,F.ven,F.sab,F.dom FROM for_maintainer F,(select M.maintainer, count (M.competence) as mc, (select count (competence) as pc from proceduraComp where (procedura=proc))
from maintainerComp M, proceduraComp P where (M.competence=P.competence and P.procedura=proc)
group by M.maintainer) as S where (F.maintainer=S.maintainer)
			 );

end; $$;

create or replace function table2 (proc varchar)
returns table(maintainer varchar,mc bigint,pc bigint,week int,lun int,mar int,mer int,gio int,ven int,sab int,dom int) language plpgsql as $$ begin
return query(
select * from table1 (proc)
union
SELECT G.maintainer,0,S.pc,G.week,G.lun,G.mar,G.mer,G.gio,G.ven,G.sab,G.dom FROM for_maintainer G,(select M.maintainer, count (M.competence) as mc, (select count (competence) as pc from proceduraComp where (procedura=proc))
from maintainerComp M, proceduraComp P where (M.competence!=P.competence and P.procedura=proc)
group by M.maintainer) as S where (G.maintainer=S.maintainer and G.maintainer not in (select table1.maintainer from table1(proc))));
end; $$;


CREATE or replace FUNCTION table3 (proc varchar, wik int)
returns table(maintainer varchar,mc bigint,pc bigint,lun int,mar int,mer int,gio int,ven int,sab int,dom int) language plpgsql as $$ begin
return query (select table2.maintainer,table2.mc,table2.pc,cast(100-((table2.lun/420.0)*100) as integer),
			  cast(100-((table2.mar/420.0)*100) as integer),
			  cast(100-((table2.mer/420.0)*100) as integer),
			  cast(100-((table2.gio/420.0)*100) as integer),
			  cast(100-((table2.ven/420.0)*100) as integer),
			  cast(100-((table2.sab/420.0)*100) as integer),
			  cast(100-((table2.dom/420.0)*100) as integer) from table2(proc) where(table2.week=wik));

end; $$;

CREATE OR REPLACE FUNCTION funz() RETURNS TRIGGER AS $$
BEGIN 
if (new.maintainer in (select maintainer from for_maintainer)) then 
	if (new.day =1) then
		update for_maintainer set lun = lun + new.minuti where maintainer=new.maintainer and new.week=week;
	elsif (new.day =2) then
		update for_maintainer set mar = mar + new.minuti where maintainer=new.maintainer and new.week=week;
	elsif (new.day=3) then
		update for_maintainer set mer = mer + new.minuti where maintainer=new.maintainer and new.week=week;
	elsif (new.day =4) then
		update for_maintainer set gio = gio + new.minuti where maintainer=new.maintainer and new.week=week;
	elsif (new.day=5) then
		update for_maintainer set ven = ven + new.minuti where maintainer=new.maintainer and new.week=week;
	elsif (new.day =6) then
		update for_maintainer set sab = sab + new.minuti where maintainer=new.maintainer and new.week=week;
	elsif (new.day=7) then
		update for_maintainer set dom = dom + new.minuti where maintainer=new.maintainer and new.week=week;
	end if;
	return new;
end if;
END

$$ LANGUAGE plpgsql;

create trigger test
after insert on calendar
for each row execute procedure funz();


CREATE OR REPLACE FUNCTION funz2() RETURNS TRIGGER AS $$
declare i int;
BEGIN 
i=1;
while i<53 loop
insert into for_maintainer values (new.username,i,0,0,0,0,0,0,0);
i=i+1;
end loop;
return new;
END
$$ LANGUAGE plpgsql;


create trigger test2
after insert on maintainer
for each row execute procedure funz2();

CREATE OR REPLACE FUNCTION funz3() RETURNS TRIGGER AS $$
declare iddi int;
declare maint varchar;
declare gg int;
declare wik int;
declare mins int;
BEGIN 
if(exists(select * from calendar where(idattivita=old.activityId))) then
select activityId into iddi from activity where ( activityId=old.activityId);
select maintainer into maint from calendar where (idattivita=old.activityId) group by maintainer;
select day into gg from calendar  where (idattivita=old.activityId) group by day;
select week into wik from calendar  where (idattivita=old.activityId) group by week;
select sum(minuti) into mins from calendar  where (idattivita=old.activityId);
if(gg=1) then
update for_maintainer set lun = lun - mins where (week=wik and maintainer=maint);
elsif(gg=2) then
update for_maintainer set mar = mar - mins where (week=wik and maintainer=maint);
elsif(gg=3) then
update for_maintainer set mer= mer - mins where (week=wik and maintainer=maint);
elsif(gg=4) then
update for_maintainer set gio = gio - mins where (week=wik and maintainer=maint);
elsif(gg=5) then
update for_maintainer set ven = ven - mins where (week=wik and maintainer=maint);
elsif(gg=6) then
update for_maintainer set sab = sab - mins where (week=wik and maintainer=maint);
elsif(gg=7) then
update for_maintainer set dom = dom - mins where (week=wik and maintainer=maint);
end if;

end if;
return old;

END
$$ LANGUAGE plpgsql;


create trigger test3
before delete on activity 
for each row execute procedure funz3();

--------------INSERT NON GESTITI IN JAVA--------------

insert into maintainer values
('us1','psw1'),
('us2','psw2'),
('us3','psw3'),
('us4','psw4'),
('us5','psw5');

insert into procedura values
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
('knowledge of robot workstation 23');

insert into maintainerComp values
('us1','PAV certification'),
('us2','PAV certification'),
('us5','PAV certification'),
('us1','electrical manteinance'),
('us3','electrical manteinance'),
('us4','electrical manteinance'),
('us1','knowledge of cable type'),
('us3','knowledge of cable type'),
('us5','knowledge of cable type'),
('us1','xyz-type robot knowledge'),
('us2','xyz-type robot knowledge'),
('us1','knowledge of robot workstation 23'),
('us5','knowledge of robot workstation 23'),
('us4','knowledge of robot workstation 23');

insert into proceduraComp values
('pr1','PAV certification'),
('pr2','PAV certification'),
('pr5','PAV certification'),
('pr1','electrical manteinance'),
('pr4','electrical manteinance'),
('pr1','knowledge of cable type'),
('pr3','knowledge of cable type'),
('pr1','xyz-type robot knowledge'),
('pr4','knowledge of robot workstation 23');
