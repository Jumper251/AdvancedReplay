package me.jumper251.replay.utils;

import java.io.BufferedReader;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.ConfigManager;





public class Updater {

	private String currentVersion;
	private int id;
	private boolean versionAvailable;
	private static ExecutorService pool = Executors.newCachedThreadPool();

	public Updater(){
		this.currentVersion = ReplaySystem.getInstance().getDescription().getVersion();
		this.id = 52849;
		this.versionAvailable = false;

		if(ConfigManager.UPDATE_NOTIFY){
			checkForUpdate();
		}
	}
	
	public void checkForUpdate(){

		getContent(new Consumer<String>() {
			
			@Override
			public void accept(String version) {
				if(version != null && !version.equalsIgnoreCase(currentVersion)){
					versionAvailable = true;
				}
			}
		});
	}
	
	private void getContent(Consumer<String> consumer){

		pool.execute(new Acceptor<String>(consumer) {

			@Override
			public String getValue() {
				try{
					HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + id).openConnection();
		        	con.setDoOutput(true);
		        	con.setRequestMethod("POST");
		        	
		        	String version;
		        	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		        	version = bufferedReader.readLine();
		        	
		        	return version;
		        	
		        } catch(Exception e){
		        	e.printStackTrace();
		        	
		        	return null;
		        }
			}
			
		});

	}
	
	public boolean isVersionAvailable() {
		return versionAvailable;
	}
}
