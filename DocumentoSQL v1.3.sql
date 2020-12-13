-----------DROP----------------
drop table if exists calendar cascade;
drop table if exists proceduraComp cascade;
drop table if exists maintainerComp cascade;
drop table if exists activity cascade;
drop table if exists procedura cascade;
drop table if exists competence cascade;
drop table if exists maintainer cascade;
drop table if exists for_maintainer cascade;
drop table if exists ewocomp cascade;
drop function if exists funz cascade;
drop function if exists funz2 cascade;
drop function if exists funz3 cascade;
drop function if exists funz4 cascade;
drop function if exists funz5 cascade;
drop function if exists forma cascade;
drop function if exists table1 cascade;
drop function if exists table2 cascade;
drop function if exists table3 cascade;
drop function if exists table1_ewo cascade;
drop function if exists table2_ewo cascade;
drop function if exists table3_ewo cascade;

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
description varchar(100) null,
estimatedTime integer null check (estimatedTime>=0),
interruptible boolean not null,
materials varchar(100) null,
week integer not null check(week>0 and week<53),
workspaceNotes varchar(100) null,
procedura varchar(30) null references procedura(nome) on delete cascade,
typ varchar(10) not null check(typ='planned' or typ='EWO' or typ='extra'),
check((typ='planned' and procedura is not null and estimatedTime is not null and description is not null) or (typ='EWO') or (typ='extra'))
);

create table ewoComp(
idactivity integer not null references activity(activityId) on delete cascade,
competence varchar(40) not null references competence(nome) on delete cascade,
primary key(idactivity,competence)
);


create table calendar(
maintainer varchar(10) not null references maintainer(username) on delete cascade,
idattivita integer  not null references activity(activityId) on delete cascade,
week integer null check(week>0 and week<53),
fascia integer not null check(fascia>0 and fascia<8),
day integer not null check(day > 0 and day<8),
minuti integer not null check(minuti>=0 and minuti<=60),
primary key(maintainer,idattivita,fascia,day)
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

CREATE or replace FUNCTION table1_ewo (iddi int)
returns table(maintainer varchar,mc bigint,cr bigint,week int,lun int,mar int,mer int,gio int,ven int,sab int,dom int) language plpgsql as $$ begin
return query (SELECT F.maintainer,S.mc,S.cr,F.week,F.lun,F.mar,F.mer,F.gio,F.ven,F.sab,F.dom FROM for_maintainer F,
			 (select mn.maintainer,count(competence) as mc,(select count(competence) from ewoComp where idactivity=iddi) as cr from maintainercomp as mn
			  where competence in (select competence from ewoComp where idactivity=iddi and competence=mn.competence) group by mn.maintainer) S where F.maintainer=S.maintainer);

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

create or replace function table2_ewo (iddi int)
returns table(maintainer varchar,mc bigint,cr bigint,week int,lun int,mar int,mer int,gio int,ven int,sab int,dom int) language plpgsql as $$ begin
return query(
select * from table1_ewo (iddi)
union
SELECT F.maintainer,0,(select table1_ewo.cr from table1_ewo(iddi) group by table1_ewo.cr),F.week,F.lun,F.mar,F.mer,F.gio,F.ven,F.sab,F.dom FROM for_maintainer F
where F.maintainer not in (SELECT F.maintainer FROM for_maintainer F,
			 (select mn.maintainer,count(competence) as mc,(select count(competence) from ewoComp where idactivity=iddi) as cr from maintainercomp as mn
			  where competence in (select competence from ewoComp where idactivity=iddi and competence=mn.competence) group by mn.maintainer) S where F.maintainer=S.maintainer));	
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

CREATE or replace FUNCTION table3_ewo (iddi int, wik int)
returns table(maintainer varchar,mc bigint,cr bigint,lun int,mar int,mer int,gio int,ven int,sab int,dom int) language plpgsql as $$ begin
return query (select table2_ewo.maintainer,table2_ewo.mc,table2_ewo.cr,cast(100-((table2_ewo.lun/420.0)*100) as integer),
			  cast(100-((table2_ewo.mar/420.0)*100) as integer),
			  cast(100-((table2_ewo.mer/420.0)*100) as integer),
			  cast(100-((table2_ewo.gio/420.0)*100) as integer),
			  cast(100-((table2_ewo.ven/420.0)*100) as integer),
			  cast(100-((table2_ewo.sab/420.0)*100) as integer),
			  cast(100-((table2_ewo.dom/420.0)*100) as integer) from table2_ewo(iddi) where(table2_ewo.week=wik));

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
select activityId into iddi from activity where ( activityId=old.activityId);
while(exists(select * from calendar where(idattivita=iddi))) loop
select maintainer into maint from calendar where (idattivita=iddi) group by maintainer;
select day into gg from calendar  where (idattivita=iddi and maintainer = maint) group by day;
select week into wik from calendar  where (idattivita=iddi) group by week;
select sum(minuti) into mins from calendar  where (idattivita=iddi and maintainer = maint and day = gg);
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
delete from calendar C where (C.idattivita=iddi and maintainer = maint and day = gg);
end loop;
return old;

END
$$ LANGUAGE plpgsql;

create trigger test3
before delete on activity 
for each row execute procedure funz3();

CREATE OR REPLACE FUNCTION forma(m varchar, w int, g int, mi int) returns integer as $$
BEGIN
if(g=1) then
update for_maintainer set lun = lun - mi where (week=w and maintainer=m);
elsif(g=2) then
update for_maintainer set mar = mar - mi where (week=w and maintainer=m);
elsif(g=3) then
update for_maintainer set mer= mer - mi where (week=w and maintainer=m);
elsif(g=4) then
update for_maintainer set gio = gio - mi where (week=w and maintainer=m);
elsif(g=5) then
update for_maintainer set ven = ven - mi where (week=w and maintainer=m);
elsif(g=6) then
update for_maintainer set sab = sab - mi where (week=w and maintainer=m);
elsif(g=7) then
update for_maintainer set dom = dom - mi where (week=w and maintainer=m);
end if;
return 1;
end; $$ language plpgsql;

CREATE OR REPLACE FUNCTION funz4() RETURNS TRIGGER AS $$
declare ewo varchar;
declare est int;
declare notavail int;
declare avail int;
declare interr int;
declare iddi int;
declare mins int;
declare maint varchar;
declare gg int;
declare wik int;
declare fasc int;
BEGIN 
select typ into ewo from activity where (activityid=new.idattivita);
if(ewo = 'EWO') then
	if(new.minuti=60) then
		return new;
	end if;
	select sum(minuti) into notavail from calendar C,activity A where(A.interruptible='false' and C.idattivita=A.activityId and C.fascia = new.fascia and C.week=new.week and C.day=new.day and C.maintainer=new.maintainer);
	if (notavail is null) then notavail:=0; end if;
	avail := 60 - notavail;
	select sum(minuti) into interr from calendar C,activity A where(A.interruptible='true' and C.idattivita=A.activityId and C.fascia = new.fascia and C.week=new.week and C.day=new.day and C.maintainer=new.maintainer);
	if (interr is null) then interr:=0; end if;
	if (avail >= interr + new.minuti) then
		return new;
	end if;
	est := new.minuti - (avail - interr);
	while (est>0) loop
		select C.minuti,C.maintainer,C.fascia,C.week,C.day,C.idattivita into mins,maint,fasc,wik,gg,iddi from calendar C, activity A where (C.idattivita=A.activityId and A.interruptible='true' and C.fascia = new.fascia and C.week=new.week and C.day=new.day and C.maintainer=new.maintainer) order by minuti desc;
		if (mins>=est) then
			update calendar set minuti = minuti - est where (fascia=fasc and maintainer= maint and week=wik and day=gg and idattivita=iddi);
			update activity set estimatedTime = estimatedTime + est where (activityId=iddi);
			perform forma(maint,wik,gg,est);
		else
			delete from calendar where  (fascia=fasc and maintainer= maint and week=wik and day=gg and idattivita=iddi);
			update activity set estimatedTime = estimatedTime + mins where (activityId=iddi);
			perform forma(maint,wik,gg,mins);
		end if;
		est = est - mins;
	end loop;
end if;
return new;
END
$$ LANGUAGE plpgsql;

create trigger test4
before insert on calendar 
for each row execute procedure funz4();


CREATE OR REPLACE FUNCTION funz5() RETURNS TRIGGER AS $$
BEGIN 
if (new.idactivity in (select idactivity from ewoComp) and new.competence in (select competence from ewoComp)) then
delete from ewoComp where (idactivity=new.idactivity and competence=new.competence); 
end if;
return new;
END
$$ LANGUAGE plpgsql;


create trigger test5
before insert on ewoComp
for each row execute procedure funz5();

drop function if exists updatewo cascade;
create or replace function updatewo(iddi integer) returns integer as $$
begin
delete from ewoComp where idactivity=iddi;
return iddi;
end $$ language plpgsql;

drop function if exists setestim;
create or replace function setestim() returns trigger as $$ 
begin
if (new.typ='EWO' and new.estimatedTime=0) then
new.estimatedTime:=null;
end if;
return new;
end $$ language plpgsql;

create trigger setest
before insert on activity
for each row execute procedure setestim();





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

--------------------GRANT PRIVILEGES-----------------------------
grant all privileges on activity to kek;
grant all privileges on calendar to kek;
grant all privileges on proceduraComp to kek;
grant all privileges on maintainerComp to kek;
grant all privileges on procedura to kek;
grant all privileges on competence to kek;
grant all privileges on maintainer to kek;
grant all privileges on for_maintainer to kek;
grant all privileges on ewoComp to kek;
