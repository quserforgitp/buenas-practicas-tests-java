create table mascotas(
    id bigint not null auto_increment,
    tipo varchar(100) not null,
    nombre varchar(100) not null,
    raza varchar(100) not null,
    edad int not null,
    color varchar(100) not null,
    peso decimal(4,2) not null,
    refugio_id bigint not null,
    adoptada boolean not null,
    primary key(id),
    constraint fk_mascota_refugio_id foreign key(refugio_id) references refugios(id)
);