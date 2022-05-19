drop table if exists CodeBlockTime;
create table CodeBlockTime
(
    id              integer primary key autoincrement, -- CodeBlockTime的id
    name            varchar(200),                 -- 名称
    commitId        varchar(45),                  -- commit hash值
    refactorType    varchar(50),                  -- 重构的类型，eg. Add_Package
    parentCodeBlock integer,                      -- 当前CodeBlockTime的父亲，指向某一个CodeBlock的id
    owner           integer,                      -- 当前CodeBlockTime的拥有者，指向某一个CodeBlock的id
    parameters      varchar(500)                  -- 只有Method类型的CodeBlockTime有这个值
);

drop table if exists CodeBlock;
create table CodeBlock
(
    id   integer primary key not null, -- CodeBlock的id
    type varchar(20)                   -- CodeBlock的类型，eg. Package/Class/Method/Attribute
);

drop table if exists CommitCodeChange;
create table CommitCodeChange
(
    commitId   varchar(45) primary key not null, -- commit hash值
    preCommit  varchar(45),                      -- 上一个CommitCodeChange
    postCommit varchar(45)                       -- 下一个CommitCodeChange
);

drop table if exists CodeBlockTime_link;
create table CodeBlockTime_link
(
    source    integer, -- 原始CodeBlockTime的id
    target    integer, -- 更改后CodeBlockTime的id
    link_type integer  -- 判断是pre/post关系`0`，还是deriver/derivee关系`1`
);

drop table if exists Mapping;
create table Mapping
(
    codeBlockSignature varchar(200), -- CodeBlock的签名
    id                 integer       -- CodeBlock的id
);

drop table if exists CodeBlockTimeChild;
create table CodeBlockTimeChild
(
    codeBlockTimeId integer,         -- CodeBlockTime的id
    codeBlockChildId     integer,    -- CodeBlockTime的孩子的id，即CodeBlock的id
    codeBlockChildType   varchar(20) -- CodeBlockTime的孩子的类型，即CodeBlock的类型，eg. class/method
);



select * from CommitCodeChange;

insert into CommitCodeChange values('22222','11111','33333');
insert into CommitCodeChange values('33333','22222','44444');

select * from sqlite_master;

select * from CodeBlockTime;


select * from CodeBlockTime;

select last_insert_rowid() as id from CodeBlockTime limit 1;
insert into CodeBlockTime (name,commitId,refactorType,parentCodeBlock,owner) values('test','123','Package',123,456);
