package db;

public class DBWriterFactory {

	public static DBWriter createDBWriter(String className){
		DBWriter dbWriter = null;
		if(className.equals("OnlineDBWriter")){
			dbWriter = OnlineDBWriter.getInstance();
	    }else if(className.equals("OfflineDBWriter")){
	    	dbWriter = new OfflineDBWriter();
	    }
	    return dbWriter;
	}
}
