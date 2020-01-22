create table payment
(
    id bigint not null auto_increment,
    payment_id varchar(255) not null,
    origin_account varchar(255) not null,
    destination_account varchar(255) not null,
    amount varchar(255) not null,
    payment_status varchar(255) not null,
    primary key (id)
)