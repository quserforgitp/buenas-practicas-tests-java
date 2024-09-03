create table adopciones(
    id bigint not null auto_increment,
    fecha datetime not null,
    tutor_id bigint not null,
    mascota_id bigint not null,
    motivo varchar(255) not null,
    status varchar(100) not null,
    justificativa_status varchar(255),
    primary key(id),
    constraint fk_adopciones_tutor_id foreign key(tutor_id) references tutores(id),
    constraint fk_adopciones_mascota_id foreign key(mascota_id) references mascotas(id)
);