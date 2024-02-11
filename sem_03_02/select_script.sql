select t."Name" as TrackName, 
	alb.title as AlbumTitle, 
	g."Name" as GenreName,
	art."Name" as ArtistName,
	p."Name" as PlaylistName
from track t
join album alb on t.albumid = alb.albumid
join genre g on t.genreid = g.genreid
join artist art on art.artistid = alb.artistid
join playlist p on (p.playlistid, t.trackid) 
	in (select playlistid, trackid from playlisttrack);
