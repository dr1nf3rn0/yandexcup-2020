WITH
	TEnhancedLog AS
	(
		SELECT 
			id,
			job_name,
			job_group,
			trigger_fire_time,
			job_finished_time,
			CAST(strftime('%s', job_finished_time) as integer) - CAST(strftime('%s', trigger_fire_time) as integer) AS duration,
			CAST(strftime('%s', CURRENT_TIMESTAMP) as integer) - CAST(strftime('%s', trigger_fire_time) as integer) AS since_start,
			(
				SELECT 
					MAX(CAST(strftime('%s', job_finished_time) as integer) - CAST(strftime('%s', trigger_fire_time) as integer)) 
				FROM 
					qrtz_log AS T 
				WHERE 
					T.job_name = Tlog.job_name 
				AND 
					T.host_name = TLog.host_name 
				AND
					T.job_status = "OK"
			) 
			AS max_host_duration,
			job_status,
			host_name
		FROM
			qrtz_log AS TLog
	),
	
	TEnhancedLog2 AS
	(
		SELECT
			T.id,
			T.job_name,
			T.job_group,
			T.trigger_fire_time,
			T.job_finished_time,
			CASE
				WHEN
					T.duration IS NULL
					AND
					T.max_host_duration IS NULL
					THEN -1
				WHEN
					T.duration IS NULL
					AND
					since_start <= 2 * T.max_host_duration
					THEN -1
				WHEN
					T.duration IS NULL
					THEN 0
				WHEN
					T.job_status <> "OK"
					THEN 0
				ELSE
					T.duration
			END AS duration,
			T.since_start,
			T.max_host_duration,
			T.job_status,
			T.host_name
		FROM
			TEnhancedLog AS T
	),
	
	TDurationDiff AS
	(
		SELECT
			T.job_name,
			coalesce(first_value(T.duration) OVER w - nth_value(T.duration,2) OVER w, 0) AS duration_diff
		FROM
			TEnhancedLog2 AS T
		WHERE
			T.duration > 0
		WINDOW w AS (PARTITION BY T.job_name ORDER BY trigger_fire_time DESC ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING)
	),
	
	TResult AS
	(
		SELECT	
			T.job_name,
			first_value(T.host_name) over w AS host_name,
			first_value(T.duration) over w AS duration,
			T2.duration_diff
		FROM
			TEnhancedLog2 AS T
		INNER JOIN
		(
			SELECT
				*
			FROM
				TDurationDiff AS T
				GROUP BY T.job_name
		) AS T2
		ON (T.job_name = T2.job_name)
		WHERE
			T.duration >= 0
		WINDOW w AS (PARTITION BY T.job_name ORDER BY trigger_fire_time DESC ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING)
	)

SELECT
	*
FROM
	TResult AS T
GROUP BY
	T.job_name