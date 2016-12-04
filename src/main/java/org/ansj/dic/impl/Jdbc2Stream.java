package org.ansj.dic.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ansj.dic.PathToStream;
import org.ansj.exception.LibraryException;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

/**
 * jdbc:mysql://192.168.10.103:3306/infcn_mss?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull|username|password|select name as name,nature,freq from dic where type=1
 * 
 * @author ansj
 *
 */
public class Jdbc2Stream extends PathToStream {

	private static final byte[] TAB = "\t".getBytes();

	private static final byte[] LINE = "\n".getBytes();

	@Override
	public InputStream toStream(String path) {
		path = path.substring(7);

		String[] split = path.split("\\|");

		String jdbc = split[0];

		String username = split[1];

		String password = split[2];

		String sqlStr = split[3];

		SimpleDataSource ds = null;

		try {
			ds = new SimpleDataSource();

			ds.setJdbcUrl(jdbc);
			ds.setUsername(username);
			ds.setPassword(password);

			Dao dao = new NutDao(ds);

			Sql sql = Sqls.create(sqlStr);

			Sql execute = dao.execute(sql.setCallback(new SqlCallback() {
				@Override
				public byte[] invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					ByteArrayOutputStream baos = new ByteArrayOutputStream(100 * 1024);
					while (rs.next()) {
						try {
							int count = rs.getMetaData().getColumnCount();
							for (int i = 1; i < count; i++) {
								baos.write(String.valueOf(rs.getObject(i)).getBytes());
								baos.write(TAB);
							}
							baos.write(String.valueOf(rs.getObject(count)).getBytes());
							baos.write(LINE);

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return baos.toByteArray();
				}
			}));

			return new ByteArrayInputStream((byte[]) execute.getResult());
		} catch (Exception e) {
			throw new LibraryException(e);
		} finally {
			if (ds != null) {
				ds.close();
			}
		}

	}

}
