drop table if exists Bill;
drop table if exists Reservations;
drop table if exists Customer;
drop table if exists RoomPrices;
drop table if exists Employees;
drop table if exists Rooms; 

create table Rooms(
    RoomNum text primary key,
    RoomView integer check (RoomView <= 0 AND RoomView >= 6),
    Bed integer
);

create table Employees(
    Login text primary key,
    Pwd text,
    Admin boolean
);

create table RoomPrices(
    ID serial primary key,
    RoomNum text references Rooms(RoomNum),
    Price money,
    Date date
);

create table Customer(
    CID serial primary key,
    Login text not null,
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
    CID integer references Customer(CID),
    CheckIn date,
    CheckOut date,
    RoomNum text references Rooms(RoomNum),
    ActualCheckOut date
);

create table Bill(
    ID serial primary key,
    ResID integer references Reservations(RID),
    CID integer references Customer(CID),
    Reason text,
    Price money,
    Date date
);