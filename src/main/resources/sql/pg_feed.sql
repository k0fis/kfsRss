CREATE OR REPLACE FUNCTION get_feed(p_url character varying)
  RETURNS t_kfs_feed AS
$BODY$
DECLARE
 feed t_kfs_feed;
BEGIN
	insert into t_kfs_feed (c_url, c_desc, c_categ) 
	SELECT p_url, '', '' where not exists( select c_id from t_kfs_feed where c_url = p_url);

	select * into feed from t_kfs_feed where c_url = p_url;
	return feed;
end
$BODY$
LANGUAGE plpgsql VOLATILE COST 100;

