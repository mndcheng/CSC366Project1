/*drop table if exists ExtraCharges;
drop table if exists ChargeType;
drop table if exists Bill;
drop table if exists Reservations;
drop table if exists Customer;
drop table if exists RoomPrices;
drop table if exists Employees;
drop table if exists Rooms; 
drop table if exists BedInfo; 

create table BedInfo(
    ID integer primary key,
    NumBed integer,
    BedType integer
);

create table Rooms(
    RoomNum integer primary key,
    RoomView integer check (RoomView > 0 AND RoomView >= 12),
    BedID integer references BedInfo (ID),
    check (RoomNum % 100 <= 12),
    check (RoomNum < 600)
);

create table Employees(
    Login text primary key,
    Pwd text,
    Admin boolean
);

create table RoomPrices(
    ID serial primary key,
    RoomNum integer references Rooms (RoomNum),
    Price decimal,
    Date date
);

create table Customer(
    Login text not null primary key,
    Pwd text,
    FName text,
    LName text,
    Email text,
    Address text,
    CCN text,
    ExpDate date,
    CRCCode text
);

create table Reservations(
    RID serial primary key,
    CustLogin text references Customer,
    CheckIn date,
    CheckOut date,
    RoomNum integer references Rooms,
    ActualCheckOut date
);

create table Bill(
    ID serial primary key,
    ResID integer references Reservations,
    CustLogin text references Customer,
    Reason text,
    RoomPrice decimal check (RoomPrice > 0),
    ExtraPrice decimal check (RoomPrice > 0),
    TotalPrice decimal,
    Date date
);

create table ChargeType(
    ID serial primary key,
    Name text,
    Cost decimal
);

create table ExtraCharges(
    ID serial primary key,
    ResID integer references Reservations,
    ChargeID integer references ChargeType
);*/ 