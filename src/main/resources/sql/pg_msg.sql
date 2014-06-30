CREATE OR REPLACE FUNCTION get_msg(p_guid character varying, p_fid NUMERIC)
  RETURNS t_kfs_msg AS
$BODY$
DECLARE
 msg t_kfs_msg;
BEGIN
	insert into t_kfs_msg (fid, g_guid) 
	SELECT p_fid, p_guid where not exists( select c_id from t_kfs_msg where fid = p_fid and g_guid = p_guid);

	select * into msg from t_kfs_msg where fid = p_fid and g_guid = p_guid;
	return msg;
end
$BODY$
LANGUAGE plpgsql VOLATILE COST 100;

