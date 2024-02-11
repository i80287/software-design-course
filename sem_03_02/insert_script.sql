insert into artist(artistid, "Name") values
	( 0, 'artist0' ),
	( 1, 'artist1' ),
	( 2, 'artist2' );

insert into genre (genreid, "Name") values
	( 0, 'genre0' ),
	( 1, 'genre1' ),
	( 2, 'genre2' ),
	( 3, 'genre3' );

insert into mediatype (mediatypeid, "Name") values
	( 0, 'mediatype0' ),
	( 1, 'mediatype1' ),
	( 2, 'mediatype2' ),
	( 3, 'mediatype3' );

insert into playlist (playlistid, "Name") values
	( 0, 'playlist0' ),
	( 1, 'playlist1' ),
	( 2, 'playlist2' ),
	( 3, 'playlist3' );

insert into album (albumid, title, artistid) values
	( 0, 'album0', 0 ),
	( 1, 'album1', 0 ),
	( 2, 'album2', 1 ),
	( 3, 'album3', 1 ),
	( 4, 'album4', 2 ),
	( 5, 'album5', 2 );

insert into track(
	trackid,
	"Name",
	albumid,
	mediatypeid,
	genreid,
	composer,
	milliseconds,
	bytes,
	unitprice) values
	( 0, 'track0', 0, 1, 2, 'cmp1', 120000, 1048576, 100 ),
	( 1, 'track1', 2, 3, 3, 'cmp2', 240000, 1572864, 200 );

insert into playlisttrack (playlistid, trackid) values
	( 1, 0 ),
	( 0, 0 ),
	( 0, 1 );
