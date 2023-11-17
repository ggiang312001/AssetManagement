
drop table if exists `return_request`;
drop table if exists `assignment`;
drop table if exists `asset`;
drop table if exists `user`;
drop table if exists `category`;
drop table if exists `location`;


create table `asset` (
    `asset_id` varchar(255) not null,
    `installed_date` datetime(6),
    `name` varchar(255) not null,
    `specification` varchar(255) not null,
    `state` varchar(255),
    `category` integer,
    `location_id` integer,
    `updated_at` datetime(6),
    primary key (`asset_id`)
);

create table `assignment` (
    `assignment_id` integer not null auto_increment,
    `assign_date` date not null,
    `assign_note` varchar(255),
    `state` varchar(255) not null,
    `asset_id` varchar(255),
    `assignee` varchar(255),
    `assigner` varchar(255),
    `updated_at` datetime(6),
    primary key (`assignment_id`)
);

create table `category` (
    `category_id` integer not null auto_increment,
    `name` varchar(255) not null,
    primary key (`category_id`)
);

create table `location` (
    `location_id` integer not null auto_increment,
    `city` varchar(255),
    primary key (`location_id`)
);

create table `return_request` (
    `request_id` integer not null auto_increment,
    `created_at` datetime(6) not null,
    `request_note` varchar(255),
    `returned_date` datetime(6),
    `state` varchar(255),
    `assignment_id` integer,
    `created_by` varchar(255),
    `accepted_by` varchar(255),
    primary key (`request_id`)
);

create table `user` (
    `staff_code` varchar(255) not null,
    `active` bit not null,
    `birthdate` date not null,
    `created_at` datetime(6) not null,
    `created_by` varchar(255) not null,
    `email` varchar(255),
    `first_name` varchar(255) not null,
    `gender` varchar(255) not null,
    `last_name` varchar(255) not null,
    `password` varchar(255),
    `role` varchar(255),
    `updated_at` datetime(6),
    `updated_by` varchar(255),
    `first_login` boolean not null,
    `username` varchar(255),
    `location_id` integer,
    primary key (`staff_code`)
);


alter table `asset` 
    add constraint FK_AssetCategory
    foreign key (`category`) 
    references `category` (`category_id`);

alter table `asset` 
    add constraint FK_AssetLocation 
    foreign key (`location_id`) 
    references `location` (`location_id`);


alter table `assignment` 
    add constraint FK_AssignmentAsset 
    foreign key (`asset_id`) 
    references `asset` (`asset_id`);


alter table `assignment` 
    add constraint FK_AssignmentAssignee 
    foreign key (`assignee`) 
    references `user` (`staff_code`);


alter table `assignment` 
    add constraint FK_AssignmentAssigner 
    foreign key (`assigner`) 
    references `user` (`staff_code`);


alter table `return_request` 
    add constraint FK_ReturnRequestAssignment 
    foreign key (`assignment_id`) 
    references `assignment` (`assignment_id`);


alter table  `return_request`
    add constraint FK_ReturnRequestCreatedBy 
    foreign key (`created_by`) 
    references `user` (`staff_code`);

alter table  `return_request`
    add constraint FK_ReturnRequestAcceptedBy
    foreign key (`accepted_by`)
    references `user` (`staff_code`);

alter table `user`
    add constraint FK_UserLocation 
    foreign key (`location_id`) 
    references `location` (`location_id`)
