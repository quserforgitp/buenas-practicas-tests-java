create table tutores(
    id bigint not null auto_increment,
    nombre varchar(100) not null,
    telefono varchar(14) not null unique,
    email varchar(100) not null unique,
    primary key(id)
);