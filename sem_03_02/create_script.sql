create table if not exists Artist (
	ArtistId bigint primary key,
	"Name" text
);

create table if not exists Album (
	AlbumId bigint primary key,
	Title text,
	ArtistId bigint,
	foreign key (ArtistId) references Artist(ArtistId) on delete cascade
);

create table if not exists Genre (
	GenreId bigint primary key,
	"Name" text
);

create table if not exists MediaType (
	MediaTypeId bigint primary key,
	"Name" text
);

create table if not exists Playlist (
	PlaylistId bigint primary key,
	"Name" text
);

create table if not exists Track (
    TrackId bigint primary key,
    "Name" text,
    AlbumId bigint,
    MediaTypeId bigint,
    GenreId bigint,
    Composer text,
    Milliseconds bigint,
    Bytes bigint,
    UnitPrice bigint,
    foreign key (AlbumId) references Album(AlbumId) on delete cascade,
    foreign key (MediaTypeId) references MediaType(MediaTypeId) on delete cascade,
    foreign key (GenreId) references Genre(GenreId) on delete cascade
);

create table if not exists PlaylistTrack (
	PlaylistId bigint,
	TrackId bigint,
	primary key (PlaylistId, TrackId),
	foreign key (PlaylistId) references Playlist(PlaylistId) on delete cascade,
	foreign key (TrackId) references Track(TrackId) on delete cascade
);
